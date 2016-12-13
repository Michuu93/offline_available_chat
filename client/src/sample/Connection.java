package sample;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Connection class.
 */
public class Connection {
    private Socket socket = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private ConnectStatus connectStatus = ConnectStatus.DISCONNECTED;
    private String server;
    private int port;

    /**
     * Connection status.
     */
    public enum ConnectStatus {
        CONNECTED, DISCONNECTED, RECONNECTING
    }

    /**
     * If nick is free, connects to the server.
     *
     * @param server - server ip.
     * @param port   - server port.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void connect(String server, int port) throws IOException, ClassNotFoundException {
        try {
            socket = new Socket(server, port);
            writer = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            writer.flush();
            reader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            sendNick();
            Boolean nickCheck;
            if ((nickCheck = (Boolean) reader.readObject()) != null) {
                System.out.println("NickCheck: " + nickCheck);
                if (nickCheck == true) {
                    System.out.println("Connected to " + server + ":" + port);
                    setConnectStatus(ConnectStatus.CONNECTED);
                    getRoomsList();
                    startReaderThread();
                } else if (nickCheck == false) {
                    System.out.println("Nick is already in use! Disconnect!");
                    Main.getConnectController().setConnectLabel("Nick is already in use, please choose another!");
                    disconnect();
                }
            }
        } catch (IOException e) {
            if (connectStatus == ConnectStatus.DISCONNECTED) {
                e.printStackTrace();
                Main.getConnectController().setConnectLabel("Server is not responding!");
            }
        }
    }

    /**
     * If connected, disconnect from the server.
     *
     * @throws IOException
     */
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
            System.out.println("Disconnected!");
        }
    }

    /**
     * Trying to reconnect to the server from the collaboration of about two seconds.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void reconnect() throws IOException, ClassNotFoundException, InterruptedException {
        if (getConnectStatus() == ConnectStatus.CONNECTED) {
            setConnectStatus(ConnectStatus.RECONNECTING);
            readerThread.interrupt();
            socket.close();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int waitedSeconds = 0;
                    while (connectStatus == ConnectStatus.RECONNECTING) {
                        System.out.println("Reconnecting! - " + waitedSeconds + " sec");
                        Platform.runLater(() -> {
                            try {
                                Main.getConnection().connect(getServer(), getPort());
                            } catch (IOException e) {
                                //e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                //.printStackTrace();
                            }
                        });
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waitedSeconds += 2;
                    }
                    Writer.sendMessagesToSend();
                    Thread.currentThread().interrupt();
                }
            };
            thread.start();
        }
    }

    /**
     * Get ObjectInputStream reader.
     *
     * @return reader - ObjectInputStream.
     */
    public ObjectInputStream getReader() {
        return reader;
    }

    /**
     * Get ObjectOutputStream writer.
     *
     * @return writer - ObjectOutputStream.
     */
    public ObjectOutputStream getWriter() {
        return writer;
    }

    /**
     * Get connection status.
     *
     * @return connectStatus.
     */
    public ConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
        Main.getMainController().setStatus(connectStatus);
    }

    /**
     * Start Reader Thread.
     */
    public void startReaderThread() {
        System.out.println("Starting Reader thread.");
        readerThread = new Thread(readerJob);
        readerThread.setName("Reader Thread");
        readerThread.start();
    }

    /**
     * Send nick to server.
     *
     * @throws IOException
     */
    public void sendNick() throws IOException {
        writer.writeObject(Main.getUserNick());
        writer.flush();
        System.out.println("Send nick: " + Main.getUserNick());
    }

    /**
     * Get rooms list and fill into roomsListView.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void getRoomsList() throws IOException, ClassNotFoundException {
        Main.setChatRoomsList((ArrayList<String>) reader.readObject());
        Main.getMainController().fillRoomsList();
    }

    /**
     * Return server ip.
     *
     * @return server.
     */
    public String getServer() {
        return server;
    }

    /**
     * Return server port.
     *
     * @return port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set server ip.
     *
     * @param server - server ip.
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Set server port.
     *
     * @param port - port.
     */
    public void setPort(int port) {
        this.port = port;
    }
}
