package server;

import common.UsersPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.ChatRoom.chatRoomsList;
import static server.ChatSession.LOUNGE;

public class ClientListener {

    protected static Map<String, Client> clients = new HashMap<>();
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Sender sender = new Sender();

    public ClientListener(ObjectInputStream reader, ObjectOutputStream writer) {
        this.reader = reader;
        this.writer = writer;
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

    protected Client addClient(String nick) {
        Client client = new Client(reader, writer);
        clients.put(nick, client);
        return client;
    }

    protected boolean verifyNick(String nick) {
        if (clients.isEmpty()) {
            addClient(nick);
            admitClient();

        } else {

            if (clients.containsKey(nick)) {
                System.out.println("Nick is taken, choose new one.");
                sender.sendToClient(writer, false);
                return false;
            } else {
                addClient(nick);
                admitClient();
            }
        }
        return true;
    }

    protected void admitClient() {
        sender.sendToClient(writer, true);
        deliverRoomList();
        deliverUserList();
    }

    private void deliverUserList() {
        System.out.println("Sending users list...");
        List<String> usersList = new ArrayList<String>(clients.keySet());
        UsersPacket roomPacket = new UsersPacket(LOUNGE, usersList);
        sender.sendToAll(roomPacket);
    }

    private void deliverRoomList() {
        System.out.println("Sending chat rooms list...");
        sender.sendToClient(writer, chatRoomsList);
    }
}
