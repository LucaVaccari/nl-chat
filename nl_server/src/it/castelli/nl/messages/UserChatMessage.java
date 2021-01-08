package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import nl.ChatGroup;
import nl.Sender;
import nl.User;
import nl.messages.IMessage;

import java.util.Arrays;

public class UserChatMessage implements IMessage
{
    @Override
    public void OnReceive(byte[] data)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        byte groupCode = data[1];
        byte userId = data[2];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String textMessage = new String(contentOfMessage);
        ChatGroup thisGroup = ServerGroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);

        if (thisGroup.getUsers().contains(thisUser))
        {
            Sender.send(data, thisUser, thisGroup);
        }

    }
}
