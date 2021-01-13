package nl.server;

import nl.server.messages.ServerMessageManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable {

    public static final int RECEIVE_WINDOW = 2048;
    private Socket connectionSocket;
    private AdvancedUser user;

    public Connection(Socket socket)
    {
        this.connectionSocket = socket;
    }

    @Override
    public void run() {

        try(InputStream in = connectionSocket.getInputStream();
            OutputStream out = connectionSocket.getOutputStream())
        {
            byte[] data = new byte[RECEIVE_WINDOW];
            while(true)
            {
                in.read(data);
                ServerMessageManager.getMessageReceiver(data[0]).OnReceive(data);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
