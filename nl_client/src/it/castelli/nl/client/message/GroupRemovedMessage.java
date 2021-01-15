package it.castelli.nl.client.message;

import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.FXMLController;
import javafx.collections.ObservableList;

/**
 * A group has been removed.
 */
public class GroupRemovedMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		ClientGroupManager.getAllGroups().remove(groupCode);

		// remove from ui
		ObservableList<ChatGroupComponent> chatGroups = FXMLController.get().chatGroupListView.getItems();
		for (ChatGroupComponent chatGroupUi : chatGroups)
		{
			if (chatGroupUi.getChatGroup().getCode() == groupCode)
			{
				chatGroups.remove(chatGroupUi);
				return;
			}
		}
	}
}
