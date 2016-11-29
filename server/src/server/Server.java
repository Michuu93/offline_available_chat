package server;

import common.MessagePacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

    private static ClientListener clientListener;
    private ChatSession chatSession;
    private static Map<String, Client> clients = new HashMap<>();
    private static ArrayList<String> chatRoomsNamesList = new ArrayList<>();
    private Map<String, List<Client>> usersInRoomsMap = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.test();
        server.verifyClientList();
        server.connect();
    }

    private void test(){
        MessagePacket messagePacket = new MessagePacket("nick", "room", "wiadomosc", "29.11");
        serialize(messagePacket);
    }

    private void connect() {
        loadRooms();
        try {

            ServerSocket serverSocket = new ServerSocket(9001);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                clientOutputStream.flush();
                ObjectInputStream clientInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));

                //TODO: przypisywanie pokoju

                chatSession = new ChatSession(this, clientInputStream, clients, usersInRoomsMap);

                clientListener = new ClientListener(clients, clientInputStream, clientOutputStream, chatSession);
                String nickName = clientListener.getClientNickname();
                Client client = clientListener.verifyNick(nickName);

                Thread thread = new Thread(chatSession);
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
                chatRoomsNamesList.add(line);
                createRoomsList(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRoomsList(String line) {
        usersInRoomsMap.put(line, new ArrayList<Client>());
    }


    protected void hungUp(ObjectInputStream reader) {
        Iterator iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Client> client = (Map.Entry) iterator.next();
            if (reader == client.getValue().getInputStream()) {
                try {
                    System.out.println(client.getKey() + " is diconnected.");
                    reader.close();
                    iterator.remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void verifyClientList() {
        if (!clients.isEmpty()) {
            deserialize();
        }
    }

    protected void serialize(Object packet) {
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("package.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deserialize() {
        try {
            Object object;
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("package.ser"));
            if ((object = inputStream.readObject()) != null) {
                chatSession.sendToAll(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        return date;
    }

    public ArrayList<String> getChatRoomsList() {
        return chatRoomsNamesList;
    }


}