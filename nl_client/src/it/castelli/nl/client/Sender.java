package it.castelli.nl.client;

import it.castelli.nl.client.graphics.AlertUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class Sender
{
	private static OutputStream outStream;
	private static final LinkedList<byte[]> messageQueue = new LinkedList<>();

	private Sender() {}

	/**
	 * Empty the queue of messages, sending each of theme
	 */
	public static void send()
	{
		try
		{
			if (outStream == null)
			{
				System.out.println("OutputStream to the server is null");
				ConnectionHandler.endConnection();
				ConnectionHandler.startConnection();
			}

			byte[] messageToSend;
			while ((messageToSend = messageQueue.poll()) != null)
			{
				if (outStream != null)
					outStream.write(messageToSend);
				else
					System.out.println("out stream is null");
			}

		}
		catch (IOException e)
		{
//			e.printStackTrace();
			AlertUtil.showErrorAlert("Sending error", "Cannot talk to the server", "The server cannot be reached");
		}
	}

	/**
	 * Setter for the OutputStream of the class
	 *
	 * @param outStream The new OutputStream
	 */
	public static void setOutStream(OutputStream outStream)
	{
		Sender.outStream = outStream;
	}

	/**
	 * Add a message to the queue of messages. The queued messages will be sent with send()
	 * @param data The message to be sent
	 */
	public static void addMessageToQueue(byte[] data)
	{
		messageQueue.add(data);
	}
}
