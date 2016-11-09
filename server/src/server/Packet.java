package server;

import java.io.Serializable;

public class Packet implements Serializable {
    private String message;
    private String room;

    Packet(){}

    Packet(String message, String room){
        this.message = message;
        this.room = room;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
