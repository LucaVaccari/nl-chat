package it.castelli.nl.message;

import nl.messages.IMessage;

import java.util.Arrays;

public class ErrorMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data)
    {
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String errorMessage = new String(contentOfMessage);
        //error window todo
    }
}
