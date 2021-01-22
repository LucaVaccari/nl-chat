package it.castelli.nl.client;

import it.castelli.nl.messages.MessageBuilder;

import java.io.IOException;
import java.io.OutputStream;

public class Sender
{
	private static OutputStream outStream;

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
				ConnectionHandler.startConnection();
			}

			byte[] messageToSend;
			if (outStream != null)
			{
				while ((messageToSend = ClientData.getInstance().getMessageQueue().peek()) != null)
				{
					messageToSend = ClientData.getInstance().getMessageQueue().poll();
					outStream.write(messageToSend);
					System.out.println("Message sent to the server");
				}
			}
		}
		catch (IOException e)
		{
			/*Platform.runLater(() -> {
				AlertUtil.showErrorAlert("Sending error", "Cannot talk to the server", "The server cannot be reached, trying to connect...");
			});*/
			ConnectionHandler.startConnection();
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
	 * @param message The message to be sent
	 */
	public static void addMessageToQueue(byte[] message)
	{
		byte[] messageWithHeader = MessageBuilder.addHeader(message);
		ClientData.getInstance().getMessageQueue().add(messageWithHeader);
	}
}
