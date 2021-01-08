package it.castelli.nl.message;

import it.castelli.nl.ClientGroupManager;
import nl.ChatGroup;

import nl.User;
import nl.messages.IMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UserChatMessage implements IMessage
{
    @Override
    public void OnReceive(byte[] data)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte temp = data[1];
        int groupCode = temp.intValue();
        temp = data[2];
        int userId = temp.intValue();
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String textMessage = new String(contentOfMessage);
        ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
        String senderName;

        for (User user : thisGroup.getUsers()) {
            if (user.getId() == userId)
            {
                senderName = user.getName();
            }
        }


        

    }
}
