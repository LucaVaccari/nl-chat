package it.castelli.nl.server;

import it.castelli.nl.GeneralData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread for receiving messages from the client
 */
public class ConnectionReceiver implements Runnable
{
	public static final int RECEIVE_WINDOW = 2048;
	private boolean isRunning = true;

	/**
	 * The run() function from the Runnable interface is called when the thread starts.
	 * This one infinitely establishes connections with clients, until the end of the program.
	 */
	public void run()
	{
		try (ServerSocket welcomeSocket = new ServerSocket(GeneralData.SERVER_RECEIVE_PORT))
		{
			while (isRunning)
			{
				try (Socket connectionSocket = welcomeSocket.accept())
				{
					//generate thread for the receiver connection
					Thread connectionThread = new Thread(new Connection(connectionSocket));
					connectionThread.start();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
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
