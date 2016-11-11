package sample;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private boolean connected = false;
    private Socket socket = null;
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;

    public void connect(String server, int port) throws IOException {
        try {
            socket = new Socket(server, port);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            getRoomsList();
            System.out.println("Connected to " + server + ":" + port);
            setConnected(true);
            startThreads();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connecting problem!");
            setConnected(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Getting rooms list error!");
        }
    }

    public void disconnect() throws IOException {
        if (isConnected()) {
            inStream.close();
            outStream.close();
            Main.getMainController().clearRoomsList();
            Main.getChatRoomsList().clear();
            Main.getJoinedChatRoomsList().clear();
            Main.getMainController().closeTabs();
            setConnected(false);
        }
    }

    public void getRoomsList() throws IOException, ClassNotFoundException {
        Main.setChatRoomsList((ArrayList<String>) inStream.readObject());
        Main.getMainController().fillRoomsList();
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        Main.getMainController().setStatus(connected);
    }

    public boolean isConnected() {
        return connected;
    }

    public ObjectOutputStream getOutStream() {
        return outStream;
    }

    public ObjectInputStream getInStream() {
        return inStream;
    }

    public void startThreads() {
        System.out.println("Starting Reader and Writer threads.");
        readerThread = new Thread(readerJob);
        readerThread.setName("Reader Thread");
        readerThread.start();
    }
}
