package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReaderThread implements Runnable {
    @Override
    public void run() {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
//        try {
//            Parent root = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MainController controller = loader.getController();
//        if (controller != null)
//            controller.setStatus(Main.getConnection().isConnected());
//        System.out.println(controller);

        while (true) {

            //Sleep do testów
            try {
                Main.getReaderThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //

            System.out.println("Connected = " + Main.getConnection().isConnected());

            if (Main.getConnection().isConnected()) {

                //test
                MessagePacket msg = new MessagePacket();
                msg.setRoom("Waiting room");
                msg.setMessage("Wiadomość testowa");

//                //Send to server
//                ObjectOutputStream outStream = Main.getConnection().getOutStream();
//                try {
//                    outStream.writeObject(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //Read from server
//                ObjectInputStream inStream = Main.getConnection().getInStream();
//                try {
//                    msg = (MessagePacket) inStream.readObject();
//                    System.out.println("ID pokoju: " + msg.getRoom() + " Wiadomość: " + msg.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }

                //Status update


            }
        }

    }
}