package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import nl.ChatGroup;
import nl.User;
import nl.messages.IMessage;

public class GroupRemovedMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte temp = data[1];
        int groupCode = temp.intValue();

        ChatGroup groupToRemove = ClientGroupManager.getAllGroups().remove(groupCode);

    }
}
