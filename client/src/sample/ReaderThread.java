package sample;

import javafx.application.Platform;
import common.MessagePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ReaderThread implements Runnable {
    private Object received;

    @Override
    public void run() {
        System.out.println("reader");
        while (true) {
            //Read from server
            try {
                if (Main.getConnection().isConnected() && ((received = Main.getConnection().getReader().readObject()) != null)) {
                    if (received instanceof MessagePacket) {
                        System.out.println("Odebrano MessagePacket");
                        MessagePacket msg = (MessagePacket) received;
                        System.out.println("Odebrano: ID pokoju: " + msg.getRoom() + " Wiadomość: " + msg.getMessage());
                    } else {
                        System.out.println("Odebrano coś innego");
                        System.out.println(received);
                    }
                }
            } catch (SocketException e) {
                //e.printStackTrace();
                System.out.println("Disconnect!");
                killThread();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Reader error!");
                killThread();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                System.out.println("Reader ClassNotFound!");
                killThread();
            }
        }
    }

    public void killThread() {
        Platform.runLater(() -> {
            try {
                Main.getConnection().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread.currentThread().interrupt();
    }
}
