package it.castelli.nl.server;

import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;

public class NLServer
{
	public static void main(String[] args) throws InterruptedException, IOException
	{
		boolean running = true;

		Thread serverThread = new Thread(new ConnectionReceiver(), "serverThread");
		serverThread.start();

		try
		{
			byte[] packet = MessageBuilder.buildClientTestMessage("Test");
			//GeneralData.sendToClient(packet, InetAddress.getLocalHost());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		while (running)
		{
			//send all the messages in the users queues

		}

		Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);
		Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);


		serverThread.interrupt();
		try
		{
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
