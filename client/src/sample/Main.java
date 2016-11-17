package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends Application {
    private static Connection connection = new Connection();
    private static String userNick;
    private static ArrayList<String> chatRoomsList = new ArrayList<String>();
    private static HashMap<String, TextArea> joinedChatRoomsTabs = new HashMap<>();
    private static HashMap<String, ArrayList> roomUsersList = new HashMap<>();
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

    public static HashMap<String, TextArea> getJoinedChatRoomsTabs() {
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

    public static ArrayList getRoomUsersList(String room) {
        if (Main.roomUsersList.containsKey(room)) return Main.roomUsersList.get(room);
        else {
            ArrayList<String> x = new ArrayList<>(Arrays.asList("xyz", "abc"));
            return x;
        }
    }

    public static void setRoomUsersList(String room, ArrayList roomUsers) {
        Main.roomUsersList.replace(room, roomUsers);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
