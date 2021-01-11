package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import it.castelli.nl.ChatGroup;

import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;

import java.util.Arrays;

public class ClientUserChatMessage implements IMessage
{
    @Override
    public void OnReceive(byte[] data)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        Byte userId = data[2];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String textMessage = new String(contentOfMessage);
        ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
        String senderName = "";

        for (User user : thisGroup.getUsers()) {
            if (user.getId() == userId)
            {
                senderName = user.getName();
            }
        }
        if (senderName == "") senderName = "Stranger";
        String message = senderName + " > " + textMessage;
        thisGroup.getChatGroupContent().getUserMessages().add(message);

    }
}
