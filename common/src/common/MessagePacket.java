package common;

import java.io.*;

public class MessagePacket implements Externalizable {
    private String message;
    private String room;
    private String date;
    private String nick;

    public MessagePacket() {
    }

    public MessagePacket(String message, String room) {
        this.message = message;
        this.room = room;
        this.date = null;
    }

    public MessagePacket(String message, String room, String date, String nick) {
        this.message = message;
        this.room = room;
        this.date = date;
        this.nick = nick;
    }

    public MessagePacket(String message, String room, String nick) {
        this.message = message;
        this.room = room;
        this.date = " ";
        this.nick = nick;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(message);
        //if (nick != null)
        out.writeUTF(nick);
        out.writeUTF(room);
        //if (date != null)
        out.writeUTF(date);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.message = in.readUTF();
        this.nick = in.readUTF();
        this.room = in.readUTF();
        this.date = in.readUTF();
    }

}
