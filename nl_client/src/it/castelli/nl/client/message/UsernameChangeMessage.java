package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import javafx.application.Platform;

import java.util.Arrays;

public class UsernameChangeMessage implements IMessage
{
	@Override
	public void onReceive(byte[] data)
	{
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String newUserName = new String(contentOfMessage).strip();

		for (ChatGroup group : ClientGroupManager.getAllGroups().values())
		{
			for (User user : group.getUsers())
			{
				if (user.getId() == userId)
				{
					user.setName(newUserName);
					System.out.println("Changed the color of " + user.getName() + " with color #" + newUserName);
					break;
				}
			}
		}

		for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
		{
			for (ChatMessageComponent chatMessageComponent :
					chatGroupComponent.getChatComponent().getMessageListView().getItems())
			{
				if (chatMessageComponent.getChatGroupMessage().getUserSender().getId() == userId)
				{
					Platform.runLater(() -> {
						chatMessageComponent.getUserNameLabel().setText(newUserName);
					});
				}
			}
		}
	}
}
