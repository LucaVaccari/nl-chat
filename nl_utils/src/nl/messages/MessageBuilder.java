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

    public static byte[] buildServerUserChatMessage( byte groupCode, byte userId, String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildServerNewUserMessage(byte userId, String userName, InetAddress ipAddress) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(userId);
        outputStream.write(userName.getBytes());
        if (userName.getBytes().length != 20)
        {

        }
        String temp = ipAddress.toString();
        outputStream.write(temp.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildServerTestMessage(String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(0); // userId which is not present
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    //message from server to client


    public static byte[] buildClientNewGroupMessage(byte groupCode, String groupName) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(0); //userId which is not present
        outputStream.write(groupName.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildClientNewUserMessage(byte groupCode, byte userId, String userName) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(userName.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildRemovedGroupMessage(byte groupCode) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(0); //userId which is not present

        return outputStream.toByteArray();
    }

    public static byte[] buildUserIdMessage(byte userId) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //group code which is not present
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildClientUserChatMessage(byte groupCode, byte userId, String text) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildClientTestMessage(String text) throws IOException {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //outputStream.write(messageType);
        outputStream.write(0); //group code which is not present
        outputStream.write(0); // userId which is not present
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }
}
