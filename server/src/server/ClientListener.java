package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class ClientListener {

    private ArrayList<Client> clients = new ArrayList<>();
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private ChatSession chatSession;
    private static ArrayList<String> chatRoomsList = new ArrayList<>();

    public ClientListener(ArrayList<Client> clients, ObjectInputStream reader, ObjectOutputStream writer, ChatSession chatSession, ArrayList<String> chatRoomsList){
        this.clients = clients;
        this.reader = reader;
        this.writer = writer;
        this.chatSession = chatSession;
        this.chatRoomsList = chatRoomsList;
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
        Client client = new Client(nick, reader, writer);
        clients.add(client);
        return client;
    }

    protected Client verifyNick(String nick){
        Client client = null;
        Iterator<Client> iterator = clients.iterator();

        if (clients.isEmpty()){
            client = addClient(nick);
            admitClient();
        }else{

            for (int i = 0; i < clients.size(); i++) {
                Client current = clients.get(i);
                if (!Objects.equals(current.getNickName(), nick)) {
                    client = addClient(nick);
                    admitClient();
                } else {
                    System.out.println("Nick is taken, choose new one.");
                    chatSession.deliverToClient(writer, false);
                }
            }
        }
        return client;
    }

    protected void admitClient() {
        chatSession.deliverToClient(writer, true);
        System.out.println("Sending chat rooms list...");
        chatSession.deliverToClient(writer, chatRoomsList);
    }
}
