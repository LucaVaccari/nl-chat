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
	private boolean isRunning = true;

	/**
	 * The run() function from the Runnable interface is called when the thread starts.
	 * This one infinitely establishes connections with clients, until the end of the program.
	 */
	public void run()
	{
		try (ServerSocket welcomeSocket = new ServerSocket(GeneralData.SERVER_RECEIVE_PORT))
		{
			System.out.println("ConnectionReceiver is working on port: " + GeneralData.SERVER_RECEIVE_PORT);
			while (isRunning)
			{
				try
				{
					Socket connectionSocket = welcomeSocket.accept();
					System.out.println(
							"New connection established with " + connectionSocket.getInetAddress().getHostAddress());
					//generate thread for the receiver connection
					Connection newConnection = new Connection(connectionSocket);
					Thread connectionThread = new Thread(newConnection);
					connectionThread.start();

					NLServer.getConnectionManager().getAllConnections().add(newConnection);
					System.out.println("new connection added to connectionManager");

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
	 * Called to interrupt the thread
	 */
	public void interrupt()
	{
		System.out.println("ConnectionReceiver not working anymore");
		isRunning = false;
	}
}
