package it.castelli.nl.server.messages;


import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Request sent by the client to create a new group
 */
public class FeedbackMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);

		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte userId = data[2];
		UserManager.getAllUsers().get(userId).getIncomingMessagesAlreadySent().removeFirst();
	}
}
