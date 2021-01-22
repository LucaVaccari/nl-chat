package it.castelli.nl.server.messages;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.UserManager;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Class for receiving a user name change
 */
public class UsernameChangeMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);

		byte userId = data[2];
		User thisUser = UserManager.getUserFromId(userId);
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String newUserName = new String(contentOfMessage).strip();

		thisUser.setName(newUserName);

		// get all users know by the sender user
		HashSet<User> knownUsers = new HashSet<>();
		for (ChatGroup group : GroupManager.getAllGroups().values())
			knownUsers.addAll(group.getUsers());

		for (User user : knownUsers)
		{
			Sender.sendToUser(data, user);
			System.out.println("Username of user " + thisUser.getName() + " queued to " + user.getName());
		}
	}
}
