package nl.server;

import it.castelli.nl.User;

import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

public class AdvancedUser extends User {

    private final LinkedList<byte[]> incomingMessages;
    private Socket connection;

    public AdvancedUser(String name, InetAddress ipAddress, byte id) {
        super(name, ipAddress, id);
        incomingMessages = new LinkedList<>();
    }

    public LinkedList<byte[]> getIncomingMessages() {
        return incomingMessages;
    }

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }
}
