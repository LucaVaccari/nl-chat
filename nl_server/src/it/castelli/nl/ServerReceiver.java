package it.castelli.nl;

import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.messages.ServerMessageManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Thread for receiving messages from the client
 */
public class ServerReceiver implements Runnable
{
	public static final int RECEIVE_WINDOW = 2048;
	private boolean isRunning = true;


	/**
	 * The run() function from the Runnable interface is called when the thread starts.
	 * This one infinitely waits for packets, until the end of the program.
	 */
	public void run()
	{
		try (DatagramSocket socket = new DatagramSocket(Sender.SERVER_RECEIVE_PORT))
		{
			byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
			DatagramPacket packet = new DatagramPacket(receiveBuffer, RECEIVE_WINDOW);
			while (isRunning)
			{
				System.out.println("ServerReceiver is working on port: " + Sender.SERVER_RECEIVE_PORT);
				socket.receive(packet);
				System.out.println("A message arrived");
				ServerMessageManager.getMessageReceiver(receiveBuffer[0]).OnReceive(receiveBuffer);
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
		System.out.println("ServerReceiver is not working anymore");
		isRunning = false;
	}
}
