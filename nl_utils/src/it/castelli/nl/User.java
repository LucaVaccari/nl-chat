package it.castelli.nl;

import it.castelli.nl.graphics.RGBColor;

import java.io.Serializable;

/**
 * A class that represents a user, made by an ID which is generated and provided by the server, a name, chosen by the
 * user and an IP address
 */
public class User implements Serializable
{
	private final byte id;
	private String name;
	private RGBColor color;

	/**
	 * Constructor for the User class with the name and the id
	 *
	 * @param name The name of the user
	 * @param id   The id of the user (provided by the server)
	 */
	public User(String name, byte id)
	{
		this.name = name;
		this.id = id;
		color = new RGBColor((byte) 0, (byte) 0, (byte) 0);
	}

	/**
	 * Getter for the id of the user
	 *
	 * @return The id of the user
	 */
	public byte getId()
	{
		return id;
	}

	/**
	 * Getter for the name of the user
	 *
	 * @return The name of the user
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for the name of the user
	 *
	 * @param name The new name for the user
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof User)
			return ((User) obj).getId() == this.getId();
		else
		{
			System.out.println("the object is not a user");
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return 93 + id;
	}

	/**
	 * Getter for the color (in Hexadecimal format)
	 *
	 * @return the String containing the color code
	 */
	public RGBColor getColor()
	{
		return color;
	}

	/**
	 * Setter for the color (in Hexadecimal format)
	 *
	 * @param color The new color (if the format is incorrect it will be ignored)
	 */
	public void setColor(RGBColor color)
	{
		this.color = color;
	}
}