package sample;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Socket socket = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private static int RECONNECTING_TIME = 150;

    public enum ConnectStatus {
        CONNECTED, DISCONNECTED, RECONNECTING
    }

    private ConnectStatus connectStatus = ConnectStatus.DISCONNECTED;

    public void connect(String server, int port) throws IOException, ClassNotFoundException {
        try {
            socket = new Socket(server, port);
            writer = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            writer.flush();
            reader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            sendNick();
            //sprawdzanie nicku
            Boolean nickCheck;
            if ((nickCheck = (Boolean) reader.readObject()) != null) {
                System.out.println("NickCheck: " + nickCheck);
                if (nickCheck == true) {
                    System.out.println("Connected to " + server + ":" + port);
                    setConnectStatus(ConnectStatus.CONNECTED);
                    getRoomsList();
                    startReaderThread();
                } else if (nickCheck == false) {
                    System.out.println("Nick zajęty! Disconnect!");
                    Main.getConnectController().setConnectLabel("Nick is already in use, please choose another!");
                    disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Main.getConnectController().setConnectLabel("Server is not responding!");
        }
    }

    public void disconnect() throws IOException {
        if (getConnectStatus() == ConnectStatus.CONNECTED) {
            setConnectStatus(ConnectStatus.DISCONNECTED);
            readerThread.interrupt();
            socket.close();
            Main.getMainController().clearRoomsList();
            Main.getChatRoomsList().clear();
            Main.getJoinedChatRoomsTabs().clear();
            Main.getMainController().closeTabs();
            Main.getMainController().clearUsersList();
            Main.clearRoomUsersList();
            System.out.println("Disconnect!");
        }
    }

    public void reconnect() throws IOException, ClassNotFoundException, InterruptedException {
        if (getConnectStatus() == ConnectStatus.CONNECTED) {
            setConnectStatus(ConnectStatus.RECONNECTING);
            readerThread.interrupt();
            socket.close();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int waitedSeconds = 0;
                    while (waitedSeconds < RECONNECTING_TIME) {
                        System.out.println("Reconnecting! - " + waitedSeconds + " sec");
                        Platform.runLater(() -> {
                            try {
                                Main.getConnection().connect(Main.getConnectController().getServer(), Main.getConnectController().getPort());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waitedSeconds += 2;
                    }
                    Platform.runLater(() -> {
                        try {
                            disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.currentThread().interrupt();
                }
            };
            thread.start();
        }
    }

    public ObjectInputStream getReader() {
        return reader;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
        Main.getMainController().setStatus(connectStatus);
    }

    public void startReaderThread() {
        System.out.println("Starting Reader thread.");
        readerThread = new Thread(readerJob);
        readerThread.setName("Reader Thread");
        readerThread.start();
    }

    public void sendNick() throws IOException {
        writer.writeObject(Main.getUserNick());
        writer.flush();
        System.out.println("Wysłano nick: " + Main.getUserNick());
    }

    public void getRoomsList() throws IOException, ClassNotFoundException {
        Main.setChatRoomsList((ArrayList<String>) reader.readObject());
        Main.getMainController().fillRoomsList();
    }
}
