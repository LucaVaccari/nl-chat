package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class UserSetColorMessage implements IMessage
{
	/**
	 * Called when a packet is received
	 *
	 * @param data The content of the packet received
	 */
	@Override
	public void OnReceive(byte[] data)
	{
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String color = new String(contentOfMessage).strip();

		for (ChatGroup group : ClientGroupManager.getAllGroups().values())
		{
			for (User user : group.getUsers())
			{
				if (user.getId() == userId)
				{
					user.setColor(color);
					System.out.println("Changed the color of " + user.getName() + " with color #" + color);
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
					chatMessageComponent.getUserNameLabel().setTextFill(Color.web(color));
				}
			}
		}
	}
}
