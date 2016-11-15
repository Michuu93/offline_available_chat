package sample;

import common.MessagePacket;

import java.io.IOException;

public class WriterThread implements Runnable {

    public WriterThread(MessagePacket message) {
            try {
                Main.getConnection().getWriter().writeObject(message);
                Main.getConnection().getWriter().flush();
                System.out.println("\nWysłano MessagePacket! Room ID: " + message.getRoom() + ", Message: " + message.getMessage());

                //zamykanie wątku, nie wiem czy działa
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void run() { }

}