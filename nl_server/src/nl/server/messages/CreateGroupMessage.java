package nl.server.messages;

import it.castelli.nl.*;
import it.castelli.nl.messages.IMessage;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import nl.server.AdvancedUser;
import nl.server.ServerData;
import nl.server.GroupManager;
import nl.server.UsersManager;

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

		User thisUser = UsersManager.getUserFromId(userId);
		if (thisUser == null)
		{
			System.out.println("Cannot find user with id " + userId + ". Group " + newGroupName + " not created.");
			return;
		}

		newGroup.getUsers().add(thisUser);
		newGroup.getSuperUsers().add(thisUser);
		GroupManager.getAllGroups().put(newGroupCode, newGroup);
		ServerData.getInstance().incrementLastGroupCode();

		Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);

		System.out.println("The group " + newGroupName + " has been created with the groupCode of: " + newGroupCode +
		                   " and it contains the user: " + userId);

		try
		{
			byte[] reply = MessageBuilder.buildClientNewGroupMessage(newGroup.getCode(), newGroup.getName());
			Sender.sendToClient(reply, thisUser.getIpAddress());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
