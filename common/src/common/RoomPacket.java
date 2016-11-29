package common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RoomPacket implements Externalizable {
    private String room;
    private Join join;
    private String nick;

    public enum Join {
        JOIN, UJNOIN
    }


    public RoomPacket() {
    }

    public RoomPacket(String room, Join join) {
        this.room = room;
        this.join = join;
    }

    public String getRoom() {
        return room;
    }

    public Enum getJoin() {
        return join;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setJoin(Join join) {
        this.join = join;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(String.valueOf(join));
        out.writeUTF(nick);
        out.writeUTF(room);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.join = Join.valueOf(in.readUTF());
        this.nick = in.readUTF();
        this.room = in.readUTF();
    }

}
