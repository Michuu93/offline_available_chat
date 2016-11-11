package server;

import common.MessagePacket;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        read();
    }

    private void read(){
        Object object;

        while (true) {
            try {
                if ((object = inStream.readObject()) != null){
                    MessagePacket messagePacket = (MessagePacket) object;
                    System.out.println("Read message from client: " + messagePacket.getRoom() + ": " + messagePacket.getMessage());
                    sendToAll(messagePacket);
                    serialize(messagePacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToAll(Object message) {
        System.out.println("sendToAll method");
        for (Client client: server.clients) {
            try{
                ObjectOutputStream outStream = client.getClientOutputStream();
                System.out.println("Writing to all: " + message);
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
