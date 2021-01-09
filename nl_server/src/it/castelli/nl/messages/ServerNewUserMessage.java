package it.castelli.nl.messages;

import it.castelli.nl.ServerData;
import it.castelli.nl.UsersManager;
import nl.User;
import nl.messages.IMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


public class ServerNewUserMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, 20 bytes for the name, others for the ip

        byte[] byteName = Arrays.copyOfRange(data, 3, 3 + 20 - 1);
        String name = new String(byteName);
        byte[] byteUserIP = Arrays.copyOfRange(data, 3 + 20 - 1, data.length - 1);
        try
        {
            InetAddress userIP = InetAddress.getByAddress(byteUserIP);
            byte newId = ServerData.getInstance().getLastUserId();
            User newUser = new User(name, userIP, newId);
            ServerData.getInstance().incrementLastUserId();
            UsersManager.getAllUsers().put(newId, newUser);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //userId Reply
    }
}
