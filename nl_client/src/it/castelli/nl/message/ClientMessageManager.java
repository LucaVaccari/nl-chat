package it.castelli.nl.message;

import nl.messages.IMessage;

import java.util.HashMap;

public class ClientMessageManager
{
    private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

    static
    {
        messageReceivers.put((byte) 0, new GroupReply());
    }

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
