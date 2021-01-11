package it.castelli.nl.message;

import it.castelli.nl.ClientData;
import it.castelli.nl.ClientGroupManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;
import it.castelli.nl.serialization.Serializer;

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

        Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
        Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);

        thisGroup.getUsers().add(newUser);
    }
}
