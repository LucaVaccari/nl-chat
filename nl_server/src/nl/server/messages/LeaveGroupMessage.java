package nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;
import it.castelli.nl.messages.MessageBuilder;
import nl.server.GroupManager;
import nl.server.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;

public class LeaveGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToLeave = GroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);
        groupToLeave.getUsers().remove(thisUser);
        groupToLeave.getSuperUsers().remove(thisUser);


        byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
        Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());
    }
}
