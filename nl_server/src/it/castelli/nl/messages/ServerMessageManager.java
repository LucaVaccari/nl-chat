package it.castelli.nl.messages;

import nl.messages.IMessage;

import java.util.HashMap;

public class ServerMessageManager
{
    private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

    static
    {
        messageReceivers.put((byte) 0, new CreateGroupMessage());
    }

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
