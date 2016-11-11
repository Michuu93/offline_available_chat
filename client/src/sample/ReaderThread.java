package sample;

import javafx.application.Platform;
import common.MessagePacket;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ReaderThread implements Runnable {
    private ObjectInputStream reader;

    @Override
    public void run() {
        reader = Main.getConnection().getInStream();
        while (true) {
            //Read from server
            try {
                if (reader.readObject() != null) {
                    String mesg = (String) reader.readObject();
                    System.out.println("Odebrano " + mesg);
                    //MessagePacket msg = (MessagePacket) reader.readObject()
                    //System.out.println("Odebrano: ID pokoju: " + msg.getRoom() + " Wiadomość: " + msg.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Reader error!");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Reader error! Class Not Found");
            }
        }

    }
}