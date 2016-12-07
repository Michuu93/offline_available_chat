package server;

import common.RoomPacket;
import common.UsersPacket;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static server.ChatSession.LOUNGE;
import static server.ClientListener.clients;


public class ChatRoom {

    /**
     * List of chat rooms names.
     */
    protected static ArrayList<String> chatRoomsList = new ArrayList<>();
    /**
     * Map with set of keys- chat rooms names and values- file stream to store rooms log.
     */
    protected static Map<String, FileOutputStream> roomsLog = new HashMap<>();
    /**
     * Stores clients list of each room.
     */
    protected static Map<String, List<Client>> usersInRoom = new HashMap<>();

    /**
     * Loads room to chat rooms list from txt file.
     */
    protected void loadRooms() {
        String filename = "src\\chatRooms.txt";
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                chatRoomsList.add(line);
                createUsersList(line);
                initializeLogFile(line);
            }
            initializeLogFile(LOUNGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates file for each chat room and puts it to rooms log map.
     * The line argument is name of a chat room.
     *
     * @param line- chat room name
     */
    private void initializeLogFile(String line) {
        roomsLog.clear();

        try {
            String filename = line + ".ser";
            FileOutputStream file = null;
            file = new FileOutputStream(filename, true);
            roomsLog.put(line, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Is responsible for adding and removing clients from chat rooms list based on flag from RoomPacket received from client.
     * The room argument is name of chat room, that client want to join or leave.
     *
     * @param room- chat room name
     * @param nick- client's nick
     * @param flag- join/unjoin flag
     */
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

    /**
     * Generates clients nicks list for chat room that is send to clients in room.
     * The room argument is name of chat room.
     *
     * @param room- name of chat room
     */
    private void generateNickList(String room) {
        UsersPacket usersPacket = new UsersPacket(room, usersInRoom.get(room).stream().map(Client::getNick).collect(Collectors.toList()));
        new Sender().sendToUsersInRoom(room, usersPacket);
    }

    /**
     * Initializes list of clients for chat room, specified by line argument- chat room name.
     *
     * @param room- name of chat room
     */
    private void createUsersList(String room) {
        usersInRoom.put(room, new ArrayList<Client>());
    }

}
