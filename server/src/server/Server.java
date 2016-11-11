package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<String> chatRoomsList = new ArrayList<>();
    private Socket clientSocket;
    private static ObjectOutputStream writer;

    public static void main(String[] args) {

        new Server().connect();
    }

    private void connect() {
        loadRooms();
        try {
            ServerSocket serverSocket = new ServerSocket(9001);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                //TODO: przypisywanie pokoju
                Client client = new Client(clientSocket);
                clients.add(client);
                deliverRoomsList(clientOutputStream);
                Thread thread = new Thread(new ClientService(client));
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
            while ((line = reader.readLine()) != null) {
                chatRoomsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deliverRoomsList(ObjectOutputStream client) {
        try {
            writer = (ObjectOutputStream) client;
            writer.writeObject(chatRoomsList);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void sendToAll(Object message) {
        for (Client client: clients) {
            try{
                System.out.println("Writing to all: " + message);
                writer.writeObject(message);
                writer.flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}