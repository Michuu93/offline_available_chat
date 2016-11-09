package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private static Stage connectStage;
    @FXML
    private Label connectionStatus;
    @FXML
    private TextField messageField;

    @FXML
    public void menuConnect() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ConnectScreen.fxml"));
        connectStage = new Stage();
        connectStage.setTitle("Connect");
        connectStage.setScene(new Scene(root, 400, 70));
        connectStage.setAlwaysOnTop(true);
        connectStage.setResizable(false);
        connectStage.show();
    }

    @FXML
    public void menuDisconnect() throws IOException {
        Main.getConnection().disconnect();
    }

    @FXML
    public void menuExit() {
        System.exit(0);
    }

    @FXML
    public void menuAbout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutScreen.fxml"));
        Stage aboutStage = new Stage();
        aboutStage.setTitle("About");
        aboutStage.setScene(new Scene(root, 300, 150));
        aboutStage.setAlwaysOnTop(true);
        aboutStage.setResizable(false);
        aboutStage.show();
    }

    @FXML
    public void setStatus(boolean isConnected) {
        if (isConnected) {
            connectionStatus.setTextFill(Color.GREEN);
            connectionStatus.setText("connected");
            System.out.println("Set Status Connected");
        } else {
            connectionStatus.setTextFill(Color.RED);
            connectionStatus.setText("disconnected");
            System.out.println("Set Status Disconnected");
        }
    }

    @FXML
    public void sendClicked() {
        messageField.clear();
    }

    public static Stage getConnectStage() {
        return connectStage;
    }
}
