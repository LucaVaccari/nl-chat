package it.castelli.nl.client;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.ChatMessageComponent;
import it.castelli.nl.client.graphics.FXMLController;
import it.castelli.nl.serialization.Serializer;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.IOException;
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
		}
		catch (IOException | ClassNotFoundException e)
		{
			allGroups = new HashMap<>();
		}

		assert allGroups != null;
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

			for (int i = 0; i < group.getChatGroupContent().getUserMessages().size(); i++)
			{
				ObservableList<ChatMessageComponent> messageListViewItems =
						chatGroupComponent.getChatComponent().getMessageListView().getItems();
				ChatMessageComponent chatMessageComponent =
						new ChatMessageComponent(group.getChatGroupContent().getUserMessages().get(i));
				messageListViewItems.add(chatMessageComponent);
			}

			Label lastMessageLabel = chatGroupComponent.getLastMessageLabel();
			ChatGroupMessage lastMessage = group.getChatGroupContent().getUserMessages()
					.get(group.getChatGroupContent().getUserMessages().size() - 1);

			if (lastMessageLabel != null)
			{
				lastMessageLabel.setText(lastMessage.getUserSender().getName() + ": " + lastMessage.getMessageContent());
			}
		}
	}
}
