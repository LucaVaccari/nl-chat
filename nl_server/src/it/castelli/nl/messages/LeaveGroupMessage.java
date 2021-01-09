package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import nl.ChatGroup;
import nl.Sender;
import nl.User;
import nl.messages.IMessage;
import nl.messages.MessageBuilder;

public class LeaveGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToLeave = ServerGroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);
        groupToLeave.getUsers().remove(thisUser);
        groupToLeave.getSuperUsers().remove(thisUser);


        byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
        Sender.send(reply, UsersManager.getUserFromId(userId).getIpAddress(), Sender.CLIENT_PORT);
    }
}
