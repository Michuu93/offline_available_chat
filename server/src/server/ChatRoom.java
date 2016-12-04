package server;

import common.RoomPacket;
import common.UsersPacket;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static server.ClientListener.clients;


public class ChatRoom {

    protected static ArrayList<String> chatRoomsList = new ArrayList<>();
    private Map<String, FileOutputStream> roomsLog = new HashMap<>();
    protected static Map<String, List<Client>> usersInRoom = new HashMap<>();

    protected void loadRooms() {
        String filename = "src\\chatRooms.txt";
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                chatRoomsList.add(line);
                createRoomsList(line);
                initializeLogFile(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeLogFile(String line) {
        roomsLog.clear();

        try {
            String filename = line + ".ser";
            FileOutputStream file = null;
            file = new FileOutputStream(filename);
            roomsLog.put(line, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry entry : roomsLog.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }


    protected void alterUsersMap(String room, String nick, RoomPacket.Join flag) {
        Client newUser = clients.get(nick);


        if (flag == RoomPacket.Join.JOIN) {

            System.out.println(nick + " joined to the " + room);
            usersInRoom.get(room).add(newUser);


        } else {

            System.out.println(nick + " unjoined from the " + room);
            usersInRoom.get(room).remove(newUser);

        }

        newUser.setNick(nick);
        generateNickList(room);
    }

    private void generateNickList(String room) {
        UsersPacket usersPacket = new UsersPacket(room, usersInRoom.get(room).stream().map(Client::getNick).collect(Collectors.toList()));
        new Sender().sendToUsersInRoom(room, usersPacket);
    }

    private void createRoomsList(String line) {
        usersInRoom.put(line, new ArrayList<Client>());
    }

    public Map<String, FileOutputStream> getRoomsLog() {
        return roomsLog;
    }
}
