package it.castelli.nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.UsersManager;
import it.castelli.nl.ChatGroup;


public class RemoveGroupMessage implements IMessage{
    @Override
    public void OnReceive(byte[] data, Connection connection) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToRemove = GroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);
        connection.setUser(thisUser);
        if (groupToRemove.getSuperUsers().contains(thisUser))
        {
            byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
            Sender.sendToGroup(reply, groupToRemove);

            GroupManager.getAllGroups().remove(groupCode);
        }

    }
}
