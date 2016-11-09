package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class ClientService implements Runnable{

    private ObjectInputStream inStream;
    private Server server = new Server();

    public ClientService (Socket socket){
        try{
            Socket clientSocket = socket;
            inStream = new ObjectInputStream(clientSocket.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        deliverRoomsList();
        read();
    }

    private void deliverRoomsList() {
        try {
            ObjectOutputStream outStream = (ObjectOutputStream) server.clientSocket.getOutputStream();
            outStream.writeObject(server.chatRoomsList);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(){
        Object object;
        try{
            while ((object = inStream.readObject()) != null){
                Packet packet = (Packet) object;
                serialize(packet);
                sendToAll(packet);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sendToAll(Object message) {
        Iterator iterator = server.outputStreams.iterator();
        while (iterator.hasNext()){
            try{
                ObjectOutputStream outStream = (ObjectOutputStream)iterator.next();
                outStream.writeObject(message);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void serialize(Object packet){
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("package.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
