package it.castelli.nl.client.message;

import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.serialization.Serializer;

import java.util.Arrays;

/**
 * A new user has entered a group (message to the client)
 */
public class ClientNewUserMessage implements IMessage
{

	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String userName = new String(contentOfMessage);
		ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
		User newUser = new User(userName, userId);

		Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);

		System.out.println(userName + " entered " + thisGroup.getName());

		thisGroup.getUsers().add(newUser);
	}
}
