package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Label connectionStatus;

    @FXML
    public void menuConnect() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ConnectScreen.fxml"));
        Stage connectStage = new Stage();
        connectStage.setTitle("Connect");
        connectStage.setScene(new Scene(root, 300, 70));
        connectStage.show();
        Connect.loadConfig();
    }

    @FXML
    public void menuDisconnect(){
        Connect.disconnect();
    }

    @FXML
    public void menuExit(){
        System.exit(0);
    }

    @FXML
    public void menuAbout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutScreen.fxml"));
        Stage aboutStage = new Stage();
        aboutStage.setTitle("About");
        aboutStage.setScene(new Scene(root, 300, 150));
        aboutStage.show();
    }

    public void setStatus(boolean isConnected){
        if (isConnected){
            connectionStatus.setTextFill(Color.GREEN);
            connectionStatus.setText("connected");
        }
        else{
            connectionStatus.setTextFill(Color.RED);
            connectionStatus.setText("disconnected");
        }
    }
}
