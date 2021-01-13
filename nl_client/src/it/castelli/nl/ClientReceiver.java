package it.castelli.nl;

import it.castelli.nl.message.ClientMessageManager;

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
     * The run() function from the Runnable interface is called when the thread starts.
     * This one infinitely waits for packets, until the end of the program.
     */
    public void run()
    {
        try (DatagramSocket socket = new DatagramSocket(Sender.CLIENT_RECEIVE_PORT))
        {
            byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
            DatagramPacket packet = new DatagramPacket(receiveBuffer, RECEIVE_WINDOW);
            while(isRunning)
            {
                System.out.println("ClientReceiver is working on port: " + Sender.CLIENT_RECEIVE_PORT);
                socket.receive(packet);
                System.out.println("A packet has been received from " + packet.getAddress().getHostAddress());
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
