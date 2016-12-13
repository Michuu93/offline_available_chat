package sample;

import common.MessagePacket;
import common.RoomPacket;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Writer - write messages to server.
 */
public class Writer {
    private static ArrayList<MessagePacket> messagesToSend = new ArrayList<>();

    /**
     * Write messagePacket to server.
     *
     * @param messagePacket - messagePacket.
     */
    public static void writeMessagePacket(MessagePacket messagePacket) {
        try {
            Main.getConnection().getWriter().writeObject(messagePacket);
            Main.getConnection().getWriter().flush();
            System.out.println("Send MessagePacket! Room ID: " + messagePacket.getRoom() + ", Message: " + messagePacket.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write roomPacket to server.
     *
     * @param roomPacket - roomPacket.
     */
    public static void writeRoomPacket(RoomPacket roomPacket) {
        try {
            roomPacket.setNick(Main.getUserNick()); //adding user nick to packet
            Main.getConnection().getWriter().writeObject(roomPacket);
            Main.getConnection().getWriter().flush();
            System.out.println("Send RoomPacket! Room ID: " + roomPacket.getRoom() + ", JOIN: " + roomPacket.getJoin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add messages to sent when reconnect.
     *
     * @param messagePacketToSend - messagePacketToSend.
     */
    public static void addMessageToSend(MessagePacket messagePacketToSend) {
        messagesToSend.add(messagePacketToSend);
    }

    /**
     * Send messages from messagesToSend list.
     */
    public static void sendMessagesToSend() {
        if (!messagesToSend.isEmpty()) {
            messagesToSend.forEach(Writer::writeMessagePacket);
            messagesToSend.clear();
        }
    }
}