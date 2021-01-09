package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import nl.ChatGroup;
import nl.User;
import nl.messages.IMessage;

import java.util.Arrays;

public class ClientNewGroupMessage implements IMessage {

    @Override
    public void OnReceive(byte[] data) {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String groupName = new String(contentOfMessage);
        ChatGroup newGroup = new ChatGroup(groupName, groupCode);
        ClientGroupManager.getAllGroups().put(groupCode, newGroup); 

    }
}