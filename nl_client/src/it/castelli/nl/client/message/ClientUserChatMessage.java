package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.NLClient;
import it.castelli.nl.client.graphics.ChatComponent;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import it.castelli.nl.graphics.RGBColor;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * New user chat message sent by a user on a group
 */
public class ClientUserChatMessage implements IMessage
{
	@Override
	public void onReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String textMessage = new String(contentOfMessage).strip();
		ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
		User thisUser = null;
		for (User user : thisGroup.getUsers())
		{
			if (user.getId() == userId)
			{
				thisUser = user;
			}
		}

		if (thisUser == null)
			thisUser = new User("Stranger", (byte) 0);


		ChatGroupMessage message = new ChatGroupMessage(thisUser, thisGroup, textMessage);
		thisGroup.getChatGroupContent().getUserMessages().add(message);

		System.out.println(thisUser.getName() + ": " + textMessage);

		User finalThisUser = thisUser;
		Platform.runLater(() -> {
			// add a message UI component on the chat element
			ListView<ChatGroupComponent> chatGroupListView = FXMLController.get().chatGroupListView;

			ChatComponent chatComponent = null;
			ObservableList<ChatMessageComponent> chatComponentItems = null;
			// find the corresponding group
			for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
			{
				if (chatGroupComponent.getChatGroup().getCode() == groupCode)
				{
					chatComponentItems = chatGroupComponent.getChatComponent().getMessageListView().getItems();
					break;
				}
			}
			// scroll to the bottom if it's selected
			if (chatGroupListView.getSelectionModel().getSelectedItem() != null)
			{
				chatComponent = chatGroupListView.getSelectionModel().getSelectedItem().getChatComponent();
				chatComponent.getMessageListView().scrollTo(chatComponent.getMessageListView().getItems().size() - 1);
			}
			ChatMessageComponent chatMessageComponent = new ChatMessageComponent(message);

			// color
			RGBColor userColor = finalThisUser.getColor();
			Color newColor = Color.color((double) userColor.getRed() / RGBColor.MAX_COLOR_SIZE,
					(double) userColor.getGreen() / RGBColor.MAX_COLOR_SIZE,
					(double) userColor.getBlue() / RGBColor.MAX_COLOR_SIZE);
			chatMessageComponent.getUserNameLabel().setTextFill(newColor);

			if (chatComponentItems != null)
				chatComponentItems.add(chatMessageComponent);

			// set last message on the ChatGroupComponent UI element
			Label lastMessageLabel = chatGroupListView.getSelectionModel().getSelectedItem().getLastMessageLabel();
			lastMessageLabel.setText(finalThisUser.getName() + ": " + textMessage);

			// set stage to front
			if (!NLClient.getPrimaryStage().isFocused() && !FXMLController.get().settingsStage.isFocused())
			{
				NLClient.getPrimaryStage().toFront();
			}
		});
	}
}
