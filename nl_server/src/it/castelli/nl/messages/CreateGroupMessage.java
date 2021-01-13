package it.castelli.nl.messages;

import it.castelli.nl.ServerData;
import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.util.Arrays;

/**
 * Request sent by the client to create a new group
 */
public class CreateGroupMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String newGroupName = new String(contentOfMessage);

		byte newGroupCode = ServerData.getInstance().getLastGroupCode();
		ChatGroup newGroup = new ChatGroup(newGroupName, newGroupCode);
		newGroup.getUsers().add(UsersManager.getUserFromId(userId));
		newGroup.getSuperUsers().add(UsersManager.getUserFromId(userId));
		ServerGroupManager.getAllGroups().put(newGroupCode, newGroup);
		ServerData.getInstance().incrementLastGroupCode();

		Serializer.serialize(ServerGroupManager.getAllGroups(), ServerGroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);

		System.out.println("The group " + newGroupName + " has been created with the groupCode of: " + newGroupCode +
		                   " and it contains the user: " + userId);

		try
		{
			byte[] reply = MessageBuilder.buildClientNewGroupMessage(newGroup.getCode(), newGroup.getName());
			Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
