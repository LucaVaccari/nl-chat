package it.castelli.nl.client;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import it.castelli.nl.graphics.RGBColor;
import it.castelli.nl.serialization.Serializer;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles all the groups the user of the client is part of
 */
public class ClientGroupManager
{
	public static final String GROUPS_FILE_PATH = "allGroups.bin";
	private static HashMap<Byte, ChatGroup> allGroups;


	/**
	 * Getter for the HashMap containing all groups mapped to their codes
	 *
	 * @return The HashMap of the groups
	 */
	public static HashMap<Byte, ChatGroup> getAllGroups()
	{
		return allGroups;
	}

	/**
	 * Shortcut for getAllGroups().get(code)
	 *
	 * @param code The code of the group to be got
	 * @return The ChatGroup corresponding to the code provided
	 */
	public static ChatGroup getGroupFromCode(byte code)
	{
		return allGroups.get(code);
	}

	public static void init()
	{
		try
		{
			allGroups = (HashMap<Byte, ChatGroup>) Serializer.deserialize(GROUPS_FILE_PATH);
			if (allGroups == null)
				allGroups = new HashMap<>();
		}
		catch (IOException | ClassNotFoundException e)
		{
			allGroups = new HashMap<>();
		}

		// update UI
		for (ChatGroup group : allGroups.values())
		{
			ObservableList<ChatGroupComponent> chatGroupListViewItems =
					FXMLController.get().chatGroupListView.getItems();
			if (FXMLController.get() != null)
				chatGroupListViewItems.add(new ChatGroupComponent(group));
			else
				System.out.println("FXMLController.get() is null");

			ChatGroupComponent chatGroupComponent = null;
			// find ChatGroupComponent with chatGroup == group
			for (ChatGroupComponent element : chatGroupListViewItems)
			{
				if (element.getChatGroup().getCode() == group.getCode())
				{
					chatGroupComponent = element;
					break;
				}
			}

			if (chatGroupComponent == null)
				continue;

			ArrayList<ChatGroupMessage> userMessages = group.getChatGroupContent().getUserMessages();
			for (ChatGroupMessage userMessage : userMessages)
			{
				ObservableList<ChatMessageComponent> messageListViewItems =
						chatGroupComponent.getChatComponent().getMessageListView().getItems();
				ChatMessageComponent chatMessageComponent = new ChatMessageComponent(userMessage);

				// set user color
				RGBColor userColor = userMessage.getUserSender().getColor();
				Color newColor = Color.color((double) userColor.getRed() / RGBColor.MAX_COLOR_SIZE,
						(double) userColor.getGreen() / RGBColor.MAX_COLOR_SIZE,
						(double) userColor.getBlue() / RGBColor.MAX_COLOR_SIZE);
				chatMessageComponent.getUserNameLabel().setTextFill(newColor);

				messageListViewItems.add(chatMessageComponent);
			}

			updateLastMessageLabel(chatGroupComponent, userMessages);
		}
	}

	public static void updateLastMessageLabel(ChatGroupComponent chatGroupComponent,
	                                          ArrayList<ChatGroupMessage> userMessages)
	{
		Label lastMessageLabel = chatGroupComponent.getLastMessageLabel();

		if (userMessages.size() > 0)
		{
			ChatGroupMessage lastMessage = userMessages.get(userMessages.size() - 1);
			lastMessageLabel
					.setText(lastMessage.getUserSender().getName() + ": " + lastMessage.getMessageContent());
		}
	}
}
