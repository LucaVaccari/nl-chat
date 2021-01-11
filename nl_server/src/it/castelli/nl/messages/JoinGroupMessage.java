package it.castelli.nl.messages;

import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;
import it.castelli.nl.User;

import java.io.IOException;

public class JoinGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToJoin = ServerGroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);

        if(!groupToJoin.getUsers().contains(thisUser)) groupToJoin.getUsers().add(thisUser);

        if(groupToJoin == null)
        {
            try {
                String groupCodeString = String.valueOf(groupCode.intValue());
                byte[] errorReply = MessageBuilder.buildErrorMessage("The group with code: " + groupCodeString + " does not exist.");
                Sender.sendToClient(errorReply, UsersManager.getUserFromId(userId).getIpAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                //send group
                byte[] reply = MessageBuilder.buildClientNewGroupMessage(groupCode, groupToJoin.getName());
                Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());

                //send Users who are in the group to the new one
                for (User user : groupToJoin.getUsers())
                {
                    if (user == thisUser)
                    {
                        reply = MessageBuilder.buildClientNewUserMessage(groupCode, user.getId(), user.getName());
                        Sender.sendToClient(reply, user, groupToJoin);
                        Sender.sendToClient(reply,UsersManager.getUserFromId(userId).getIpAddress());
                    }
                    else
                    {
                        reply = MessageBuilder.buildClientNewUserMessage(groupCode, user.getId(), user.getName());
                        Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
