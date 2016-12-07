package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static server.ClientListener.clients;

/**
 * @version 1.0
 * @since 2016.11.05
 * Main class responsible for initialisation of server work and create connect with clients.
 */

public class Server {

    private ClientListener clientListener;
    private ChatSession chatSession;

    public static void main(String[] args) {
        new ChatRoom().loadRooms();
        new Server().connect();
    }

    /**
     * This method starts communication between a client program and a server program.
     */
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

    /**
     * Hung up method, which removes client from clients list and close the stream between client and server.
     * The reader argument is client that connection is interrupted.
     *
     * @param reader- client reader stream
     */
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

    /**
     *
     */
    private void verifyClientList() {
        if (!clients.isEmpty()) {
            new Serializer().deserialize();
        }
    }

    /**
     * Returns current time
     *
     * @return current time
     */
    protected String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());
        return time;
    }


}