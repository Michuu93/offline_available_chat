package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Connect Window Controller class.
 */
public class ConnectController implements Initializable {
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nickField;
    @FXML
    private Label connectLabel;

    /**
     * Initialize Connect Window (load recent server).
     *
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        try {
            recentLoad();
            Main.setConnectController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to the server and save to recent.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    public void connectButtonClick() throws IOException, ClassNotFoundException {
        if (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.CONNECTED) Main.getConnection().disconnect();
        String server = serverField.getText();
        Main.getConnection().setServer(server);
        Integer port = Integer.parseInt(portField.getText());
        Main.getConnection().setPort(port);
        Main.setUserNick(nickField.getText());
        Main.getConnection().connect(server, port);
    }

    /**
     * Read recent server from recentServer.txt and show.
     *
     * @throws FileNotFoundException
     */
    private void recentLoad() throws IOException {
        File recentServer = new File("recentServer.txt");
        recentServer.createNewFile();
        Scanner reader = new Scanner(recentServer);
        if (reader.hasNextLine()) serverField.setText(reader.nextLine());
        else serverField.setText("127.0.0.1");
        if (reader.hasNextLine()) portField.setText(reader.nextLine());
        else portField.setText("9001");
        if (reader.hasNextLine()) nickField.setText(reader.nextLine());
        else nickField.setText("anonymous");
        reader.close();
    }

    /**
     * Save recent server to recentServer.txt.
     */
    public void recentSave() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("recentServer.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println(serverField.getText());
        writer.println(portField.getText());
        writer.print(nickField.getText());
        writer.close();
    }

    /**
     * Set text in connectLabel.
     *
     * @param information - information text.
     */
    public void setConnectLabel(String information) {
        connectLabel.setText(information);
    }
}