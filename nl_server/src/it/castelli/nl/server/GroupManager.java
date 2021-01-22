package it.castelli.nl.server;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
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
			for (ChatGroup group : allGroups.values())
			{
				for (User user : group.getUsers())
				{
					System.out.println("the group " + group.getCode() + " contains user " + user.getId());
				}
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			allGroups = new HashMap<>();
			System.out.println("allGroup not found, creating new allGroups");
		}
	}
}
