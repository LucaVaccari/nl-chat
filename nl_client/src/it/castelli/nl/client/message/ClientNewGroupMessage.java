package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.FXMLController;
import javafx.application.Platform;

import java.util.Arrays;

/**
 * Represents a message telling the client a new group has been successfully created
 */
public class ClientNewGroupMessage implements IMessage
{

	@Override
	public void onReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String groupName = new String(contentOfMessage).strip();
		ChatGroup newGroup = new ChatGroup(groupName, groupCode);
		ClientGroupManager.getAllGroups().put(groupCode, newGroup);

		User thisUser = ClientData.getInstance().getThisUser();
		newGroup.getUsers().add(thisUser);

		System.out.println("New group " + groupName + " has been created with id " + groupCode);

		// prevent the UI update to be executed on the secondary thread
		Platform.runLater(
				() -> FXMLController.get().chatGroupListView.getItems().add(new ChatGroupComponent(newGroup)));
	}
}