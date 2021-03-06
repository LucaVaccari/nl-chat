package it.castelli.nl.client.message;

import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.AlertUtil;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.FXMLController;
import javafx.application.Platform;
import javafx.collections.ObservableList;

/**
 * A group has been removed.
 */
public class GroupRemovedMessage implements IMessage
{
	@Override
	public void onReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		ClientGroupManager.getAllGroups().remove(groupCode);

		Platform.runLater(() -> {
			AlertUtil.showInformationAlert("Group removed", "One of your groups has been removed",
					"You left one of your groups, or it was deleted by its creator");
		});

		// remove from ui
		Platform.runLater(() -> {
			ObservableList<ChatGroupComponent> chatGroups = FXMLController.get().chatGroupListView.getItems();
			for (ChatGroupComponent chatGroupUi : chatGroups)
			{
				if (chatGroupUi.getChatGroup().getCode() == groupCode)
				{
					chatGroups.remove(chatGroupUi);
					return;
				}
			}
		});
	}
}
