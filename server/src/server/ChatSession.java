package server;

import common.MessagePacket;
import common.RoomPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ChatSession implements Runnable {

    private ObjectInputStream reader;
    private Server server= new Server();
    private ChatRoom chatRoom = new ChatRoom();
    private Sender sender = new Sender();

    protected static final String LOUNGE = "Waiting room";

    public ChatSession(){}

    public ChatSession(ObjectInputStream reader) {
        try {
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
                        readMessage((MessagePacket) object);
                    }

                    else if (object instanceof RoomPacket) {
                        updateListing((RoomPacket) object);
                    }
                    else {
                        System.out.println("Sth else - " + object);
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

    private void updateListing(RoomPacket object) {
        RoomPacket roomPacket = object;
        System.out.println("Received roompacket " + roomPacket.getRoom());
        chatRoom.alterUsersMap(roomPacket.getRoom(), roomPacket.getNick(), (RoomPacket.Join) roomPacket.getJoin());
    }

    private void readMessage(MessagePacket messagePacket) {
        synchronized (this) {

            server.getCurrentDate();
            messagePacket.setDate(server.getCurrentDate());

            String room = messagePacket.getRoom();
            if (room.equals(LOUNGE))
                sender.sendToAll(messagePacket);
            else
                sender.sendToUsersInRoom(room, messagePacket);
               // new Serializer(chatRoom.getRoomsLog()).serialize(room, messagePacket);

            System.out.println(messagePacket.getDate() + ": Read message from client from " + room + ": " + messagePacket.getMessage());
        }
    }

}
