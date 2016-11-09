package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Connection connection;
    private static Thread readerThread;
    private static String userNick;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setResizable(false);
        primaryStage.show();

        connection = new Connection();
        Runnable threadJob = new ReaderThread();
        readerThread = new Thread(threadJob);
        readerThread.start();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Thread getReaderThread() {
        return readerThread;
    }

    public static String getUserNick() {
        return userNick;
    }

    public static void setUserNick(String userNick) {
        Main.userNick = userNick;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
