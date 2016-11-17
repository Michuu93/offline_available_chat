package common;

public class RoomPacket {
    private String room;
    private Join join;
    private String nick;

    public enum Join {
        JOIN, UJNOIN
    }


    public RoomPacket(){}

    public RoomPacket(String room, Join join){
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
}
