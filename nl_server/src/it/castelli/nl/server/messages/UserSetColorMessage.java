package it.castelli.nl.server.messages;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.graphics.RGBColor;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.UserManager;

import java.util.Arrays;
import java.util.HashSet;

public class UserSetColorMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);

		byte userId = data[2];
		User thisUser = UserManager.getUserFromId(userId);
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		RGBColor rgbColor = new RGBColor(contentOfMessage[0], contentOfMessage[1], contentOfMessage[2]);

		thisUser.setColor(rgbColor);

		// get all users know by the sender user
		HashSet<User> knownUsers = new HashSet<>();
		for (ChatGroup group : GroupManager.getAllGroups().values())
			knownUsers.addAll(group.getUsers());

		for (User user : knownUsers)
		{
			Sender.sendToUser(data, user);
			System.out.println("Color of user " + thisUser.getName() + " queued to " + user.getName());
		}
	}
}
