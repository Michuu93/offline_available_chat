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
    private Map<String, List<Client>> usersInRoom = new HashMap<>();
    private Map<String, FileOutputStream> roomLog = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.verifyClientList();
        server.connect();
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

                chatSession = new ChatSession(this, clientInputStream, clients, usersInRoom);

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
                initializeLogFile(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRoomsList(String line) {
        usersInRoom.put(line, new ArrayList<Client>());
    }

    private void initializeLogFile(String line) {
        roomLog.clear();

        try {
            String filename = line + ".ser";
            FileOutputStream file = null;
            file = new FileOutputStream(filename);
            roomLog.put(line, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry entry : roomLog.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
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

    protected void serialize(String room, Object packet) {
        try {
            FileOutputStream fileOutputStream = roomLog.get(room);
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