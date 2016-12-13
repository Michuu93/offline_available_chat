package sample;

import common.MessagePacket;
import common.UsersPacket;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;

/**
 * Reader Thread - read messages from server.
 */
public class ReaderThread implements Runnable {
    private Object received;

    @Override
    public void run() {
        while (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.CONNECTED) {
            try {
                if ((received = Main.getConnection().getReader().readObject()) != null) {
                    if (received instanceof MessagePacket) {
                        MessagePacket msg = (MessagePacket) received;
                        System.out.println("Received MessagePacket! Room ID: " + msg.getRoom() + ", Message: " + msg.getMessage());
                        Platform.runLater(() -> Main.getMainController().viewMessage(msg));
                    } else if (received instanceof UsersPacket) {
                        UsersPacket roomUsers = (UsersPacket) received;
                        Platform.runLater(() -> Main.setRoomUsersList(roomUsers.getRoom(), roomUsers.getClientsList()));
                        System.out.println("Received UsersPacket! Room ID: " + roomUsers.getRoom() + ", Users List: " + roomUsers.getClientsList());
                        Platform.runLater(() -> Main.getMainController().fillUsersList(roomUsers.getRoom()));
                    } else {
                        System.out.println("Received something else!");
                    }
                }
            } catch (SocketException e) {
                //e.printStackTrace();
                System.out.println("SocketException - Disconnect!");
                reconnect();
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

    /**
     * Kill Reader Thread.
     */
    public void killThread() {
        Platform.runLater(() -> Main.getConnection().disconnect());
        Thread.currentThread().interrupt();
    }

    /**
     * Calls the reconnect method (when the server disconnects).
     */
    public void reconnect() {
        Platform.runLater((() -> Main.getConnection().reconnect()));
    }
}
