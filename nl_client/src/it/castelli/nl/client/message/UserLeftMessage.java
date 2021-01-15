package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.serialization.Serializer;

public class UserLeftMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
		User userWhoLeft = thisGroup.getUserById(userId);

		Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);

		System.out.println(userWhoLeft.getName() + " left " + thisGroup.getName());

		thisGroup.getUsers().remove(userWhoLeft);
	}
}
