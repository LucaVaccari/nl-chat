package it.castelli.nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import it.castelli.nl.server.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Received when a new user request to register
 */
public class NewUserMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id,  20 bytes for the name,
		// others for the ip

		byte[] temp = Arrays.copyOfRange(data, 3, 23 - 1);
		String name = new String(temp); //reads the new User name

		byte newId = ServerData.getInstance().getLastUserId();
		ServerData.getInstance().incrementLastUserId();

		User newUser = new User(name, newId);

		UserManager.AdvancedUser newAdvancedUser = new UserManager.AdvancedUser(newUser);
		UserManager.getAllUsers().put(newId, newAdvancedUser);

		System.out.println("new User created with name: " + name + " and userId: " + newId);

		if (connection.getAdvancedUser() == null)
			connection.setUser(newAdvancedUser);
		System.out.println("User with id " + newAdvancedUser.getUser().getId() + " added to the connection");

		byte[] reply = MessageBuilder.buildUserIdMessage(newId);
		System.out.println("Created UserIdMessage fromNewUserMessage in the onReceive method");
		Sender.sendToUser(reply, newUser);
	}
}
