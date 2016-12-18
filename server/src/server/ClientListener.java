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

/**
 * Responsible for adding new client to the chat.
 */

public class ClientListener {

    /**
     * Stores set of nick keys with clients that respond to them.
     */
    protected static Map<String, Client> clients = new HashMap<>();
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private static Sender sender = new Sender();

    /**
     * Constructor with with the given clients input stream and output stream.
     *
     * @param reader inputStream
     * @param writer outputStream
     */
    public ClientListener(ObjectInputStream reader, ObjectOutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Method receive nickname from client that want to join to the chat.
     *
     * @return nickname
     */
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

    /**
     * Rensponsible for creating and adding new client to the chat.
     *
     * @param nick clients nickname
     * @return client object
     */
    protected Client addClient(String nick) {
        Client client = new Client(reader, writer);
        clients.put(nick, client);
        return client;
    }

    /**
     * Veryfies if nickname which was chosen by client, isn't already taken.
     *
     * @param nick
     * @return true if nick is not taken or false
     */
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

    /**
     * Sends positive response to client.
     */
    protected void admitClient() {
        sender.sendToClient(writer, true);
        deliverRoomList();
        deliverUserList();
    }

    /**
     * Delivers all users list to client.
     */
    protected static void deliverUserList() {
        System.out.println("Sending users list...");
        List<String> usersList = new ArrayList<String>(clients.keySet());
        UsersPacket roomPacket = new UsersPacket(LOUNGE, usersList);
        sender.sendToAll(roomPacket);
    }

    /**
     * Delivers all chat rooms names list to client.
     */
    private void deliverRoomList() {
        System.out.println("Sending chat rooms list...");
        sender.sendToClient(writer, chatRoomsList);
    }
}
