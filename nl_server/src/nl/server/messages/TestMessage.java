package nl.server.messages;

import it.castelli.nl.messages.IMessage;

import java.util.Arrays;

public class TestMessage implements IMessage {

    @Override
    public void OnReceive(byte[] data)
    {
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String text = new String(contentOfMessage);
        System.out.println(text);
    }
}
