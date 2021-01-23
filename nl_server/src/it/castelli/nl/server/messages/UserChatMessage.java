package it.castelli.nl.server.messages;


import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.UserManager;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class which handles receiving user chat messages on the server.
 * It forwards the message to all of the users of a group
 */
public class UserChatMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String textMessage = new String(contentOfMessage).strip();
		ChatGroup thisGroup = GroupManager.getGroupFromCode(groupCode);
		User thisUser = UserManager.getUserFromId(userId);

		System.out.println("a user message has arrived from user: " + thisUser.getName() + " and id: " + userId +
				" in the group with code: " + groupCode);

		if (thisGroup.getUsers().contains(thisUser))
		{
			//send ClientUserChatMessage
			try
			{
				byte[] reply = MessageBuilder
						.buildClientUserChatMessage(new ChatGroupMessage(thisUser, thisGroup, textMessage));
				System.out.println("Created ClientUserChatMessage from UserChatMessage in the onReceive method");
				Sender.sendToGroup(reply, thisGroup);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			System.out.println("The user with id: " + thisUser.getId() + " and name: " + thisUser.getName() +
					" is not part of group " + thisGroup.getCode());
			System.out.println("The participants are: ");
			for (User user : thisGroup.getUsers())
			{
				System.out.println("user with id: " + user.getId() + " and name: " + user.getName());
			}
		}
	}
}
