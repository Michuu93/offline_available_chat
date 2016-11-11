package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    ArrayList<Client> clients = new ArrayList<>();
    ArrayList<String> chatRoomsList = new ArrayList<>();
    Socket clientSocket;

    public static void main(String[] args) {
        new Server().connect();
    }

    private void connect() {
        try {
            ServerSocket serverSocket = new ServerSocket(9001);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                loadRooms();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                //TODO: przypisywanie pokoju
                Client client = new Client(clientOutputStream);
                clients.add(client);
                deliverRoomsList(clientOutputStream);
                Thread thread = new Thread(new ClientService(clientSocket));
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

    private void loadRooms() {
        String filename = "src\\chatRooms.txt";
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            chatRoomsList.add("Waiting Room");
            while ((line = reader.readLine()) != null) {
                chatRoomsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deliverRoomsList(ObjectOutputStream client) {
        try {
            ObjectOutputStream outStream = (ObjectOutputStream) client;
            outStream.writeObject(chatRoomsList);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}