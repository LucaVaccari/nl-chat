package nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;
import it.castelli.nl.messages.MessageBuilder;
import nl.server.ServerGroupManager;
import nl.server.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;

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
            byte[] reply = MessageBuilder.buildRemovedGroupMessage(groupCode);
            Sender.sendToClient(reply, thisUser.getIpAddress());
            Sender.sendToClient(reply, thisUser, groupToRemove);

            ServerGroupManager.getAllGroups().remove(groupCode);
        }

    }
}
