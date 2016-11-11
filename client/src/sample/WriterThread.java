package sample;

import common.MessagePacket;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriterThread implements Runnable {

    public WriterThread(MessagePacket message) {
            try {
                Main.getConnection().getWriter().writeObject(message);
                Main.getConnection().getWriter().flush();
                System.out.println("Wysłano do pokoju: " + message.getRoom() + "\nwiadomość: " + message.getMessage());

                //zamykanie wątku, nie wiem czy działa
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void run() { }

}