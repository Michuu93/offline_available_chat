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

/**
 * Main Window Controller class.
 */
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

    /**
     * Create Connect Window and set mainController reference.
     *
     * @throws IOException
     */
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

    /**
     * Call disconnect method.
     *
     * @throws IOException
     */
    @FXML
    public void menuDisconnect() throws IOException {
        Main.getConnection().disconnect();
    }

    /**
     * Call disconnect method and exit application.
     *
     * @throws IOException
     */
    @FXML
    public void menuExit() throws IOException {
        Main.getConnection().disconnect();
        System.exit(0);
    }

    /**
     * Create About Window.
     *
     * @throws IOException
     */
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

    /**
     * Display connection status in Main Window.
     *
     * @param connectStatus - connection status.
     */
    @FXML
    public void setStatus(Connection.ConnectStatus connectStatus) {
        if (connectStatus == Connection.ConnectStatus.CONNECTED) {
            connectionStatus.setTextFill(Color.GREEN);
            connectionStatus.setText("connected");
        }
        if (connectStatus == Connection.ConnectStatus.DISCONNECTED) {
            connectionStatus.setTextFill(Color.RED);
            connectionStatus.setText("disconnected");
        }
        if (connectStatus == Connection.ConnectStatus.RECONNECTING) {
            connectionStatus.setTextFill(Color.ORANGE);
            connectionStatus.setText("reconnecting");
        }
    }

    /**
     * Show rooms list in ListView.
     */
    @FXML
    public void fillRoomsList() {
        roomsListView.setItems(FXCollections.observableList(Main.getChatRoomsList()));
    }

    /**
     * Show users list in room ListView.
     *
     * @param room - room.
     */
    @FXML
    public void fillUsersList(String room) {
        usersListView.setItems(FXCollections.observableList(Main.getRoomUsersList(room)));
        System.out.println("Fill users list in room: " + room + " list: " + FXCollections.observableList(Main.getRoomUsersList(room)));
    }

    /**
     * Clear rooms list in ListView.
     */
    @FXML
    public void clearRoomsList() {
        roomsListView.getItems().clear();
        roomsListView.refresh();
    }

    /**
     * Clear users list in ListView.
     */
    @FXML
    public void clearUsersList() {
        usersListView.getItems().clear();
        usersListView.refresh();
    }

    /**
     * Call sendClicked method when enter button is pressed.
     *
     * @param e - clicked button.
     */
    public void onEnter(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER") && !messageField.getText().isEmpty()) {
            sendClicked();
        }
    }

    /**
     * Send messages from messageField to server and clear messageField.
     * When connectStatus is DISCONNECTED - do nothing.
     * When connectStatus is CONNECTED - send message to server.
     * When connectStatus is RECONNECTING - add a message to be send later.
     */
    @FXML
    public void sendClicked() {
        if (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.DISCONNECTED) {
            System.out.println("You are not connected to the server!");
        } else {
            String roomID = tabPane.getSelectionModel().getSelectedItem().getText();
            String message = messageField.getText();
            MessagePacket msgPacket = new MessagePacket(message, roomID, Main.getUserNick());
            if (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.CONNECTED) {
                Writer.writeMessagePacket(msgPacket);
            }
            if (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.RECONNECTING) {
                Writer.addMessageToSend(msgPacket);
            }
        }
        messageField.clear();
    }

    /**
     * Call joinRoom when double-click on room name.
     *
     * @param click
     */
    public void roomClick(MouseEvent click) {
        if (click.getClickCount() == 2) {
            String currentItemSelected = (String) roomsListView.getSelectionModel().getSelectedItem();
            if ((currentItemSelected != null) && !Main.getJoinedChatRoomsTabs().containsKey(currentItemSelected)) {
                joinRoom(currentItemSelected);
            }
        }
    }

    /**
     * Join to room and create room tab.
     *
     * @param joinRoom
     */
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
        RoomPacket roomPacket = new RoomPacket(joinRoom, RoomPacket.Join.JOIN);
        Writer.writeRoomPacket(roomPacket);
    }

    /**
     * Leave from room, and close room tab.
     *
     * @param e
     */
    public void leaveRoom(Event e) {
        Tab closedTab = (Tab) e.getSource();
        String closedTabName = closedTab.getText();
        System.out.println("Leaving room " + closedTabName);
        Main.getJoinedChatRoomsTabs().remove(closedTabName);
        Main.removeRoomUsersList(closedTabName);
        RoomPacket roomPacket = new RoomPacket(closedTabName, RoomPacket.Join.UNJOIN);
        Writer.writeRoomPacket(roomPacket);
    }

    /**
     * Show users list in room ListView when changing tab.
     *
     * @param e
     */
    public void changeTab(Event e) {
        if (Main.getConnection().getConnectStatus() == Connection.ConnectStatus.CONNECTED) {
            if (!tabSwitch) {
                tabSwitch = true;
            } else {
                Tab tabSwitched = (Tab) e.getSource();
                System.out.println("Tab switched to: " + tabSwitched.getText());
                fillUsersList(tabSwitched.getText());
                tabSwitch = false;
            }
        }
    }

    /**
     * View messages in window.
     *
     * @param message - message to show.
     */
    public void viewMessage(MessagePacket message) {
        if (message.getRoom().equalsIgnoreCase("Waiting room")) {
            waitingRoomListView.getItems().add(message.getDate() + " [" + message.getNick() + "]: " + message.getMessage() + "\n");
            waitingRoomListView.scrollTo(waitingRoomListView.getItems().size() - 1);
        } else {
            ListView roomTab = Main.getJoinedChatRoomsTabs().get(message.getRoom());
            roomTab.getItems().add(message.getDate() + " [" + message.getNick() + "]: " + message.getMessage() + "\n");
            roomTab.scrollTo(roomTab.getItems().size() - 1);
        }
    }

    /**
     * Close all room tabs.
     */
    public void closeTabs() {
        int tabPaneSize = tabPane.getTabs().size();
        tabPane.getTabs().remove(1, tabPaneSize);
    }

    /**
     * Get reference to Connect Window.
     *
     * @return connectStage.
     */
    public static Stage getConnectStage() {
        return connectStage;
    }
}
