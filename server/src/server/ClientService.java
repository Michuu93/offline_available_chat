package server;

import common.MessagePacket;

import java.io.*;
import java.util.ArrayList;

public class ClientService implements Runnable{

    private ObjectInputStream reader;
    private ArrayList<Client> clients;

    public ClientService (Client client){
        try{
            reader = new ObjectInputStream(client.getSocket().getInputStream());
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
        Boolean complete = true;
        while (true) {
            try {
                if (complete) {
                    if ((object = reader.readObject()) != null) {
                        MessagePacket messagePacket = (MessagePacket) object;
                        System.out.println("Read message from client: " + messagePacket.getRoom() + ": " + messagePacket.getMessage());
                        Server.sendToAll(messagePacket);
                        //serialize(messagePacket);
                    }
                }
            }catch (EOFException e){
                complete = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
