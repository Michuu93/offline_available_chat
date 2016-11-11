package server;

import common.MessagePacket;
import java.io.FileOutputStream;
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

        }
    }
    @Override
    public void run() {
        read();
    }

    private void read(){
        Object object;
        try{
            while ((object = inStream.readObject()) != null){
                MessagePacket messagePacket = (MessagePacket) object;
                sendToAll(messagePacket.getMessage());
                serialize(messagePacket);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sendToAll(Object message) {
        for (Client client: server.clients) {
            try{
                ObjectOutputStream outStream = (ObjectOutputStream) client.getClientOutputStream();
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
