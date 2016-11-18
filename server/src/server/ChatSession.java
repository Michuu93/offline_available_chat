package server;

import common.MessagePacket;
import common.RoomPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ChatSession implements Runnable {

    private ObjectInputStream reader;
    private Server server;
    private Map<String, Client> clients = new HashMap<>();

    public ChatSession(Server server, ObjectInputStream reader, Map<String, Client> clients) {
        try {
            this.server = server;
            this.clients = clients;
            this.reader = reader;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        read();
    }

    private void read() {
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
                        String room = messagePacket.getRoom();
                        if (room == server.getChatRoomsList().get(0))
                            sendToAll(messagePacket);
                        else
                            sendToUsersInRoom(room, messagePacket);
                        server.serialize(messagePacket);
                    }
                    if (object instanceof RoomPacket) {
                        RoomPacket roomPacket = (RoomPacket) object;
                        System.out.println("Received roompacket" + roomPacket.getRoom());
                        alterList(roomPacket.getRoom(), roomPacket.getNick(), (RoomPacket.Join) roomPacket.getJoin());
                    }
                }
            } catch (EOFException e) {
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
        System.out.println("Writing to all: " + message);
        for (Map.Entry<String, Client> client : clients.entrySet()) {
            try {
                client.getValue().getOutputStream().writeObject(message);
                client.getValue().getOutputStream().flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendToUsersInRoom(String room, Object message) {
        System.out.println("Writing to users in room: " + room);
        try {
            for (Client client : server.getUsersInRoomsMap().get(room)) {
                client.getOutputStream().writeObject(message);
                client.getOutputStream().flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void alterList(String room, String nick, RoomPacket.Join flag) {

        if (flag == RoomPacket.Join.JOIN) {
            System.out.println(nick + " joined to the " + room);
            server.getUsersInRoomsMap().get(room).add(clients.get(nick));
            sendToUsersInRoom(room, server.getUsersInRoomsMap().get(room));
        } else {
            System.out.println(nick + " unjoined from the " + room);
            server.getUsersInRoomsMap().get(room).remove(clients.get(nick));
            sendToUsersInRoom(room, server.getUsersInRoomsMap().get(room));
        }
    }


}
