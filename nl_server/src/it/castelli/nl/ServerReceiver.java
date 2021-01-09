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
		try (DatagramSocket socket = new DatagramSocket(Sender.SERVER_RECEIVE_PORT))
		{
			byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
			DatagramPacket packet = new DatagramPacket(receiveBuffer, RECEIVE_WINDOW);
			while (isRunning)
			{
				System.out.println("ServerReceiver sta funzionando sulla porta: " + Sender.SERVER_RECEIVE_PORT);
				socket.receive(packet);
				System.out.println("è arrivato un messaggio");
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
