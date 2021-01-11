package it.castelli.nl.messages;

import java.util.Arrays;

public class ServerTestMessage implements IMessage {

    @Override
    public void OnReceive(byte[] data)
    {
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String text = new String(contentOfMessage);
        System.out.println(text);
    }
}
