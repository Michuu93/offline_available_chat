package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import static server.ChatRoom.usersInRoom;
import static server.ClientListener.clients;

/**
 * Created by Niki on 2016-12-04.
 */
public class Sender {


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
