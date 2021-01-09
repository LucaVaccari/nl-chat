package nl.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class MessageBuilder
{
    //message types constants

    public static final byte CREATE_GROUP_MESSAGE_TYPE = 0;
    public static final byte JOIN_GROUP_MESSAGE_TYPE = 1;
    public static final byte LEAVE_GROUP_MESSAGE_TYPE = 2;
    public static final byte REMOVE_GROUP_MESSAGE_TYPE = 3;
    public static final byte SERVER_USER_CHAT_MESSAGE_TYPE = 4;
    public static final byte SERVER_NEW_USER_MESSAGE_TYPE = 5;
    public static final byte SERVER_TEST_MESSAGE_TYPE = 6;

    public static final byte CLIENT_NEW_GROUP_MESSAGE_TYPE = 7;
    public static final byte CLIENT_NEW_USER_MESSAGE_TYPE = 8;
    public static final byte GROUP_REMOVED_MESSAGE_TYPE = 9;
    public static final byte USER_ID_MESSAGE_TYPE = 10;
    public static final byte CLIENT_USER_CHAT_MESSAGE_TYPE = 11;
    public static final byte ERROR_MESSAGE_TYPE = 12;
    public static final byte CLIENT_TEST_MESSAGE_TYPE = 13;


    //message from client to server

    /**
     * Send a request to the server to create a new group
     * @param userId The user creating the group
     * @param name The name of the group
     * @return The array of bytes to be sent
     * @throws IOException Thrown when failing to create the packet
     */
    public static byte[] buildCreateGroupMessage(byte userId, String name) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CREATE_GROUP_MESSAGE_TYPE);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(userId);
        outputStream.write(name.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildJoinGroupMessage( byte groupToJoinCode, byte userId)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(JOIN_GROUP_MESSAGE_TYPE);
        outputStream.write(groupToJoinCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildLeaveGroupMessage( byte groupToLeaveCode, byte userId)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(LEAVE_GROUP_MESSAGE_TYPE);
        outputStream.write(groupToLeaveCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildRemoveGroupMessage( byte groupToRemoveCode, byte userId)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(REMOVE_GROUP_MESSAGE_TYPE);
        outputStream.write(groupToRemoveCode);
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildServerUserChatMessage( byte groupCode, byte userId, String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(SERVER_USER_CHAT_MESSAGE_TYPE);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildServerNewUserMessage(byte userId, String userName, InetAddress ipAddress) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, 20 for the name, others for the ip

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(SERVER_NEW_USER_MESSAGE_TYPE);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(userId);
        outputStream.write(userName.getBytes());

        while(outputStream.size() != 23)
        {
            outputStream.write(0);
        }

        String temp = ipAddress.toString();
        outputStream.write(temp.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildServerTestMessage(String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(SERVER_TEST_MESSAGE_TYPE);
        outputStream.write(0); //groupCode which is not present
        outputStream.write(0); // userId which is not present
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    //message from server to client

    public static byte[] buildClientNewGroupMessage(byte groupCode, String groupName) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CLIENT_NEW_GROUP_MESSAGE_TYPE);
        outputStream.write(groupCode);
        outputStream.write(0); //userId which is not present
        outputStream.write(groupName.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildClientNewUserMessage(byte groupCode, byte userId, String userName) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CLIENT_NEW_USER_MESSAGE_TYPE);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(userName.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildRemovedGroupMessage(byte groupCode)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(GROUP_REMOVED_MESSAGE_TYPE);
        outputStream.write(groupCode);
        outputStream.write(0); //userId which is not present

        return outputStream.toByteArray();
    }

    public static byte[] buildUserIdMessage(byte userId)
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(USER_ID_MESSAGE_TYPE);
        outputStream.write(0); //group code which is not present
        outputStream.write(userId);

        return outputStream.toByteArray();
    }

    public static byte[] buildClientUserChatMessage(byte groupCode, byte userId, String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CLIENT_USER_CHAT_MESSAGE_TYPE);
        outputStream.write(groupCode);
        outputStream.write(userId);
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildErrorMessage(String error) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ERROR_MESSAGE_TYPE);
        outputStream.write(0); //group code which is not present
        outputStream.write(0); // userId which is not present
        outputStream.write(error.getBytes());

        return outputStream.toByteArray();
    }

    public static byte[] buildClientTestMessage(String text) throws IOException
    {
        // syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CLIENT_TEST_MESSAGE_TYPE);
        outputStream.write(0); //group code which is not present
        outputStream.write(0); // userId which is not present
        outputStream.write(text.getBytes());

        return outputStream.toByteArray();
    }

}
