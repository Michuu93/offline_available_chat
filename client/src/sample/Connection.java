package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
            getRoomsList();
            connected = true;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("connection problem!");
            connected = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("getRoomList error!");
        }
    }

    public void disconnect() throws IOException {
        inStream.close();
        outStream.close();
        connected = false;
    }

    public void getRoomsList() throws IOException, ClassNotFoundException {
        ArrayList<String> roomsList = new ArrayList<String>();
        roomsList = (ArrayList<String>) inStream.readObject();
        Main.setChatRoomsList(roomsList);

        //wyświetlenie listy test
        Main.getChatRoomsList().forEach((a) -> System.out.println(a));
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
