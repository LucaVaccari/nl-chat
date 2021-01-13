package it.castelli.nl.message;

import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.ClientGroupManager;
import it.castelli.nl.ChatGroup;

import it.castelli.nl.User;
import javafx.application.Platform;

import java.util.Arrays;

/**
 * New user chat message sent by a user on a group
 */
public class ClientUserChatMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String textMessage = new String(contentOfMessage);
		ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
		User thisUser = null;
		for (User user : thisGroup.getUsers())
		{
			if (user.getId() == userId)
			{
				thisUser = user;
			}
		}

		if (thisUser == null)
			thisUser = new User("Stranger", (byte) 0);

		System.out.println(thisUser.getName() + ": " + textMessage);

		User finalThisUser = thisUser;
		Platform.runLater(() -> thisGroup.getChatGroupContent().getUserMessages()
				.add(new ChatGroupMessage(finalThisUser, thisGroup, textMessage)));
	}
}
