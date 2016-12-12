package sample;

import common.MessagePacket;
import common.UsersPacket;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;

public class ReaderThread implements Runnable {
    private Object received;

    @Override
    public void run() {
        while (true) {
            //Read from server
            try {
                if ((Main.getConnection().getConnectStatus() == Connection.ConnectStatus.CONNECTED) && ((received = Main.getConnection().getReader().readObject()) != null)) {
                    if (received instanceof MessagePacket) {
                        MessagePacket msg = (MessagePacket) received;
                        System.out.println("Received MessagePacket! Room ID: " + msg.getRoom() + ", Message: " + msg.getMessage());
                        Platform.runLater( () -> Main.getMainController().viewMessage(msg));
                    } else if (received instanceof UsersPacket) {
                        UsersPacket roomUsers = (UsersPacket) received;
                        Platform.runLater(() -> Main.setRoomUsersList(roomUsers.getRoom(), roomUsers.getClientsList()));
                        System.out.println("Received UsersPacket! Room ID: " + roomUsers.getRoom() + ", Users List: " + roomUsers.getClientsList());
                        Platform.runLater(() -> Main.getMainController().fillUsersList(roomUsers.getRoom()));
                    }
                    else {
                        System.out.println("Received something else!");
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
                System.out.println("SocketException - Disconnect!");
                reconnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException - Reader error! - Disconnect!");
                killThread();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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

    public void reconnect() {
        Platform.runLater((() -> {
            try {
                Main.getConnection().reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
}
