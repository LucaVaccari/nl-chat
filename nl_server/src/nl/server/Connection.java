package nl.server;

import nl.server.messages.MessageManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Connection implements Runnable {

    public static final int RECEIVE_WINDOW = 2048;
    private Socket connectionSocket;

    public Connection(Socket socket)
    {
        this.connectionSocket = socket;
    }

    @Override
    public void run() {

        try(InputStream in = connectionSocket.getInputStream())
        {
            byte[] data = new byte[RECEIVE_WINDOW];
            while(true)
            {
                in.read(data);
                MessageManager.getMessageReceiver(data[0]).OnReceive(data);
            }
        }
        catch (IOException e)
        {
            System.out.println("The connection ended");
            e.printStackTrace();
        }
    }
}
