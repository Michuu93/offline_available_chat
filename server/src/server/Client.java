package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private String room;
    private String nickName;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    Client(){

    }

    Client(Socket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream){
        this.socket = clientSocket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.room = "Waiting room";
    }

    public Socket getSocket() {
        return socket;
    }

    public String getRoom() {
        return room;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}
