package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import nl.ChatGroup;
import nl.messages.IMessage;
import java.util.Arrays;

import static it.castelli.nl.ServerGroupManager.lastGroupCode;

public class CreateGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {


        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others
        Byte temp = data[2];
        int userId = temp.intValue();
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String newGroupName = new String(contentOfMessage);

        ChatGroup newGroup = new ChatGroup(newGroupName);
        newGroup.getUsers().add(UsersManager.getUserFromId(userId));
        ServerGroupManager.getAllGroups().put(lastGroupCode++, newGroup);

        //GroupReply with group code and name of the group
    }
}
