package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Server {

    private static ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<String> chatRoomsList = new ArrayList<>();
    private static ObjectOutputStream writer;

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

                ObjectOutputStream clientOutputStream = new ObjectOutputStream( new BufferedOutputStream(clientSocket.getOutputStream()));
                clientOutputStream.flush();
                ObjectInputStream clientInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                //TODO: przypisywanie pokoju

                String nickName = getClientNickname(clientInputStream);
                adminClient(nickName, clientInputStream);

                Client client = new Client(clientSocket, clientOutputStream, clientInputStream);
                clients.add(client);

                Thread thread = new Thread(new ClientService(client));
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

    private String getClientNickname(ObjectInputStream reader) {
        String nick = null;
        try {
            while ((nick = String.valueOf(reader.read())) != null) {
                return nick;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nick;
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
            writer = client;
            writer.writeObject(object);
            writer.flush();
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

    protected void adminClient(String nick, ObjectInputStream reader){
        Iterator<Client> iterator = clients.iterator();
        ObjectOutputStream writer = null;

        while(iterator.hasNext()){
            Client client = iterator.next();

            if (client.getInputStream() == reader){
                writer = client.getOutputStream();

                if (!Objects.equals(client.getNickName(), nick)){
                    client.setNickName(nick);
                    deliverToClient(writer, true);
                    deliverToClient(writer, chatRoomsList);
                }

            }else{
                deliverToClient(writer, false);
            }
        }
    }

    private void verifyClientList(){
        if (!clients.isEmpty()){
            deserialize();
        }
    }


    protected void serialize(Object packet){
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

    private void deserialize(){
        try {
            Object object;
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("package.ser"));
            if ((object = inputStream.readObject()) != null){
                sendToAll(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}