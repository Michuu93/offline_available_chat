package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class ClientListener {

    private static Map<String, Client> clients = new HashMap<>();
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private ChatSession chatSession;

    public ClientListener(Map<String, Client> clients, ObjectInputStream reader, ObjectOutputStream writer, ChatSession chatSession){
        this.clients = clients;
        this.reader = reader;
        this.writer = writer;
        this.chatSession = chatSession;
    }

    protected String getClientNickname() {
        String nick = null;
        Object object;
        try {
            while ((object = reader.readObject()) != null) {
                nick = (String) object;
                System.out.println("Received nick: " + nick);
                return nick;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Client addClient(String nick){
        Client client = new Client(reader, writer);
        clients.put(nick, client);
        return client;
    }

    protected Client verifyNick(String nick){
        Client client = null;

        if (clients.isEmpty()){
            client = addClient(nick);
            admitClient();

        }else{

            if (clients.containsKey(nick)){
                System.out.println("Nick is taken, choose new one.");
                chatSession.deliverToClient(writer, false);
            }else{
                client = addClient(nick);
                admitClient();
            }
        }
        return client;
    }

    protected void admitClient() {
        chatSession.deliverToClient(writer, true);
        System.out.println("Sending chat rooms list...");
        chatSession.deliverToClient(writer, new Server().getChatRoomsList());
    }
}
