package it.castelli.nl.client;

import it.castelli.nl.User;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * Contains all the shared data every class of the client might need.
 * Implements the singleton pattern to allow the client data to be accessible everywhere and to be serialized.
 */
public class ClientData implements Serializable
{
	public static final String CLIENT_DATA_FILE_PATH = "clientData.bin";
	private static ClientData instance;
	private final LinkedList<byte[]> messageQueue = new LinkedList<>();
	private User thisUser;
	private InetAddress serverAddress;

	/**
	 * DO NOT USE FOR ANY REASON
	 */
	public ClientData()
	{
	}

	/**
	 * Getter for the singleton instance
	 *
	 * @return The singleton instance
	 */
	public static ClientData getInstance()
	{
		if (instance == null)
		{
			try
			{
				instance = (ClientData) Serializer.deserialize(CLIENT_DATA_FILE_PATH);
				System.out.println("The length of the queue after deserialization is: " + getInstance().messageQueue.size());
			}
			catch (IOException | ClassNotFoundException e)
			{
				instance = new ClientData();
			}
		}

		return instance;
	}

	/**
	 * Getter for the user object of the client
	 *
	 * @return The user object of the client
	 */
	public User getThisUser()
	{
		return thisUser;
	}

	/**
	 * Sertter for the user object of the client
	 *
	 * @param user The new user to be assigned
	 */
	public void setThisUser(User user)
	{
		thisUser = user;
	}

	/**
	 * Getter for the server address
	 *
	 * @return The server address
	 */
	public InetAddress getServerAddress()
	{
		if (serverAddress == null)
		{
			try
			{
				setServerAddress(InetAddress.getLocalHost());
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}

		return serverAddress;
	}

	/**
	 * Setter for the server address
	 *
	 * @param serverAddress The new server address
	 */
	public void setServerAddress(InetAddress serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	public LinkedList<byte[]> getMessageQueue()
	{
		return messageQueue;
	}
}
