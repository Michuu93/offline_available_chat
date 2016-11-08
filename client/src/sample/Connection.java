package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection {
    private static Connection connection;
    private boolean connected = false;
    private Socket socket = null;
    private BufferedReader reader = null;

    public void connect(String server, int port) throws IOException {
        try {
            socket = new Socket(server, port);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
            connected = true;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Connection problem.");
            connected = false;
        }
    }

    public void disconnect() throws IOException {
        reader.close();
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public BufferedReader getReader() {
        return reader;
    }

}
