package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private String room;
    private String nick;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public Client() {

    }

    public Client(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.room = "Waiting room";
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
