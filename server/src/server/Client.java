package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Represents chat client.
 */
public class Client {

    /**
     * Client's nickname.
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
     * Constructor with with the given clients input stream and output stream.
     *
     * @param inputStream
     * @param outputStream
     */
    public Client(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    /**
     * Gets the clients stream to write.
     *
     * @return output stream
     */
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Gets the clients stream to read.
     *
     * @return input stream
     */
    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    /**
     * Gets clients nickname.
     *
     * @return nickname
     */
    public String getNick() {
        return nick;
    }

    /**
     * Changes the nickname of client.
     *
     * @param nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }
}
