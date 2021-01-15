package it.castelli.nl.server;

import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;

public class NLServer
{
	private static ConnectionManager connectionManager = new ConnectionManager();
	private static ConnectionReceiver connectionReceiver = new ConnectionReceiver();

	public static void main(String[] args) throws InterruptedException, IOException
	{
		boolean running = true;

		Thread connectionManagerThread = new Thread(connectionManager, "connectionManager");
		connectionManagerThread.start();

		Thread serverThread = new Thread(connectionReceiver, "serverThread");
		serverThread.start();

		try
		{
			byte[] packet = MessageBuilder.buildClientTestMessage("Test");
			//Sender.send(packet, InetAddress.getLocalHost());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		while (running)
		{

		}

		Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);
		Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);

		connectionReceiver.interrupt();
		connectionManager.interrupt();
		try
		{
			connectionManagerThread.join();
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static ConnectionManager getConnectionManager() {
		return connectionManager;
	}
}
