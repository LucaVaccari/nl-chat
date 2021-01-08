package it.castelli.nl.messages;

import nl.messages.IMessage;

import java.util.HashMap;

public class ServerMessageManager
{
    private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

    static
    {
        messageReceivers.put((byte) 0, new UserChatMessage());
        messageReceivers.put((byte) 1, new CreateGroupMessage());
        messageReceivers.put((byte) 2, new JoinGroupMessage());
        messageReceivers.put((byte) 3, new LeaveGroupMessage());
        messageReceivers.put((byte) 4, new RemoveGroupMessage());
    }

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
