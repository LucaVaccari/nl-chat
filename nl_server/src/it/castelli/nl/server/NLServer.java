package it.castelli.nl.server;

import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.util.Scanner;

public class NLServer
{
	private static final ConnectionManager connectionManager = new ConnectionManager();
	private static final ConnectionReceiver connectionReceiver = new ConnectionReceiver();

	public static void main(String[] args) throws IOException
	{
		boolean running = true;

		GroupManager.init();
		UserManager.init();

		Thread connectionManagerThread = new Thread(connectionManager, "connectionManager");
		connectionManagerThread.start();

		Thread serverThread = new Thread(connectionReceiver, "serverThread");
		serverThread.start();

		Scanner scanner = new Scanner(System.in);

		while (running)
		{
			if (scanner.next().equals("stop"))
				running = false;
		}

		Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(UserManager.getAllUsers(), UserManager.USERS_FILE_PATH);
		Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);

		connectionReceiver.interrupt();
		connectionManager.interrupt();

		System.out.println("Closing the server...");
		System.exit(0);
	}

	public static ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}
}
