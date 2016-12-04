package sample;

import common.MessagePacket;
import common.UsersPacket;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

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
                        System.out.println("Received MessagePacket! Room ID: " + msg.getRoom() + ", Message: " + msg.getMessage());
                        Main.getMainController().viewMessage(msg);
                    } else if (received instanceof UsersPacket) {
                        UsersPacket roomUsers = (UsersPacket) received;
                        Main.setRoomUsersList(roomUsers.getRoom(), roomUsers.getClientsList());
                        System.out.println("Received UsersPacket! Room ID: " + roomUsers.getRoom() + ", Users List: " + roomUsers.getClientsList());
                        Platform.runLater(() -> Main.getMainController().fillUsersList(roomUsers.getRoom()));
                    }
                    else {
                        System.out.println("Received something else!");
                    }
                }
            } catch (SocketException e) {
                //e.printStackTrace();
                System.out.println("SocketException - Disconnect!");
                killThread();
            } catch (IOException e) {
                e.printStackTrace();
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
