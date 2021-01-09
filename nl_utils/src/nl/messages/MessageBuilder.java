package nl.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class MessageBuilder
{
    //message types constants


    //message from client to server

    public static byte[] buildCreateGroupMessage(byte userId, String name) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(userId);
        outputStream.write(name.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildJoinGroupMessage( byte groupToJoinCode, byte userId) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupToJoinCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildLeaveGroupMessage( byte groupToLeaveCode, byte userId) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupToLeaveCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildRemoveGroupMessage( byte groupToRemoveCode, byte userId) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupToRemoveCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildUserChatMessage( byte groupCode, byte userId, String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildNewUserMessage(byte userId, InetAddress ipAddress) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(userId);
        String text = ipAddress.toString();
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    //message from server to client


}
