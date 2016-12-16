package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

import static server.ClientListener.clients;

/**
 * @version 1.0
 * @since 2016.11.05
 * Main class responsible for initialisation of server work and create connect with clients.
 */

public class Server {

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

                ChatSession chatSession = new ChatSession(clientInputStream);

                ClientListener clientListener = new ClientListener(clientInputStream, clientOutputStream);
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
     * Hung up method, which close the stream between client and server.
     * The stream argument is a client that connection was interrupted.
     *
     * @param stream- client reader/writer stream
     */
    protected void hungUp(Object stream) {

        ObjectInputStream reader;
        ObjectOutputStream writer;
        Iterator iterator = clients.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, Client> client = (Map.Entry) iterator.next();

            try {
                if (stream == (reader = client.getValue().getInputStream())) {
                    reader.close();
                    System.out.println(client.getKey() + " is diconnected.");
                    iterator.remove();
                } else if (stream == (writer = client.getValue().getOutputStream())) {
                    writer.close();
                    System.out.println(client.getKey() + " is diconnected.");
                    iterator.remove();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    /**
     * Returns current time
     *
     * @return current time
     */
    protected String getCurrentTime() {
        ZonedDateTime dateTime = ZonedDateTime.from(ZonedDateTime.now());
        String time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time;
    }


}