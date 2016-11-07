package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class ConnectController {
    @FXML
    public void connectServer() throws IOException {
        boolean isConnected = Connect.connect();
        //MainController.setStatus(isConnected);
        //nie wiem jak zmienić label ze sceny MainScreen
    }
}