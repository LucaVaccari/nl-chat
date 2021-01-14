package it.castelli.nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.ServerData;
import it.castelli.nl.server.UsersManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Received when a new user request to register
 */
public class NewUserMessage extends Message
{
	@Override
	public void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id,  20 bytes for the name,
		// others for the ip

		byte[] temp = Arrays.copyOfRange(data, 3, 23 - 1);
		String name = new String(temp); //reads the new User name
		byte[] byteUserIP = Arrays.copyOfRange(data, 23, 23 + 4); //reads the ip address

		try
		{
			InetAddress userIP = InetAddress.getByAddress(byteUserIP);
			byte newId = ServerData.getInstance().getLastUserId();
			ServerData.getInstance().incrementLastUserId();

			User newUser = new User(name, newId);
			UsersManager.getAllUsers().put(newId, new UsersManager.AdvancedUser(newUser));

			Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);
			Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);

			System.out.println("new User created with name: " + name + ", IP address: " + userIP.getHostAddress() +
			                   " and userId: " + newId);

			byte[] reply = MessageBuilder.buildUserIdMessage(newId);
			Sender.sendToUser(reply, newUser);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
