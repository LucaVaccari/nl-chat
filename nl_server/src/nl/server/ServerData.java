package nl.server;

import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Contains all the shared data every class of the server might need.
 * Implements the singleton pattern to allow the server data to be accessible everywhere and to be serialized.
 */
public class ServerData implements Serializable
{
	public static final String SERVER_DATA_FILE_PATH = "serverData.bin";
	private static ServerData instance;

	private byte lastUserId = 1;
	private byte lastGroupCode = 1;

	private ServerData() {}

	/**
	 * Getter for the singleton instance
	 *
	 * @return The singleton instance
	 */
	public static ServerData getInstance()
	{
		if (instance == null)
		{
			try
			{
				instance = (ServerData) Serializer.deserialize(SERVER_DATA_FILE_PATH);
			}
			catch (IOException | ClassNotFoundException e)
			{
				instance = new ServerData();
			}
		}

		return instance;
	}

	/**
	 * Getter for the last user id assigned to a user (the ids are generated progressively)
	 *
	 * @return The last generated user id
	 */
	public byte getLastUserId()
	{
		return lastUserId;
	}

	/**
	 * Getter for the last group code assigned to a group (the codes are generated progressively)
	 *
	 * @return The last generated group code
	 */
	public byte getLastGroupCode()
	{
		return lastGroupCode;
	}

	/**
	 * Generate a new user id, by incrementing the current one
	 */
	public void incrementLastUserId()
	{
		lastUserId++;
	}

	/**
	 * Generate a new group code, by incrementing the current one
	 */
	public void incrementLastGroupCode()
	{
		lastGroupCode++;
	}
}
