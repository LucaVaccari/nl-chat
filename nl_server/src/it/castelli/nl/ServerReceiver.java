package it.castelli.nl;

import nl.Sender;
import it.castelli.nl.messages.ServerMessageManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ServerReceiver implements Runnable
{
    public static final int RECEIVE_WINDOW = 2048;
    private boolean isRunning = true;


    public void run()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(Sender.PORT);
            byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
            DatagramPacket packet = new DatagramPacket(receiveBuffer, RECEIVE_WINDOW);
            while(isRunning)
            {
                socket.receive(packet);
                ServerMessageManager.getMessageReceiver(receiveBuffer[0]).OnReceive(receiveBuffer);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void interrupt()
    {
        isRunning = false;
    }
}
