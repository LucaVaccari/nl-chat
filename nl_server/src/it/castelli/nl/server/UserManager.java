package it.castelli.nl.server;

import it.castelli.nl.User;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Handles all the users of the application
 */
public class UserManager
{
	public static final String USERS_FILE_PATH = "allUser.bin";
	private static HashMap<Byte, AdvancedUser> allUsers;

	/**
	 * Getter for the HashMap containing all the users bound to their id code
	 *
	 * @return The HashMap of users
	 */
	public static HashMap<Byte, AdvancedUser> getAllUsers()
	{
		return allUsers;
	}

	/**
	 * Shortcut for getAllUsers().get(id)
	 *
	 * @param id The id of the user to get
	 * @return The user with the corresponding id
	 */
	public static User getUserFromId(byte id)
	{
		return allUsers.get(id).getUser();
	}

	public static LinkedList<byte[]> getQueueFromId(byte id)
	{
		return allUsers.get(id).getIncomingMessages();
	}

	public static LinkedList<byte[]> getQueueFromUser(User user)
	{
		byte id = user.getId();
		return allUsers.get(id).getIncomingMessages();
	}

	public static void init()
	{
		try
		{
			allUsers = (HashMap<Byte, AdvancedUser>) Serializer.deserialize(USERS_FILE_PATH);
		}
		catch (IOException | ClassNotFoundException e)
		{
			allUsers = new HashMap<>();
		}
		if (allUsers == null)
			allUsers = new HashMap<>();

		for(AdvancedUser user : allUsers.values())
		{
			for(int i = user.incomingMessagesAlreadySent.size() - 1; i >= 0; i--)
			{
				byte[] message = user.incomingMessagesAlreadySent.get(i);
				user.incomingMessages.addFirst(message);
				user.incomingMessagesAlreadySent.remove(message);
			}
		}
	}

	public static class AdvancedUser implements Serializable
	{
		private final LinkedList<byte[]> incomingMessages = new LinkedList<>();
		private final LinkedList<byte[]> incomingMessagesAlreadySent = new LinkedList<>();
		private User user;

		public AdvancedUser(User user)
		{
			this.user = user;
		}

		public User getUser()
		{
			return user;
		}

		public void setUser(User user)
		{
			this.user = user;
		}

		public LinkedList<byte[]> getIncomingMessages()
		{
			return incomingMessages;
		}

		public LinkedList<byte[]> getIncomingMessagesAlreadySent()
		{
			return incomingMessagesAlreadySent;
		}
	}


}
