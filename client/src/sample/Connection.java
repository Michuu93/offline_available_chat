package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Socket socket;
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;
    private Runnable readerJob = new ReaderThread();
    private Thread readerThread;

    public void connect(String server, int port) throws IOException {
        try {
            socket = new Socket(server, port);
            getRoomsList();
            System.out.println("Connected to " + server + ":" + port);
            startReaderThread();
        } catch (IOException e) {
            //e.printStackTrace();
            Main.getConnectController().setConnectLabel("Server is not responding!");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("Getting rooms list error!");
        }
    }

    public void disconnect() throws IOException {
        if (isConnected()) {
            stopReaderThread();
            socket.close();
            inStream.close();
            outStream.close();
            Main.getMainController().clearRoomsList();
            Main.getChatRoomsList().clear();
            Main.getJoinedChatRoomsList().clear();
            Main.getMainController().closeTabs();
        }
    }

    public void getRoomsList() throws IOException, ClassNotFoundException {
        Main.setChatRoomsList((ArrayList<String>) inStream.readObject());
        Main.getMainController().fillRoomsList();
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void startReaderThread() {
        System.out.println("Starting Reader thread.");
        readerThread = new Thread(readerJob);
        readerThread.setName("Reader Thread");
        readerThread.start();
    }

    public void stopReaderThread(){
        System.out.println("Stopping Reader thread.");
        readerThread.interrupt();
    }
}
