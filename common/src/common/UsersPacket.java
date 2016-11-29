package common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Pack200;

public class UsersPacket implements Externalizable {
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(room);
        out.writeInt(clientsList.size());
        for (String client: clientsList){
            out.writeUTF(client);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.room = in.readUTF();
        clientsList.clear();
        int listSize = in.readInt();
        for (int i = 0; i < listSize; i++) {
            this.clientsList.add(in.readUTF());
        }

    }
}
