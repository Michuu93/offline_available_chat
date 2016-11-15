package sample;

import javafx.application.Platform;
import common.*;

import java.io.IOException;
import java.net.SocketException;

public class ReaderThread implements Runnable {
    private Object received;

    @Override
    public void run() {
        while (true) {
            //Read from server
            try {
                if (Main.getConnection().isConnected() && ((received = Main.getConnection().getReader().readObject()) != null)) {
                    if (received instanceof MessagePacket) {
                        MessagePacket msg = (MessagePacket) received;
                        System.out.println("Odebrano MessagePacket! Room ID: " + msg.getRoom() + ", Message: " + msg.getMessage());
                        Main.getMainController().viewMessage(msg);
                    } else if (received instanceof UsersPacket){
                        UsersPacket msg = (UsersPacket) received;
                        System.out.println("Odebrano UsersPacket! Room ID: " + msg.getRoom() + ", Users List: " + msg.getClientsList());
                    } else {
                        System.out.println("Odebrano coś innego!");
                    }
                }
            } catch (SocketException e) {
                //e.printStackTrace();
                System.out.println("SocketException - Disconnect!");
                killThread();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("IOException - Reader error! - Disconnect!");
                killThread();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                System.out.println("ClassNotFoundException - Reader ClassNotFound! - Disconnect!");
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
