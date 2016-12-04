package sample;

import common.*;

import java.io.IOException;

public class Writer {

    public static void writeMessagePacket(MessagePacket messagePacket) {
        try {
            messagePacket.setNick(Main.getUserNick()); //adding user nick to packet
            Main.getConnection().getWriter().writeObject(messagePacket);
            Main.getConnection().getWriter().flush();
            System.out.println("Wysłano MessagePacket! Room ID: " + messagePacket.getRoom() + ", Message: " + messagePacket.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRoomPacket(RoomPacket roomPacket) {
        try {
            roomPacket.setNick(Main.getUserNick()); //adding user nick to packet
            Main.getConnection().getWriter().writeObject(roomPacket);
            Main.getConnection().getWriter().flush();
            System.out.println("Wysłano RoomPacket! Room ID: " + roomPacket.getRoom() + ", JOIN: " + roomPacket.getJoin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}