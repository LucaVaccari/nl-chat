package it.castelli.nl;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * A class that represents a user, made by an ID which is generated and provided by the server, a name, chosen by the user and an IP address
 */
public class User implements Serializable
{
	private final byte id;

	private String name;
	private InetAddress ipAddress;

	/**
	 * Constructor for the User class with the name, the address and the id
	 * @param name The name of the user
	 * @param ipAddress The IP address of the user
	 * @param id The id of the user (provided by the server)
	 */
	public User(String name, InetAddress ipAddress, byte id)
	{
		this.name = name;
		this.ipAddress = ipAddress;
		this.id = id;
	}

	/**
	 * Constructor for the User class with the name and the id
	 * @param name The name of the user
	 * @param id The id of the user (provided by the server)
	 */
	public User(String name, byte id)
	{
		this.name = name;
		this.id = id;
	}

	/**
	 * Constructor for the User class with the IP address and the id
	 * @param ipAddress The IP address of the user
	 * @param id The id of the user (provided by the server)
	 */
	public User(InetAddress ipAddress, byte id)
	{
		this.ipAddress = ipAddress;
		this.id = id;
	}

	/**
	 * Getter for the id of the user
	 * @return The id of the user
	 */
	public byte getId()
	{
		return id;
	}

	/**
	 * Getter for the name of the user
	 * @return The name of the user
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for the name of the user
	 * @param name The new name for the user
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * The getter for the IP address of the user
	 * @return The IP address of the user
	 */
	public InetAddress getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * Setter for the IP address of the user
	 * @param ipAddress The new IP address for the user
	 */
	public void setIpAddress(InetAddress ipAddress)
	{
		this.ipAddress = ipAddress;
	}
}


