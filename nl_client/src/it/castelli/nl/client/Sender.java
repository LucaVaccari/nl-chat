package it.castelli.nl.client;

import it.castelli.nl.messages.MessageBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class Sender
{
	private static final LinkedList<byte[]> messageQueue = new LinkedList<>();
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
				ConnectionHandler.endConnection();
				ConnectionHandler.startConnection();
			}

			byte[] messageToSend;
			if (outStream != null)
			{
				while ((messageToSend = messageQueue.peek()) != null)
				{
					/*messageToSend = messageQueue.poll();
					outStream.write(messageToSend);*/
					boolean userExists = ClientData.getInstance().getThisUser().getId() > 0;
					boolean isNewUserMessage =
							messageToSend[MessageBuilder.HEADER_SIZE] == MessageBuilder.SERVER_NEW_USER_MESSAGE_TYPE;
					if (userExists || isNewUserMessage)
					{
						messageToSend = messageQueue.poll();
						outStream.write(messageToSend);
						System.out.println("A message was sent to the server");
					}
				}

			}
		}
		catch (IOException e)
		{
			//AlertUtil.showErrorAlert("Sending error", "Cannot talk to the server", "The server cannot be reached");
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
	 *
	 * @param message The message to be sent
	 */
	public static void addMessageToQueue(byte[] message)
	{
		byte[] messageWithHeader = MessageBuilder.addHeader(message);
		messageQueue.add(messageWithHeader);
	}
}
