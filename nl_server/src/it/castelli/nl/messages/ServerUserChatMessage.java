package it.castelli.nl.messages;

import it.castelli.nl.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class which handles receiving user chat messages on the server.
 * It forwards the message to all of the users of a group
 */
public class ServerUserChatMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String textMessage = new String(contentOfMessage);
		ChatGroup thisGroup = ServerGroupManager.getGroupFromCode(groupCode);
		User thisUser = UsersManager.getUserFromId(userId);

		System.out.println("a user message has arrived from user: " + userId + " in the group with code: " + groupCode);

		if (thisGroup.getUsers().contains(thisUser))
		{
			//send ClientUserChatMessage

			try
			{
				byte[] reply = MessageBuilder.buildClientUserChatMessage(new ChatGroupMessage(thisUser, thisGroup,
				                                                                              textMessage));
				for (User user : thisGroup.getUsers())
					Sender.sendToClient(reply, user.getIpAddress());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}
}
