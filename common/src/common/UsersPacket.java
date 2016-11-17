package common;

import java.util.ArrayList;

public class UsersPacket {
    private String room;
    private ArrayList<String> clientsList =  new ArrayList<String>();

    public UsersPacket(String room, ArrayList<String> clientsList) {
        this.room = room;
        this.clientsList = clientsList;
    }

    public UsersPacket(){

    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public ArrayList<String> getClientsList() {
        return clientsList;
    }

    public void setClientsList(ArrayList<String> clientsList) {
        this.clientsList = clientsList;
    }
}
