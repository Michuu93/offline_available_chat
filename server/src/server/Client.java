package server;

import java.io.ObjectOutputStream;

public class Client {

    private ObjectOutputStream clientOutputStream;
    private String room;

    Client(){

    }

    Client(ObjectOutputStream clientOutputStream){
        this.clientOutputStream = clientOutputStream;
        this.room = "Waiting room";
    }

    Client(ObjectOutputStream clientOutputStream, String room){
        this.clientOutputStream = clientOutputStream;
        this.room = room;
    }

    public ObjectOutputStream getClientOutputStream() {
        return clientOutputStream;
    }

    public void setClientOutputStream(ObjectOutputStream clientOutputStream) {
        this.clientOutputStream = clientOutputStream;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
