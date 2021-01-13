package nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;
import it.castelli.nl.messages.MessageBuilder;
import nl.server.AdvancedUser;
import nl.server.GroupManager;
import nl.server.UsersManager;
import it.castelli.nl.ChatGroup;
import it.castelli.nl.Sender;

import java.io.IOException;

public class JoinGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToJoin = GroupManager.getGroupFromCode(groupCode);
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
                    reply = MessageBuilder.buildClientNewUserMessage(groupCode, user.getId(), user.getName());
                    if (user == thisUser)
                    {
                        Sender.sendToClient(reply, user, groupToJoin);
                    }
                    else
                    {
                        Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
