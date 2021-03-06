package it.castelli.nl.server.messages;


import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.ServerData;

import java.io.IOException;
import java.util.Arrays;

/**
 * Request sent by the client to create a new group
 */
public class CreateGroupMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);

		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String newGroupName = new String(contentOfMessage).strip();

		byte newGroupCode = ServerData.getInstance().getLastGroupCode();
		ChatGroup newGroup = new ChatGroup(newGroupName, newGroupCode);
		User thisUser = connection.getAdvancedUser().getUser();

		newGroup.getUsers().add(thisUser);
		newGroup.getSuperUsers().add(thisUser);
		GroupManager.getAllGroups().put(newGroupCode, newGroup);
		ServerData.getInstance().incrementLastGroupCode();

		System.out.println("The group " + newGroupName + " has been created with the groupCode of: " + newGroupCode +
				" and it contains the user: " + userId);

		try
		{
			byte[] reply = MessageBuilder.buildClientNewGroupMessage(newGroup.getCode(), newGroup.getName());
			System.out.println("Created ClientNewGroupMessage from CreateGroupMessage in the onReceive method");
			Sender.sendToUser(reply, thisUser);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
