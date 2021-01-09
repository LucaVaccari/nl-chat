package it.castelli.nl.messages;

import nl.messages.IMessage;
import nl.messages.MessageBuilder;
import java.util.HashMap;

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

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
