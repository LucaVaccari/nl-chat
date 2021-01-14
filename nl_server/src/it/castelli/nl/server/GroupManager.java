package it.castelli.nl.server;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Handles all of the existing groups
 */
public class GroupManager
{
	public static final String GROUPS_FILE_PATH = "allGroups.bin";
	private static HashMap<Byte, ChatGroup> allGroups;

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
}
