package it.castelli.nl;

import java.util.ArrayList;

/**
 * Contains the possible content of a group (like messages)
 */
public class ChatGroupContent
{
	private final ArrayList<String> userMessages = new ArrayList<>();

	/**
	 * Getter for the list of messages of the group
	 * @return The list of messages of the group
	 */
	public ArrayList<String> getUserMessages()
	{
		return userMessages;
	}
}