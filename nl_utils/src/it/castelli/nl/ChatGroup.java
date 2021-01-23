package it.castelli.nl;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A group containing users. Every user member can send a message and all the others will receive it.
 * A small subgroup of super users can decide to remove the group (or remove members, MAYBE, not yet)
 */
public class ChatGroup implements Serializable
{
	protected final HashSet<User> users;
	protected final HashSet<User> superUsers;
	private final byte code;
	private final ChatGroupContent chatGroupContent = new ChatGroupContent();
	private String name;

	/**
	 * Constructor taking the name and the code of the group
	 *
	 * @param name The name of the group
	 * @param code The code of the group
	 */
	public ChatGroup(String name, byte code)
	{
		this.name = name;
		this.code = code;
		superUsers = new HashSet<>();
		users = new HashSet<>();
	}

	/**
	 * Getter for the code of the group
	 *
	 * @return The code of the group
	 */
	public byte getCode()
	{
		return code;
	}

	/**
	 * Getter for the name of the group
	 *
	 * @return The name of the group
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for the name of the group
	 *
	 * @param name The new name for the group
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Getter for the users of the group
	 *
	 * @return The list of users of the group
	 */
	public HashSet<User> getUsers()
	{
		return users;
	}

	/**
	 * The getter for the chat group content, containing all the messages of the chat
	 *
	 * @return The ChatGroupContent
	 */
	public ChatGroupContent getChatGroupContent()
	{
		return chatGroupContent;
	}

	/**
	 * Getter for the list of super users (with more privileges)
	 *
	 * @return The list of super users
	 */
	public HashSet<User> getSuperUsers()
	{
		return superUsers;
	}


	public User getUserById(byte userId)
	{
		for (User user : users)
		{
			if (user.getId() == userId)
				return user;
		}
		return null;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ChatGroup)
		{
			return (((ChatGroup) obj).getCode() == this.getCode());
		}
		return false;
	}
}
