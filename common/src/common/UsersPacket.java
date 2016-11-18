package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersPacket implements Serializable {
    private String room;
    private List<String> clientsList = new ArrayList<String>();

    public UsersPacket(String room, List<String> clientsList) {
        this.room = room;
        this.clientsList = clientsList;
    }

    public UsersPacket() {

    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getClientsList() {
        return clientsList;
    }

    public void setClientsList(ArrayList<String> clientsList) {
        this.clientsList = clientsList;
    }
}
