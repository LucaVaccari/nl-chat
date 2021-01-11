package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import it.castelli.nl.messages.IMessage;

public class GroupRemovedMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        ClientGroupManager.getAllGroups().remove(groupCode);

    }
}
