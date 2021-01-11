package it.castelli.nl;

import it.castelli.nl.serialization.Serializer;

import java.util.HashMap;

/**
 * Handles all of the existing groups
 */
public class ServerGroupManager
{
	private static HashMap<Byte, ChatGroup> allGroups;
	public static final String GROUPS_FILE_PATH = "allGroups.bin";
	public static int lastGroupCode;

	static
	{
		allGroups = (HashMap<Byte, ChatGroup>) Serializer.deserialize(GROUPS_FILE_PATH);
	}

	/**
	 * Getter for the HashMap containing all groups mapped to their codes
	 * @return The HashMap of the groups
	 */
	public static HashMap<Byte, ChatGroup> getAllGroups()
	{
		return allGroups;
	}

	/**
	 * Shortcut for getAllGroups().get(code)
	 * @param code The code of the group to be got
	 * @return The ChatGroup corresponding to the code provided
	 */
	public static ChatGroup getGroupFromCode(int code)
	{
		return allGroups.get(code);
	}
}
