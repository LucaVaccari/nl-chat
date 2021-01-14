package it.castelli.nl.server.messages;


import it.castelli.nl.User;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.UsersManager;

import java.util.Arrays;

public class TestMessage implements IMessage {

    @Override
    public void OnReceive(byte[] data, Connection connection)
    {
        byte userId = data[2];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        User thisUser = UsersManager.getUserFromId(userId);
        connection.setUser(thisUser);
        String text = new String(contentOfMessage);
        System.out.println(text);
    }
}
