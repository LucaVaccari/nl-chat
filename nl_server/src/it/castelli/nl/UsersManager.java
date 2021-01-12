package it.castelli.nl;

import it.castelli.nl.serialization.Serializer;

import java.util.HashMap;

/**
 * Handles all the users of the application
 */
public class UsersManager
{
	private static HashMap<Byte, User> allUsers;
	public static final String USERS_FILE_PATH = "allUser.bin";

	static
	{
		allUsers = (HashMap<Byte, User>) Serializer.deserialize(USERS_FILE_PATH);

		if (allUsers == null) allUsers = new HashMap<>();
	}

    /**
     * Getter for the HashMap containing all the users bound to their id code
     * @return The HashMap of users
     */
	public static HashMap<Byte, User> getAllUsers()
	{
		return allUsers;
	}

    /**
     * Shortcut for getAllUsers().get(id)
     * @param id The id of the user to get
     * @return The user with the corresponding id
     */
	public static User getUserFromId(byte id)
	{
		return allUsers.get(id);
	}
}
