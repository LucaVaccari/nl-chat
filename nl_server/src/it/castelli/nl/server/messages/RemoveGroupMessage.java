package it.castelli.nl.server.messages;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;

import java.io.IOException;


public class RemoveGroupMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		ChatGroup groupToRemove = GroupManager.getGroupFromCode(groupCode);
		User thisUser = connection.getAdvancedUser().getUser();
		if (groupToRemove.getSuperUsers().contains(thisUser))
		{
			byte[] reply = new byte[0];
			try
			{
				reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("Created RemovedGroupMessage from RemoveGroupMessage in the onReceive method");
			Sender.sendToGroup(reply, groupToRemove);

			GroupManager.getAllGroups().remove(groupCode);
		} else
		{
			byte[] reply = new byte[0];
			try
			{
				reply = MessageBuilder.buildInformationMessage("You are not the creator of this group, so you can't remove it");
				Sender.sendToUser(reply, thisUser);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
