package it.castelli.nl;

import it.castelli.nl.message.ClientMessageManager;
import nl.Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Thread for receiving messages from the server
 */
public class ClientReceiver implements Runnable
{
    public static final int RECEIVE_WINDOW = 2048;
    private boolean isRunning = true;

    /**
     * The run() function from the Runnable interface is called when the thread starts
     */
    public void run()
    {
        try (DatagramSocket socket = new DatagramSocket(Sender.CLIENT_RECEIVE_PORT))
        {
            byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
            DatagramPacket packet = new DatagramPacket(receiveBuffer, RECEIVE_WINDOW);
            while(isRunning)
            {
                socket.receive(packet);
                ClientMessageManager.getMessageReceiver(receiveBuffer[0]).OnReceive(receiveBuffer);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Called when the thread is interrupted (not sure)
     */
    public void interrupt()
    {
        isRunning = false;
    }
}
