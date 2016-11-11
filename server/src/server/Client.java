package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private Socket Socket;
    private String room;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    Client(){

    }

    Client(Socket clientSocket){
        this.Socket = clientSocket;
        //this.outputStream = outputStream;
        //this.inputStream = inputStream;
        this.room = "Waiting room";
    }

    public Socket getSocket() {
        return Socket;
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
