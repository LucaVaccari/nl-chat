package it.castelli.nl.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ClientData;
import it.castelli.nl.ClientGroupManager;
import it.castelli.nl.User;
import it.castelli.nl.graphics.ChatGroupComponent;
import it.castelli.nl.graphics.FXMLController;
import it.castelli.nl.serialization.Serializer;
import javafx.application.Platform;

import java.util.Arrays;

/**
 * Represents a message telling the client a new group has been successfully created
 */
public class ClientNewGroupMessage implements IMessage
{

	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String groupName = new String(contentOfMessage);
		ChatGroup newGroup = new ChatGroup(groupName, groupCode);
		ClientGroupManager.getAllGroups().put(groupCode, newGroup);

		User thisUser = ClientData.getInstance().getThisUser();
		newGroup.getUsers().add(thisUser);

		Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
		Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);

		// prevent the UI update to be executed on the secondary thread
		Platform.runLater(() -> FXMLController.get().chatGroupListView.getItems().add(new ChatGroupComponent(newGroup)));
	}
}