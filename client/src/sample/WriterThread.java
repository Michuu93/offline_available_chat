package sample;

import common.*;

import java.io.IOException;

public class WriterThread implements Runnable {

    public WriterThread(MessagePacket messagePacket) {
        try {
            messagePacket.setNick(Main.getUserNick()); //adding user nick to packet
            Main.getConnection().getWriter().writeObject(messagePacket);
            Main.getConnection().getWriter().flush();
            System.out.println("\nWysłano MessagePacket! Room ID: " + messagePacket.getRoom() + ", Message: " + messagePacket.getMessage());

            Thread.currentThread().interrupt(); //closing thread (I hope that's work)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WriterThread(RoomPacket roomPacket) {
        try {
            roomPacket.setNick(Main.getUserNick()); //adding user nick to packet
            Main.getConnection().getWriter().writeObject(roomPacket);
            Main.getConnection().getWriter().flush();
            System.out.println("\nWysłano RoomPacket! Room ID: " + roomPacket.getRoom() + ", JOIN: " + roomPacket.getJoin());

            Thread.currentThread().interrupt(); //closing thread (I hope that's work)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
    }

}