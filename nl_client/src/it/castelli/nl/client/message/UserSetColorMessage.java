package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import it.castelli.nl.graphics.RGBColor;
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
	public void onReceive(byte[] data)
	{
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		RGBColor rgbColor = new RGBColor(contentOfMessage[0], contentOfMessage[1], contentOfMessage[2]);

		for (ChatGroup group : ClientGroupManager.getAllGroups().values())
		{
			for (User user : group.getUsers())
			{
				if (user.getId() == userId)
				{
					user.setColor(rgbColor);
					System.out.println("Changed the color of " + user.getName() + " with color " + rgbColor);
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
					Color newColor = Color.color((double) rgbColor.getRed() / RGBColor.MAX_COLOR_SIZE,
							(double) rgbColor.getGreen() / RGBColor.MAX_COLOR_SIZE,
							(double) rgbColor.getBlue() / RGBColor.MAX_COLOR_SIZE);
					chatMessageComponent.getUserNameLabel().setTextFill(newColor);
				}
			}
		}
	}
}
