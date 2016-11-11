package common;

import java.io.Serializable;

public class MessagePacket implements Serializable {
    private String message;
    private String room;

    public void MessagePacket(){}

    public void MessagePacket(String message, String room){
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
