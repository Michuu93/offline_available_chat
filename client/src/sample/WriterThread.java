package sample;

import common.MessagePacket;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriterThread implements Runnable {
    private ObjectOutputStream writer;

    public WriterThread(MessagePacket message) {
            try {
                writer = Main.getConnection().getOutStream();
                writer.writeObject(message);
                writer.flush();
                //writer.close();
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