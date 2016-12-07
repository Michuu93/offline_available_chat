package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import static server.ChatRoom.usersInRoom;
import static server.ClientListener.clients;

/**
 * Responsible for sending packets to client.
 */
public class Sender {


    /**
     * Sends object to every clients connected with server.
     *
     * @param message
     */
    protected void sendToAll(Object message) {
        System.out.println("Writing to all...");
        for (Map.Entry<String, Client> client : clients.entrySet()) {
            try {
                client.getValue().getOutputStream().writeObject(message);
                client.getValue().getOutputStream().flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Sends object to every clients connected with given room, specified with room argument.
     *
     * @param room    chat room name
     * @param message
     */
    protected void sendToUsersInRoom(String room, Object message) {
        System.out.println("Writing to users in room: " + room);
        try {
            for (Client client : usersInRoom.get(room)) {
                client.getOutputStream().writeObject(message);
                client.getOutputStream().flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sends object to given client, specified by client argument.
     *
     * @param client client's output stream.
     * @param object
     */
    protected void sendToClient(ObjectOutputStream client, Object object) {
        try {
            ObjectOutputStream writer = client;
            writer.writeObject(object);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
