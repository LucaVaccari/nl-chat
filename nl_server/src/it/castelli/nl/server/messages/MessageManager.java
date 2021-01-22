package it.castelli.nl.server.messages;


import it.castelli.nl.messages.MessageBuilder;

import java.util.HashMap;

/**
 * The ServerMessageManager organizes all the types of messages which the server can receive,
 * by mapping their instances with a specific message code (1 byte, using the constants defined in MessageBuilder)
 */
public class MessageManager
{
	private static final HashMap<Byte, Message> messageReceivers = new HashMap<>();

	static
	{
		messageReceivers.put(MessageBuilder.CREATE_GROUP_MESSAGE_TYPE, new CreateGroupMessage());
		messageReceivers.put(MessageBuilder.JOIN_GROUP_MESSAGE_TYPE, new JoinGroupMessage());
		messageReceivers.put(MessageBuilder.LEAVE_GROUP_MESSAGE_TYPE, new LeaveGroupMessage());
		messageReceivers.put(MessageBuilder.REMOVE_GROUP_MESSAGE_TYPE, new RemoveGroupMessage());
		messageReceivers.put(MessageBuilder.SERVER_NEW_USER_MESSAGE_TYPE, new NewUserMessage());
		messageReceivers.put(MessageBuilder.SERVER_TEST_MESSAGE_TYPE, new TestMessage());
		messageReceivers.put(MessageBuilder.SERVER_USER_CHAT_MESSAGE_TYPE, new UserChatMessage());
		messageReceivers.put(MessageBuilder.SERVER_END_CONNECTION_MESSAGE_TYPE, new EndConnectionMessage());
		messageReceivers.put(MessageBuilder.USER_SET_COLOR_MESSAGE_TYPE, new UserSetColorMessage());
	}

	/**
	 * Returns the MessageReceiver (implementation of IMessage, with OnReceive function) bound to the code specified
	 *
	 * @param messageType The code of the corresponding message receiver
	 * @return The message receiver corresponding to the code
	 */
	public static Message getMessageReceiver(byte messageType)
	{
		return messageReceivers.get(messageType);
	}
}
