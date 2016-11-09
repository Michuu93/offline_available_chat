package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ConnectController implements Initializable {
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nickField;
    @FXML
    private Label serverLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Label nickLabel;

    public String server;
    public int port;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            recentLoad();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void connectButtonClick() throws IOException {
        //TODO: zrobic sprawdzanie wypełnienia pól
        server = serverField.getText();
        if (server.isEmpty()) {
            serverLabel.setTextFill(Color.RED);
        } else
        port = Integer.parseInt(portField.getText());
        if (port < 1024 | port > 65535) {
            portLabel.setTextFill(Color.RED);
        } else
        Main.setUserNick(nickField.getText());
        if (Main.getUserNick() == null) {
            nickLabel.setTextFill(Color.RED);
        } else
        System.out.println("Server: " + server + " Port: " + port + " Nick: " + Main.getUserNick());
        Main.getConnection().connect(server, port);

        if (Main.getConnection().isConnected()) {
            recentSave();
            MainController.getConnectStage().close();
        } else {
            serverLabel.setTextFill(Color.RED);
            portLabel.setTextFill(Color.RED);
            System.out.println("Server IP or Port error!");
        }
    }

    private void recentLoad() throws FileNotFoundException {
        File recentServer = new File("recentServer.txt");
        Scanner reader = new Scanner(recentServer);
        if (reader.hasNextLine()) serverField.setText(reader.nextLine());
        else serverField.setText("127.0.0.1");
        if (reader.hasNextLine()) portField.setText(reader.nextLine());
        else portField.setText("666");
        if (reader.hasNextLine()) nickField.setText(reader.nextLine());
        else nickField.setText("anonymous");
        reader.close();
    }

    private void recentSave() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("recentServer.txt");
        writer.println(serverField.getText());
        writer.println(portField.getText());
        writer.print(nickField.getText());
        writer.close();
    }

}