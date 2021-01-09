package it.castelli.nl.message;

import nl.messages.IMessage;

import java.util.HashMap;

public class ClientMessageManager
{
    private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

    static
    {
        messageReceivers.put((byte) 0, new ClientUserChatMessage());
        messageReceivers.put((byte) 1, new ClientNewGroupMessage());
        messageReceivers.put((byte) 2, new ClientNewUserMessage());
        messageReceivers.put((byte) 3, new GroupRemovedMessage());
    }

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
