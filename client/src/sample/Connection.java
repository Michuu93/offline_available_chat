package sample;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Socket socket = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;
    private boolean connected = false;
    private ObjectInputStream reader;
    ObjectOutputStream writer;

    public void connect(String server, int port) throws IOException, ClassNotFoundException {
        try {
            socket = new Socket(server, port);
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to " + server + ":" + port);
            setConnected(true);
            getRoomsList();
            startReaderThread();
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
            Main.getJoinedChatRoomsList().clear();
            Main.getMainController().closeTabs();
            System.out.println("Disconnect!");
        }
    }

    public Socket getSocket() {
        return socket;
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
        Platform.runLater(() -> Main.getMainController().setStatus(connected));
    }

    public void startReaderThread() {
        System.out.println("Starting Reader thread.");
        readerThread = new Thread(readerJob);
        readerThread.setName("Reader Thread");
        readerThread.start();
    }

    public void getRoomsList() throws IOException, ClassNotFoundException {
        Main.setChatRoomsList((ArrayList<String>) reader.readObject());
        Main.getMainController().fillRoomsList();
    }

}
