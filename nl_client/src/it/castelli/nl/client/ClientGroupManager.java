package it.castelli.nl.client;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.client.graphics.ChatGroupComponent;
import it.castelli.nl.client.graphics.FXMLController;
import it.castelli.nl.serialization.Serializer;

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

	public static void init() {
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
			if (FXMLController.get() != null)
				FXMLController.get().chatGroupListView.getItems().add(new ChatGroupComponent(group));
			else
				System.out.println("FXMLController.get() is null");
		}
	}
}
