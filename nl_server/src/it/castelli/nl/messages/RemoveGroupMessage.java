package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;
import it.castelli.nl.User;

public class RemoveGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToRemove = ServerGroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);
        if (groupToRemove.getSuperUsers().contains(thisUser))
        {
            ServerGroupManager.getAllGroups().remove(groupCode);
        }

        byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
        Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());

    }
}
