package nl.server.messages;

import it.castelli.nl.messages.IMessage;
import it.castelli.nl.messages.MessageBuilder;

import java.util.HashMap;

/**
 * The ServerMessageManager organizes all the types of messages which the server can receive,
 * by mapping their instances with a specific message code (1 byte, using the constants defined in MessageBuilder)
 */
public class ServerMessageManager
{
	private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

	static
	{
		messageReceivers.put(MessageBuilder.CREATE_GROUP_MESSAGE_TYPE, new CreateGroupMessage());
		messageReceivers.put(MessageBuilder.JOIN_GROUP_MESSAGE_TYPE, new JoinGroupMessage());
		messageReceivers.put(MessageBuilder.LEAVE_GROUP_MESSAGE_TYPE, new LeaveGroupMessage());
		messageReceivers.put(MessageBuilder.REMOVE_GROUP_MESSAGE_TYPE, new RemoveGroupMessage());
		messageReceivers.put(MessageBuilder.SERVER_NEW_USER_MESSAGE_TYPE, new ServerNewUserMessage());
		messageReceivers.put(MessageBuilder.SERVER_TEST_MESSAGE_TYPE, new ServerTestMessage());
		messageReceivers.put(MessageBuilder.SERVER_USER_CHAT_MESSAGE_TYPE, new ServerUserChatMessage());
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
