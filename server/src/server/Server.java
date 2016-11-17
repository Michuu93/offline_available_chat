package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class Server {

    private static ArrayList<Client> clients = new ArrayList<>();
    private static ArrayList<String> chatRoomsList = new ArrayList<>();
    private static ClientListener clientListener;
    private ChatSession chatSession;

    public static void main(String[] args) {
        Server server = new Server();
        server.verifyClientList();
        server.connect();
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

                chatSession = new ChatSession(this, clientInputStream, clients);

                clientListener = new ClientListener(clients, clientInputStream, clientOutputStream, chatSession, chatRoomsList);
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
                chatRoomsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void hungUp(ObjectInputStream reader) {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getInputStream() == reader) {
                try {
                    System.out.println(client.getNickName() + " is diconnected.");
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

}