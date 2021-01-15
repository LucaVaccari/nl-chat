package it.castelli.nl.server.messages;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;


public class LeaveGroupMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		ChatGroup groupToLeave = GroupManager.getGroupFromCode(groupCode);
		User thisUser = connection.getAdvancedUser().getUser();
		groupToLeave.getUsers().remove(thisUser);
		groupToLeave.getSuperUsers().remove(thisUser);


		byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
		System.out.println("Created RemovedGroupMessage from LeaveGroupMessage in the onReceive method");
		Sender.sendToUser(reply, thisUser);

		reply = MessageBuilder.buildUserLeftMessage(thisUser.getId(), groupCode);
		System.out.println("Created UserLeftMessage from LeaveGroupMessage in the onReceive method");
		Sender.sendToOthersInGroup(reply, thisUser, groupToLeave);
	}
}
