package sample;

import java.io.*;
import java.net.Socket;

public class Connection {
    private static Connection connection;
    private boolean connected = false;
    private Socket socket = null;
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;

    public void connect(String server, int port) throws IOException {
        try {
            socket = new Socket(server, port);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            connected = true;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Connection problem.");
            connected = false;
        }
    }

    public void disconnect() throws IOException {
        outStream.close();
        connected = false;
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

}
