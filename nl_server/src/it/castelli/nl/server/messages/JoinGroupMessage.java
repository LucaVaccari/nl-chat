package it.castelli.nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;
import it.castelli.nl.server.UsersManager;
import it.castelli.nl.ChatGroup;

import java.io.IOException;

public class JoinGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data, Connection connection) {

        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        Byte groupCode = data[1];
        byte userId = data[2];
        ChatGroup groupToJoin = GroupManager.getGroupFromCode(groupCode);
        User thisUser = UsersManager.getUserFromId(userId);



        if(groupToJoin == null)
        {
            //the group doesn't exist -> error message
            try
            {
                String groupCodeString = String.valueOf(groupCode.intValue());
                byte[] errorReply = MessageBuilder.buildErrorMessage("The group with code: " + groupCodeString + " does not exist.");
                Sender.sendToUser(errorReply, thisUser);
            }
            catch (IOException e)
            {
                System.out.println("IOException in " + this.toString() + " during the error reply creation");
                e.printStackTrace();
            }
        }
        else
        {
            //the group exists

            //the user is added to the group
            groupToJoin.getUsers().add(thisUser);

            //communication to inform others that a new User joined the group and transmission of the group to the user
            try
            {
                //send group
                byte[] reply = MessageBuilder.buildClientNewGroupMessage(groupCode, groupToJoin.getName());
                Sender.sendToUser(reply, thisUser);

                //sends Users who are in the group to the new one and sends the new one to others
                for (User user : groupToJoin.getUsers())
                {
                    reply = MessageBuilder.buildClientNewUserMessage(groupCode, user.getId(), user.getName());
                    if (user == thisUser)
                        Sender.sendToOthersInGroup(reply, thisUser, groupToJoin);
                    else
                        Sender.sendToUser(reply, thisUser);
                }
            }
            catch (IOException e)
            {
                System.out.println("IOException in " + this.toString() + " during reply creation");
                e.printStackTrace();
            }
        }
    }
}
