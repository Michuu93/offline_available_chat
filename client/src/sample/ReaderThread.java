package sample;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

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

            //Read from server test
            try {
                if (Main.getConnection().isConnected()) { //open reader when connected
                    String message;
                    while ((message = Main.getConnection().getReader().readLine()) != null)
                        System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("BufferedReader error!");
            }

            //Status update
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
//            MainController controller = loader.getController();
//            if (controller != null)
//                controller.setStatus(Main.getConnection().isConnected());
//            System.out.println(controller);

        }
    }


}