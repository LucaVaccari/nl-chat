package nl.server;

import it.castelli.nl.User;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 * Handles all the users of the application
 */
public class UsersManager
{
	private static HashMap<Byte, AdvancedUser> allUsers;
	public static final String USERS_FILE_PATH = "allUser.bin";

	static
	{
		try
		{
			allUsers = (HashMap<Byte, AdvancedUser>) Serializer.deserialize(USERS_FILE_PATH);
		}
		catch (IOException | ClassNotFoundException e)
		{
			allUsers = new HashMap<>();
		}
	}

    /**
     * Getter for the HashMap containing all the users bound to their id code
     * @return The HashMap of users
     */
	public static HashMap<Byte, AdvancedUser> getAllUsers()
	{
		return allUsers;
	}

    /**
     * Shortcut for getAllUsers().get(id)
     * @param id The id of the user to get
     * @return The user with the corresponding id
     */
	public static AdvancedUser getUserFromId(byte id)
	{
		return allUsers.get(id);
	}

	/**
	 * Gets the user which has the given IP address
	 * @param IPAddress The ip of the user to get
	 * @return the user with the corresponding IP
	 */
	public static AdvancedUser getUserFromIp(InetAddress IPAddress)
	{
		for (AdvancedUser user : allUsers.values()) {
			if (user.getIpAddress() == IPAddress)
				return user;
		}
		return null;
	}

	public static void addConnectionToUser(InetAddress IPAddress, Socket socket)
	{
		AdvancedUser user = getUserFromIp(IPAddress);
		if (user != null)
			user.setConnection(socket);
	}

	public static void removeConnectionToUser(InetAddress IPAddress)
	{
		AdvancedUser user = getUserFromIp(IPAddress);
		if (user != null)
			user.setConnection(null);
	}
}
