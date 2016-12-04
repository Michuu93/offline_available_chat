package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import static server.ClientListener.clients;

public class Server {

    private ClientListener clientListener;
    private ChatSession chatSession;

    public static void main(String[] args) {
        new ChatRoom().loadRooms();
        new Server().connect();
    }

    private void connect() {
        try {
            ServerSocket serverSocket = new ServerSocket(9001);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                clientOutputStream.flush();
                ObjectInputStream clientInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));

                chatSession = new ChatSession(clientInputStream);

                clientListener = new ClientListener(clientInputStream, clientOutputStream);
                if (clientListener.verifyNick(clientListener.getClientNickname())) {
                    Thread thread = new Thread(chatSession);
                    thread.start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void hungUp(ObjectInputStream reader) {
        Iterator iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Client> client = (Map.Entry) iterator.next();

            if (reader == client.getValue().getInputStream()) {
                try {
                    System.out.println(client.getKey() + " is diconnected.");
                    clients.remove(client);
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
            new Serializer().deserialize();
        }
    }

    protected String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        return date;
    }


}