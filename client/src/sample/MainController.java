package sample;

import common.MessagePacket;
import common.RoomPacket;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private static Stage connectStage;
    private static Boolean tabSwitch = false;
    @FXML
    private Label connectionStatus;
    @FXML
    private TextField messageField;
    @FXML
    private ListView roomsListView;
    @FXML
    private ListView usersListView;
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView waitingRoomListView;

    @FXML
    public void menuConnect() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ConnectScreen.fxml"));
        connectStage = new Stage();
        connectStage.setTitle("Connect");
        connectStage.setScene(new Scene(root, 400, 80));
        connectStage.setAlwaysOnTop(true);
        connectStage.setResizable(false);
        connectStage.show();

        //tutaj przekazanie kontrolera, chociaż mogłoby być w jakiejś metodzie typu initialize, w momencie tworzenia głónego okna, ale wysyłało null
        Main.setMainController(this);
    }

    @FXML
    public void menuDisconnect() throws IOException {
        Main.getConnection().disconnect();
    }

    @FXML
    public void menuExit() throws IOException {
        Main.getConnection().disconnect();
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
        } else {
            connectionStatus.setTextFill(Color.RED);
            connectionStatus.setText("disconnected");
        }
    }

    @FXML
    public void fillRoomsList() {
        roomsListView.setItems(FXCollections.observableList(Main.getChatRoomsList()));
    }

    public void fillUsersList(String room) {
        if (Main.getRoomUsersList(room) != null)
            usersListView.setItems(FXCollections.observableList(Main.getRoomUsersList(room)));
        else {
            usersListView.getItems().clear();
            usersListView.refresh();
        }
    }

    @FXML
    public void clearRoomsList() {
        roomsListView.getItems().clear();
        roomsListView.refresh();
    }

    @FXML
    public void clearUsersList() {
        usersListView.getItems().clear();
        usersListView.refresh();
    }

    @FXML
    public void onEnter(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER") && !messageField.getText().isEmpty()) {
            sendClicked();
        }
    }

    @FXML
    public void sendClicked() {
        if (Main.getConnection().isConnected()) {
            String roomID = tabPane.getSelectionModel().getSelectedItem().getText();
            String message = messageField.getText();

            MessagePacket msgPacket = new MessagePacket(message, roomID, Main.getUserNick());
            Writer.writeMessagePacket(msgPacket);
            messageField.clear();
        } else {
            System.out.println("Nie można wysłać wiadomośći, nie jesteś połączony!");
            messageField.clear();
        }

    }

    public void roomClick(MouseEvent click) {
        if (click.getClickCount() == 2) {
            String currentItemSelected = (String) roomsListView.getSelectionModel().getSelectedItem();
            if ((currentItemSelected != null) && !Main.getJoinedChatRoomsTabs().containsKey(currentItemSelected)) {
                joinRoom(currentItemSelected);
            }
        }
    }

    public void joinRoom(String joinRoom) {
        Tab newTab = new Tab();
        newTab.setText(joinRoom);
        newTab.setOnSelectionChanged(event -> changeTab(event));
        newTab.setOnClosed(event -> leaveRoom(event));
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);

        ListView newListView = new ListView();
        newTab.setContent(newListView);

        Main.getJoinedChatRoomsTabs().put(joinRoom, newListView);

        //send join to server
        RoomPacket roomPacket = new RoomPacket(joinRoom, RoomPacket.Join.JOIN);
        Writer.writeRoomPacket(roomPacket);
    }

    public void leaveRoom(Event e) {
        Tab closedTab = (Tab) e.getSource();
        String closedTabName = closedTab.getText();
        System.out.println("Leaving room " + closedTabName);

        Main.getJoinedChatRoomsTabs().remove(closedTabName);
        Main.removeRoomUsersList(closedTabName);

        //send unjoin to server
        RoomPacket roomPacket = new RoomPacket(closedTabName, RoomPacket.Join.UNJOIN);
        Writer.writeRoomPacket(roomPacket);
    }

    public void changeTab(Event e) {
        if (Main.getConnection().isConnected()) {
            if (!tabSwitch) {
                tabSwitch = true;
            } else {
                Tab tabSwitched = (Tab) e.getSource();
                //clearUsersList();
                fillUsersList(tabSwitched.getText());
                tabSwitch = false;
                System.out.println("Tab switched to: " + tabSwitched.getText());
            }
        }
    }

    public void viewMessage(MessagePacket message) {
        if (message.getRoom().equalsIgnoreCase("Waiting room")) { //view message in chat room tab
            waitingRoomListView.getItems().add(message.getDate() + " [" + message.getNick() + "]: " + message.getMessage() + "\n");
            waitingRoomListView.scrollTo(waitingRoomListView.getItems().size() - 1);

        } else { //view message in other tabs
            ListView roomTab = Main.getJoinedChatRoomsTabs().get(message.getRoom());
            roomTab.getItems().add(message.getDate() + " [" + message.getNick() + "]: " + message.getMessage() + "\n");
            roomTab.scrollTo(roomTab.getItems().size() - 1);
        }
    }

    public void closeTabs() {
        int tabPaneSize = tabPane.getTabs().size();
        tabPane.getTabs().remove(1, tabPaneSize);
    }

    public static Stage getConnectStage() {
        return connectStage;
    }
}
