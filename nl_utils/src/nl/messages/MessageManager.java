package nl.messages;

import java.util.HashMap;

public class MessageManager
{
    private static final HashMap<Byte, IMessage> messageReceivers = new HashMap<>();

    static
    {
        //initialize the HashMap with message classes instances and relative id codes
    }

    public static IMessage getMessageReceiver(byte messageType)
    {
        return messageReceivers.get(messageType);
    }
}
