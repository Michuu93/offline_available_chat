package server;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

import static server.ChatRoom.roomsLog;


public class Serializer {

    /**
     * Serialiaze objects to given room log file.
     * @param room chat room name
     * @param packet message
     */
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

        Iterator iterator = roomsLog.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry log = (Map.Entry)iterator.next();
            String room = (String) log.getKey();
            try {
                Object object;
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(room + ".ser"));
                if ((object = inputStream.readObject()) != null) {
                    new Sender().sendToUsersInRoom(room, object);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
