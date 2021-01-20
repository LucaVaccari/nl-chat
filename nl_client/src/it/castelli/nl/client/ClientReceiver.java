package it.castelli.nl.client;

import it.castelli.nl.client.graphics.AlertUtil;
import it.castelli.nl.client.message.ClientMessageManager;
import it.castelli.nl.client.message.IMessage;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Thread for receiving messages from the server
 */
public class ClientReceiver implements Runnable
{
	public static final int MAX_PACKET_LENGTH = 1024;
	private boolean isRunning = true;

	/**
	 * The run() function from the Runnable interface is called when the thread starts.
	 * This one infinitely waits for packets, until the end of the program.
	 */
	public void run()
	{
		try (Socket socket = ConnectionHandler.getSocket();
		     InputStream inStream = socket.getInputStream();)
		{
			byte[] receiveBuffer = new byte[MAX_PACKET_LENGTH];
			Sender.send();
			while (isRunning)
			{
				if (!socket.isConnected() || socket.isClosed())
				{
					Platform.runLater(() -> AlertUtil.showErrorAlert("Connection error", "Connection interrupted by " +
					                                                                     "the remote host",
					                                                 "The server is offline. Try again later."));
					interrupt();
					break;
				}

				if (inStream.available() > 0)
				{
					int messageSize = inStream.read(receiveBuffer);
					int offset = 0;
					while (offset < messageSize)
					{
						short temp = ByteBuffer.wrap(new byte[]{receiveBuffer[offset], receiveBuffer[offset + 1]}).getShort();
						int messageLength = Short.valueOf(temp).intValue();
						offset += MessageBuilder.HEADER_SIZE;
						byte[] message = Arrays.copyOfRange(receiveBuffer, offset, offset + messageLength);
						offset += messageLength;

						System.out.println("A packet has been received from " + socket.getInetAddress().getHostAddress());
						IMessage messageReceiver = ClientMessageManager.getMessageReceiver(message[0]);
						if (messageReceiver == null)
							System.out.println("Cannot find message receiver with id " + message[0]);
						else
						{
							messageReceiver.OnReceive(message);
							Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
							Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Called when the thread is interrupted
	 */
	public void interrupt()
	{
		byte[] packet = new byte[0];
		try
		{
			packet = MessageBuilder.buildServerEndConnectionMessage();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Sender.addMessageToQueue(packet);
		Sender.send();
		isRunning = false;
		System.out.println("Should be stopping");
	}
}
