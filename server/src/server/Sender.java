package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

import static server.ChatRoom.usersInRoom;
import static server.ClientListener.clients;

/**
 * Responsible for sending packets to client.
 */
public class Sender {

    private Server server = new Server();
    private ObjectOutputStream writer;


    /**
     * Sends object to every clients connected with server.
     *
     * @param message
     */
    protected void sendToAll(Object message) {
        System.out.println("Writing to all...");
        Iterator iterator = clients.entrySet().iterator();

        try {
            while (iterator.hasNext()) {

                Map.Entry<String, Client> client = (Map.Entry) iterator.next();

                writer = client.getValue().getOutputStream();
                writer.writeObject(message);
                writer.flush();

            }
        } catch (Exception ex) {
            server.hungUp(writer);
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
                writer = client.getOutputStream();
                writer.writeObject(message);
                writer.flush();
            }
        } catch (Exception ex) {
            server.hungUp(writer);
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
            writer = client;
            writer.writeObject(object);
            writer.flush();
        } catch (IOException e) {
            server.hungUp(writer);
        }
    }
}
