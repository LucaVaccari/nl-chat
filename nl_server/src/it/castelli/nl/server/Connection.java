package it.castelli.nl.server;

import it.castelli.nl.server.messages.MessageManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Represents a connection with a client. Constantly waits for messages from it.
 */
public class Connection implements Runnable
{
	public static final int RECEIVE_WINDOW = 2048;
	private final Socket connectionSocket;
	private UserManager.AdvancedUser user;
	private boolean isRunning = true;

	/**
	 * Constructor for the connection object
	 *
	 * @param socket The socket on which the client is connected
	 */
	public Connection(Socket socket)
	{
		this.connectionSocket = socket;
	}

	@Override
	public void run()
	{
		try (InputStream in = connectionSocket.getInputStream())
		{
			byte[] data = new byte[RECEIVE_WINDOW];
			while (isRunning)
			{
				if(in.read(data) > 0)
				{
					MessageManager.getMessageReceiver(data[0]).onReceive(data, this);
					data = new byte[RECEIVE_WINDOW];
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("The connection has been interrupted");
		}
	}

	/**
	 * Getter for the client's user
	 *
	 * @return The client's user
	 */
	public UserManager.AdvancedUser getAdvancedUser()
	{
		return user;
	}

	/**
	 * Setter for the client's user
	 *
	 * @param user The client's user
	 */
	public void setUser(UserManager.AdvancedUser user)
	{
		this.user = user;
	}

	public void interrupt()
	{
		isRunning = false;
		try
		{
			connectionSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Socket getSocket()
	{
		return connectionSocket;
	}
}
