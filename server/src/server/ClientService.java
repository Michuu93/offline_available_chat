package server;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class ClientService implements Runnable{

    private ObjectInputStream inStream;

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
        read();
    }

    private void read(){
        //todo: czytanie wiadomosci oraz nazwy pokoju z pakietu
        Object packet;
        try{
            while ((packet = inStream.readObject()) != null){
                serialize(packet);
                sendToEveryone(packet);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sendToEveryone(Object message) {
        Server server = new Server();
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
