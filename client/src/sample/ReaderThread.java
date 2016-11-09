package sample;

import sun.plugin2.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReaderThread implements Runnable {
    @Override
    public void run() {
        while (true) {

            //Sleep
            try {
                Main.getReaderThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Connected = " + Main.getConnection().isConnected());

            MessagePacket msg = new MessagePacket();
            msg.setRoom("Waiting room");
            msg.setMessage("Wiadomość testowa");

            if (Main.getConnection().isConnected()) {
                //Send to server
                ObjectOutputStream outStream = Main.getConnection().getOutStream();
                try {
                    outStream.writeObject(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Read from server
                ObjectInputStream inStream = Main.getConnection().getInStream();
                try {
                    msg = (MessagePacket) inStream.readObject();
                    System.out.println("ID pokoju: " + msg.getRoom() + " Wiadomość: " + msg.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //Read from server test
//            try {
//                if (Main.getConnection().isConnected()) { //open reader when connected
//                    String message;
//
//                    while ((message = Main.getConnection().getReader().readLine()) != null)
//                        System.out.println(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("BufferedReader error!");
//            }

            //Status update
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
//            MainController controller = loader.getController();
//            if (controller != null)
//                controller.setStatus(Main.getConnection().isConnected());
//            System.out.println(controller);

        }
    }


}