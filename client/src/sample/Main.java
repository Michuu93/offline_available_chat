package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main method.
 */
public class Main extends Application {
    private static Connection connection = new Connection();
    private static String userNick;
    private static ArrayList<String> chatRoomsList = new ArrayList<String>();
    private static HashMap<String, ListView> joinedChatRoomsTabs = new HashMap<>();
    private static HashMap<String, List<String>> roomUsersList = new HashMap<>();
    private static MainController mainController;
    private static ConnectController connectController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root, 790, 490));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Return reference to connection.
     *
     * @return connection reference.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Return user nick.
     *
     * @return userNick.
     */
    public static String getUserNick() {
        return userNick;
    }

    /**
     * Set user nick.
     *
     * @param userNick - user nick.
     */
    public static void setUserNick(String userNick) {
        Main.userNick = userNick;
    }

    /**
     * Return chat rooms list.
     *
     * @return chatRoomsList.
     */
    public static ArrayList<String> getChatRoomsList() {
        return chatRoomsList;
    }

    /**
     * Set chat rooms list.
     *
     * @param chatRoomsList - rooms list.
     */
    public static void setChatRoomsList(ArrayList<String> chatRoomsList) {
        Main.chatRoomsList = chatRoomsList;
    }

    /**
     * Return references to joined chat rooms tabs.
     *
     * @return joinedChatRoomsTabs.
     */
    public static HashMap<String, ListView> getJoinedChatRoomsTabs() {
        return joinedChatRoomsTabs;
    }

    /**
     * Return reference to Main Controller.
     *
     * @return mainController.
     */
    public static MainController getMainController() {
        return mainController;
    }

    /**
     * Set reference to Main Controller.
     *
     * @param mainController - reference to Main Controler.
     */
    public static void setMainController(MainController mainController) {
        Main.mainController = mainController;
    }

    /**
     * Return reference to Connect Controler.
     *
     * @return connectController.
     */
    public static ConnectController getConnectController() {
        return connectController;
    }

    /**
     * Set reference to Connect Controler.
     *
     * @param connectController - reference to Connect Controler.
     */
    public static void setConnectController(ConnectController connectController) {
        Main.connectController = connectController;
    }

    /**
     * Return users in room.
     *
     * @param room - the room from which to get list.
     * @return roomUsersList.
     */
    public static List getRoomUsersList(String room) {
        if (Main.roomUsersList.containsKey(room)) return Main.roomUsersList.get(room);
        else {
            System.out.println("List not exist! - " + room);
            return new ArrayList<String>();
        }
    }

    /**
     * Set users in room.
     *
     * @param room      - room.
     * @param roomUsers - users list.
     */
    public static void setRoomUsersList(String room, List<String> roomUsers) {
        if (roomUsersList.containsKey(room)) {
            Main.roomUsersList.remove(room);
            Main.roomUsersList.put(room, roomUsers);
        } else Main.roomUsersList.put(room, roomUsers);
    }

    /**
     * Clear roomUsersList.
     */
    public static void clearRoomUsersList() {
        roomUsersList.clear();
    }

    /**
     * Removes the list of users in a specific room.
     *
     * @param room - room.
     */
    public static void removeRoomUsersList(String room) {
        roomUsersList.remove(room);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
