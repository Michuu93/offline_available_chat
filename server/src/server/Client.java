package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {

    /**
     * Client's nickname
     */
    private String nick;
    /**
     * Client's output stream to write.
     */
    private ObjectOutputStream outputStream;
    /**
     * Client's input stream to read.
     */
    private ObjectInputStream inputStream;


    /**
     * Default constructor.
     */
    public Client() {

    }

    /**
     * Constructor with clients input
     *
     * @param inputStream
     * @param outputStream
     */
    public Client(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
