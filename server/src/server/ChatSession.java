package server;

import common.MessagePacket;
import common.RoomPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSession implements Runnable {

    private ObjectInputStream reader;
    private Server server;
    private Map<String, Client> clients = new HashMap<>();
    private Map<String, List<Client>> usersInRooms = new HashMap<>();
    private Map<String, List<String>>  nicksInRooms = new HashMap<>();

    public ChatSession(Server server, ObjectInputStream reader, Map<String, Client> clients, Map<String, List<Client>> usersInRooms) {
        try {
            this.server = server;
            this.clients = clients;
            this.reader = reader;
            this.usersInRooms = usersInRooms;
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
                        alterMap(roomPacket.getRoom(), roomPacket.getNick(), (RoomPacket.Join) roomPacket.getJoin());
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
            for (Client client : usersInRooms.get(room)) {
                client.getOutputStream().writeObject(message);
                client.getOutputStream().flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void alterMap(String room, String nick, RoomPacket.Join flag) {

        if (flag == RoomPacket.Join.JOIN) {
            System.out.println(nick + " joined to the " + room);
            //clients.get(nick).setNick(nick);
            //addToNicksList(room);

            usersInRooms.get(room).add(clients.get(nick));

        } else {

            System.out.println(nick + " unjoined from the " + room);
            usersInRooms.get(room).remove(clients.get(nick));
            sendToUsersInRoom(room, usersInRooms.get(room));
            //deleteFromNicksList(room);
        }
    }

    private void addToNicksList(String room) {
        for (Client client : usersInRooms.get(room)) {
           // nicks.add(client.getNick());
        }
        //sendToUsersInRoom(room, nicks);
    }

    private void deleteFromNicksList(String room) {
        for (Client client : usersInRooms.get(room)) {
           // nicks.remove(client.getNick());
        }
       // sendToUsersInRoom(room, nicks);
    }


}
