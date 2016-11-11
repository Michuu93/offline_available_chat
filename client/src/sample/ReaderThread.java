package sample;

import javafx.application.Platform;
import static java.lang.Thread.sleep;
import common.MessagePacket;

public class ReaderThread implements Runnable {
    @Override
    public void run() {
        while (true) {
            //Sleep do testów
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //

            System.out.println("Connected = " + Main.getConnection().isConnected());
            System.out.println(Main.getMainController());
            Platform.runLater(() -> Main.getMainController().setStatus(Main.getConnection().isConnected()));


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