import com.sun.deploy.util.SessionState;

import java.net.Socket;

/**
 * Created by Niki on 2016-11-07.
 */
public class ClientService implements Runnable{

    public ClientService (Socket socket){
        try{
            Socket clientSocket = socket;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {

    }
}
