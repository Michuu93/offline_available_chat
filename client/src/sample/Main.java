package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private static Connection connection = new Connection();
    private static String userNick;
    private static ArrayList<String> chatRoomsList = new ArrayList<String>();
    private static ArrayList<String> joinedChatRoomsList = new ArrayList<String>();
    private static MainController mainController;
    private static ConnectController connectController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root, 800, 500));
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

    public static ArrayList<String> getJoinedChatRoomsList() {
        return joinedChatRoomsList;
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

    public static void main(String[] args) {
        launch(args);
    }

}
