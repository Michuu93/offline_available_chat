package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Socket socket = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;
    private boolean connected = false;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

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
                    setConnected(true);
                    getRoomsList();
                    startReaderThread();
                } else if (nickCheck == false) {
                    System.out.println("Nick zajęty! Disconnect!");
                    Main.getConnectController().setConnectLabel("Nick is already in use, please choose another!");
                    disconnect();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Main.getConnectController().setConnectLabel("Server is not responding!");
        }
    }

    public void disconnect() throws IOException {
        if (isConnected()) {
            setConnected(false);
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

    public ObjectInputStream getReader() {
        return reader;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        Main.getMainController().setStatus(connected);
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
