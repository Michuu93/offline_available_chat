package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niki on 2016-12-04.
 */
public class Serializer {

    private Map<String, FileOutputStream> roomsLog;

    public Serializer(){}

    public Serializer( Map<String, FileOutputStream> roomsLog){
        this.roomsLog = roomsLog;
    }

    protected void serialize(String room, Object packet) {
        try {
            FileOutputStream fileOutputStream = roomsLog.get(room);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void deserialize() {
        try {
            Object object;
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("package.ser"));
            if ((object = inputStream.readObject()) != null) {
                new Sender().sendToAll(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
