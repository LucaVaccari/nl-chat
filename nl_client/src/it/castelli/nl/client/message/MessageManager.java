package it.castelli.nl.client.message;

import it.castelli.nl.messages.MessageBuilder;

import java.util.HashMap;

/**
 * The ClientMessageManager organizes all the types of messages which the client can receive,
 * by mapping their instances with a specific message code (1 byte, using the constants defined in MessageBuilder)
 */
public class MessageManager
{
	private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

	static
	{
		messageReceivers.put(MessageBuilder.CLIENT_NEW_GROUP_MESSAGE_TYPE, new ClientNewGroupMessage());
		messageReceivers.put(MessageBuilder.CLIENT_NEW_USER_MESSAGE_TYPE, new ClientNewUserMessage());
		messageReceivers.put(MessageBuilder.CLIENT_TEST_MESSAGE_TYPE, new ClientTestMessage());
		messageReceivers.put(MessageBuilder.CLIENT_USER_CHAT_MESSAGE_TYPE, new ClientUserChatMessage());
		messageReceivers.put(MessageBuilder.INFORMATION_MESSAGE_TYPE, new InformationMessage());
		messageReceivers.put(MessageBuilder.GROUP_REMOVED_MESSAGE_TYPE, new GroupRemovedMessage());
		messageReceivers.put(MessageBuilder.USER_ID_MESSAGE_TYPE, new UserIdMessage());
		messageReceivers.put(MessageBuilder.USER_SET_COLOR_MESSAGE_TYPE, new UserSetColorMessage());
		messageReceivers.put(MessageBuilder.USER_NAME_CHANGE_MESSAGE_TYPE, new UsernameChangeMessage());
	}

	/**
	 * Returns the MessageReceiver (implementation of IMessage, with OnReceive function) bound to the code specified
	 *
	 * @param messageType The code of the corresponding message receiver
	 * @return The message receiver corresponding to the code
	 */
	public static IMessage getMessageReceiver(byte messageType)
	{
		return messageReceivers.get(messageType);
	}
}
