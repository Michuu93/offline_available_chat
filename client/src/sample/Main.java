package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public static Connection getConnection() {
        return connection;
    }

    public static String getUserNick() {
        return userNick;
    }

    public static void setUserNick(String userNick) {
        Main.userNick = userNick;
    }

    public static ArrayList<String> getChatRoomsList() {
        return chatRoomsList;
    }

    public static void setChatRoomsList(ArrayList<String> chatRoomsList) {
        Main.chatRoomsList = chatRoomsList;
    }

    public static HashMap<String, ListView> getJoinedChatRoomsTabs() {
        return joinedChatRoomsTabs;
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void setMainController(MainController mainController) {
        Main.mainController = mainController;
    }

    public static ConnectController getConnectController() {
        return connectController;
    }

    public static void setConnectController(ConnectController connectController) {
        Main.connectController = connectController;
    }

    public static List getRoomUsersList(String room) {
        if (Main.roomUsersList.containsKey(room)) return Main.roomUsersList.get(room);
        else {
            System.out.println("List not exist! - " + room);
            return null;
        }
    }

    public static void setRoomUsersList(String room, List<String> roomUsers) {
        if (roomUsersList.containsKey(room)) Main.roomUsersList.replace(room, roomUsers);
        else Main.roomUsersList.put(room, roomUsers);
    }

    public static void clearRoomUsersList(){
        roomUsersList.clear();
    }

    public static void removeRoomUsersList(String room){
        roomUsersList.remove(room);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
