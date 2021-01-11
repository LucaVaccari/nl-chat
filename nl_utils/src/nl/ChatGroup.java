package nl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A group containing users. Every user member can send a message and all the others will receive it.
 * A small subgroup of super users can decide to remove the group (or remove members, MAYBE, not yet)
 */
public class ChatGroup implements Serializable
{
	private ArrayList<User> users;
	private ArrayList<User> superUsers;
	private final byte code;
	private String name;
	private final ChatGroupContent chatGroupContent = new ChatGroupContent();

	/**
	 * Constructor taking the name and the code of the group
	 * @param name The name of the group
	 * @param code The code of the group
	 */
	public ChatGroup(String name, byte code)
	{
		this.name = name;
		this.code = code;
	}

	/**
	 * Getter for the code of the group
	 * @return The code of the group
	 */
	public byte getCode()
	{
		return code;
	}

	/**
	 * Getter for the name of the group
	 * @return The name of the group
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for the name of the group
	 * @param name The new name for the group
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Getter for the users of the group
	 * @return The list of users of the group
	 */
	public ArrayList<User> getUsers()
	{
		return users;
	}

	/**
	 * The getter for the chat group content, containing all the messages of the chat
	 * @return The ChatGroupContent
	 */
	public ChatGroupContent getChatGroupContent()
	{
		return chatGroupContent;
	}

	/**
	 * Getter for the list of super users (with more privileges)
	 * @return The list of super users
	 */
	public ArrayList<User> getSuperUsers()
	{
		return superUsers;
	}
}
