package sample;

import javafx.application.Platform;
import common.MessagePacket;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ReaderThread implements Runnable {
    private ObjectInputStream reader;

    @Override
    public void run() {
        Object received;

        while (true) {
            Platform.runLater(() -> Main.getMainController().setStatus(Main.getConnection().isConnected()));
            if (Main.getConnection().isConnected()) {
                System.out.println("Reader działa.");
                //Read from server
                try {
                    reader = new ObjectInputStream(Main.getConnection().getSocket().getInputStream());
                    if ((received = reader.readObject()) != null) {
                        if (received instanceof MessagePacket) {
                            System.out.println("Odebrano MessagePacket");
                            MessagePacket msg = (MessagePacket) received;
                            System.out.println("Odebrano: ID pokoju: " + msg.getRoom() + " Wiadomość: " + msg.getMessage());
                        } else {
                            System.out.println("Odebrano coś innego");
                            String msg = (String) reader.readObject();
                            System.out.println("Odebrano " + msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Reader error!");

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Reader ClassNotFound!");
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }
}