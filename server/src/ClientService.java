import com.sun.deploy.util.SessionState;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class ClientService implements Runnable{

    private ObjectInputStream inStream;

    public ClientService (Socket socket){
        try{
            Socket clientSocket = socket;
            inStream = new ObjectInputStream(clientSocket.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        reading();
    }

    private void reading(){
        Object message = null;
        try{
            while ((message = inStream.readObject()) != null){
                sendToEveryone(message);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sendToEveryone(Object message) {
        Server server = new Server();
        Iterator iterator = server.outputStreams.iterator();
        while (iterator.hasNext()){
            try{
                ObjectOutputStream outStream = (ObjectOutputStream)iterator.next();
                outStream.writeObject(message);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }


}
