package it.castelli.nl.messages;

import it.castelli.nl.ServerData;
import it.castelli.nl.ServerGroupManager;
import it.castelli.nl.UsersManager;
import nl.ChatGroup;
import nl.Sender;
import nl.messages.IMessage;
import nl.messages.MessageBuilder;

import java.io.IOException;
import java.util.Arrays;

import static it.castelli.nl.ServerGroupManager.lastGroupCode;

public class CreateGroupMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {


        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others
        Byte userId = data[2];
        byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
        String newGroupName = new String(contentOfMessage);

        byte newGroupCode = ServerData.getInstance().getLastGroupCode();
        ChatGroup newGroup = new ChatGroup(newGroupName, newGroupCode);
        newGroup.getUsers().add(UsersManager.getUserFromId(userId));
        newGroup.getSuperUsers().add(UsersManager.getUserFromId(userId));
        ServerGroupManager.getAllGroups().put(newGroupCode, newGroup);
        ServerData.getInstance().incrementLastGroupCode();

        try {
            byte[] reply = MessageBuilder.buildClientNewGroupMessage(newGroup.getCode(), newGroup.getName());
            Sender.sendToClient(reply, UsersManager.getUserFromId(userId).getIpAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
