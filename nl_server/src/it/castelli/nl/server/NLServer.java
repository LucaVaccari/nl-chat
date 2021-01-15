package it.castelli.nl.server;

import it.castelli.nl.serialization.Serializer;

import java.io.IOException;

public class NLServer
{
	private static ConnectionManager connectionManager = new ConnectionManager();
	private static ConnectionReceiver connectionReceiver = new ConnectionReceiver();

	public static void main(String[] args) throws InterruptedException, IOException
	{
		boolean running = true;

		GroupManager.init();
		UserManager.init();

		Thread connectionManagerThread = new Thread(connectionManager, "connectionManager");
		connectionManagerThread.start();

		Thread serverThread = new Thread(connectionReceiver, "serverThread");
		serverThread.start();

		while (running)
		{

		}

		Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(UserManager.getAllUsers(), UserManager.USERS_FILE_PATH);
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
