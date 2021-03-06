package it.castelli.nl.client;

import it.castelli.nl.GeneralData;
import it.castelli.nl.client.graphics.AlertUtil;
import it.castelli.nl.messages.MessageBuilder;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler
{
	private static Socket socket;
	private static ClientReceiver receiver;

	/**
	 * Tries to start the connection with the server by creating a new Socket and a ClientReceiver thread.
	 * If it succeeds it updates the Sender output stream to the server.
	 * It gives an error messages if the connection fails.
	 */
	public static void startConnection()
	{
		try
		{

			Platform.runLater(() -> {
				NLClient.getPrimaryStage().setTitle("nl-chat | connecting");
			});
			socket = new Socket(ClientData.getInstance().getServerAddress(), GeneralData.SERVER_RECEIVE_PORT);
			receiver = new ClientReceiver();
			new Thread(receiver, "Client receiver").start();
			System.out.println("Connection established with the server");
			Sender.setOutStream(socket.getOutputStream());

			if (!socket.isClosed() && socket.isConnected())
			{
				Platform.runLater(() ->
						NLClient.getPrimaryStage().setTitle("nl-chat | connected with " + socket.getInetAddress().getHostAddress()));

				byte[] data;
				if (ClientData.getInstance().getThisUser().getId() > 0)
				{
					data = MessageBuilder.buildServerTestMessage("Start communications", ClientData.getInstance().getThisUser().getId());
					Sender.addMessageToQueue(data);
					Sender.send();
				}
			}
		}
		catch (IOException e)
		{

			Platform.runLater(() ->
			{
				NLClient.getPrimaryStage().setTitle("nl-chat | server offline");
				AlertUtil.showErrorAlert("Connection error", "Cannot connect to the server",
						"The server is offline or unreachable. Try setting your" +
								" " +
								"server address in the settings menu.");
			});
		}
	}

	public static void endConnection()
	{
		try
		{
			byte[] data = MessageBuilder.buildServerEndConnectionMessage();
			Sender.addMessageToQueue(data);
			Sender.send();

			if (receiver != null)
				receiver.interrupt();

			if (socket != null)
				socket.close();

			NLClient.getPrimaryStage().setTitle("nl-chat | server offline");
			System.out.println("Connection was ended by the client");
			Sender.setOutStream(null);
			socket = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isConnected()
	{
		if (socket == null)
			return false;

		return !socket.isClosed() && socket.isConnected();
	}

	public static Socket getSocket()
	{
		return socket;
	}
}
