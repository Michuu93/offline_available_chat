import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Niki on 2016-11-07.
 */
public class Server{

    ArrayList<ObjectOutputStream> outputStreams;

    private void connecting(){
        try {
            ServerSocket serverSocket = new ServerSocket(9001);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream stream = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStreams.add(stream);
                Thread t = new Thread(new ClientService(clientSocket));
                t.start();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void reading(Socket clientSocket){

    }

    private void sendToEveryone(){

    }
}
