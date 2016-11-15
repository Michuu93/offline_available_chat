package server;

import common.MessagePacket;
import java.io.*;

public class ClientService implements Runnable{

    private ObjectInputStream reader;
    private Server server = new Server();

    public ClientService (Client client){
        try{
            reader = client.getInputStream();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
            read();
    }

    private void read(){
        Object object;
        Boolean complete = true;
        while (true) {
            try {
                if (complete && (object = reader.readObject()) != null) {
                    if (object instanceof String){
                        String nickname =  (String) object;
                        server.setNickname(nickname, reader);
                        System.out.println("Odebrano nick: " + nickname);
                    }
                    if (object instanceof MessagePacket) {
                        MessagePacket messagePacket = (MessagePacket) object;
                        System.out.println("Read message from client: " + messagePacket.getRoom() + ": " + messagePacket.getMessage());
                        server.sendToAll(messagePacket);
                        server.serialize(messagePacket);
                    }
                }
            }catch (EOFException e){
                complete = false;
                server.hungUp(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
