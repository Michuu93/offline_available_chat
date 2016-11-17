package server;

import common.MessagePacket;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatSession implements Runnable{

    private ObjectInputStream reader;
    private Server server;
    private ArrayList<Client> clients = new ArrayList<>();

    public ChatSession(Server server, ObjectInputStream reader, ArrayList<Client> clients){
        try{
            this.server = server;
            this.clients = clients;
            this.reader = reader;
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
                if (complete && (object = reader.readObject()) != null) {
                    if (object instanceof MessagePacket) {
                        server.getCurrentDate();
                        MessagePacket messagePacket = (MessagePacket) object;
                        messagePacket.setDate(server.getCurrentDate());
                        System.out.println(messagePacket.getDate() + ": Read message from client: " + messagePacket.getRoom() + ": " + messagePacket.getMessage());
                        sendToAll(messagePacket);
                        server.serialize(messagePacket);
                    }
                }
            }catch (EOFException e){
                complete = false;
                server.hungUp(reader);
            } catch (IOException e) {
                server.hungUp(reader);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected void deliverToClient(ObjectOutputStream client, Object object) {
        try {
            ObjectOutputStream writer = client;
            writer.writeObject(object);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendToAll(Object message) {
        for (Client client: clients) {
            try{
                System.out.println("Writing to all: " + message);
                client.getOutputStream().writeObject(message);
                client.getOutputStream().flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
