package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import nl.ChatGroup;
import nl.User;
import nl.messages.IMessage;

import java.util.Arrays;

public class ClientNewUserMessage implements IMessage {

    @Override
    public void OnReceive(byte[] data) {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        Byte userId = data[2];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String userName = new String(contentOfMessage);
        ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
        User newUser = new User(userName, userId);

        thisGroup.getUsers().add(newUser);
    }
}
