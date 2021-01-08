package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import nl.ChatGroup;
import nl.User;
import nl.messages.IMessage;

public class JoinGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToJoin = ServerGroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);
        groupToJoin.getUsers().add(thisUser);

        //GroupReply with group code and name of the group
    }
}
