package it.castelli.nl.server;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;

public class Sender
{
	/**
	 * Sends the given data to a specific given User.
	 *
	 * @param data data to be sent
	 * @param user user to send the data to
	 */
	public static void sendToUser(byte[] data, User user)
	{
		byte[] dataWithHeader = MessageBuilder.addHeader(data);
		UserManager.getQueueFromUser(user).add(dataWithHeader);
		//System.out.println("data added to the queue of the user: " + user.getId());
	}

	/**
	 * Sends the given data to the given group. The given sender user won't receive the data
	 *
	 * @param data       data to be sent
	 * @param senderUser sender user (who won't receive the data)
	 * @param group      group to send the data to
	 */
	public static void sendToOthersInGroup(byte[] data, User senderUser, ChatGroup group)
	{
		for (User user : group.getUsers())
		{
			if (!user.equals(senderUser))
			{
				byte[] dataWithHeader = MessageBuilder.addHeader(data);
				UserManager.getQueueFromUser(user).add(dataWithHeader);
				//System.out.println("data added to the queue of the user: " + user.getId());
			}
		}
	}

	/**
	 * Sends the given data to the given group.
	 *
	 * @param data  data to be sent
	 * @param group group to send the data to
	 */
	public static void sendToGroup(byte[] data, ChatGroup group)
	{
		for (User user : group.getUsers())
		{
			byte[] dataWithHeader = MessageBuilder.addHeader(data);
			UserManager.getQueueFromUser(user).add(dataWithHeader);
		}
	}

}
