package it.castelli.nl.server;

import it.castelli.nl.User;
import it.castelli.nl.server.messages.MessageManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements Runnable {

    public static final int RECEIVE_WINDOW = 2048;
    private Socket connectionSocket;
    private InetAddress IPAddress;
    private User user;


    public Connection(Socket socket)
    {
        this.connectionSocket = socket;
        InetSocketAddress ClientAddress = (InetSocketAddress) connectionSocket.getRemoteSocketAddress();
        IPAddress = ClientAddress.getAddress();
    }

    @Override
    public void run() {

        try(InputStream in = connectionSocket.getInputStream())
        {
            byte[] data = new byte[RECEIVE_WINDOW];
            while(true)
            {
                in.read(data);
                MessageManager.getMessageReceiver(data[0]).OnReceive(data, this);
            }
        }
        catch (IOException e)
        {
            System.out.println("The connection ended");
            e.printStackTrace();
        }
    }
}
