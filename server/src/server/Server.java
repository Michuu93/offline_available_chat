package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

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
                ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
                //TODO: przypisywanie pokoju
                Client client = new Client(clientSocket, clientOutputStream, clientInputStream);
                clients.add(client);
                deliverToClient(clientOutputStream, chatRoomsList);
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

    private void deliverToClient(ObjectOutputStream client, Object object) {
        try {
            writer = (ObjectOutputStream) client;
            writer.writeObject(chatRoomsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendToAll(Object message) {
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

    protected void hungUp(ObjectInputStream reader){
        Iterator<Client> iterator = clients.iterator();
        while(iterator.hasNext()){
            Client client = iterator.next();
            if (client.getInputStream() == reader){
                try {
                    client.getSocket().close();
                    iterator.remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void setNickname(String nick, ObjectInputStream reader){
        Iterator<Client> iterator = clients.iterator();
        ObjectOutputStream writer = null;
        while(iterator.hasNext()){
            Client client = iterator.next();
            if (client.getInputStream() == reader){
                writer = client.getOutputStream();
                if (client.getNickName() != nick)
                client.setNickName(nick);
            }else{
                String error = "Nick jest zajęty!";
                deliverToClient(writer, error);
            }
        }
    }



}