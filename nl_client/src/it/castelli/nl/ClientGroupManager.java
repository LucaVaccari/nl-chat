package it.castelli.nl;

import it.castelli.nl.graphics.ChatGroupComponent;
import it.castelli.nl.graphics.FXMLController;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Handles all the groups the user of the client is part of
 */
public class ClientGroupManager
{
	private static HashMap<Byte, ChatGroup> allGroups;
	public static final String GROUPS_FILE_PATH = "allGroups.bin";

	static
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
			FXMLController.get().chatGroupListView.getItems().add(new ChatGroupComponent(group));
		}
	}

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

	public static void init() {}
}
