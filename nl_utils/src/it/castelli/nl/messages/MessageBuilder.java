package it.castelli.nl.messages;

import it.castelli.nl.ChatGroupMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * Provides useful methods and constants to build packets to send either to the client or to the server
 */
public class MessageBuilder
{
	//message types constants

	public static final byte CREATE_GROUP_MESSAGE_TYPE = 1;
	public static final byte JOIN_GROUP_MESSAGE_TYPE = 2;
	public static final byte LEAVE_GROUP_MESSAGE_TYPE = 3;
	public static final byte REMOVE_GROUP_MESSAGE_TYPE = 4;
	public static final byte SERVER_USER_CHAT_MESSAGE_TYPE = 5;
	public static final byte SERVER_NEW_USER_MESSAGE_TYPE = 6;
	public static final byte SERVER_TEST_MESSAGE_TYPE = 7;
	public static final byte SERVER_END_CONNECTION_MESSAGE_TYPE = 8;

	public static final byte CLIENT_NEW_GROUP_MESSAGE_TYPE = 9;
	public static final byte CLIENT_NEW_USER_MESSAGE_TYPE = 10;
	public static final byte GROUP_REMOVED_MESSAGE_TYPE = 11;
	public static final byte USER_ID_MESSAGE_TYPE = 12;
	public static final byte CLIENT_USER_CHAT_MESSAGE_TYPE = 13;
	public static final byte ERROR_MESSAGE_TYPE = 14;
	public static final byte CLIENT_TEST_MESSAGE_TYPE = 15;
	public static final byte USER_LEFT_MESSAGE_TYPE = 16;

	public static final int PACKET_SIZE = 1024;

	//messages from client to server

	/**
	 * Build a packet which contains a request to the server to create a new group
	 *
	 * @param userId The user creating the group
	 * @param name   The name of the group
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to create the packet
	 */
	public static byte[] buildCreateGroupMessage(byte userId, String name) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(CREATE_GROUP_MESSAGE_TYPE);
		outputStream.write((byte) 0); //groupCode which is not present
		outputStream.write(userId);
		outputStream.write(name.getBytes());

		System.out.println("Created CreateGroupMessage with group name: " + name);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a request to the server to join a group
	 *
	 * @param groupToJoinCode The code of the group to join
	 * @param userId          The id of the user who wants to enter the group
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildJoinGroupMessage(byte groupToJoinCode, byte userId) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(JOIN_GROUP_MESSAGE_TYPE);
		outputStream.write(groupToJoinCode);
		outputStream.write(userId);
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a request to the server to leave a group
	 *
	 * @param groupToLeaveCode The code of the group to leave
	 * @param userId           The if of the user who wants to leave the group
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildLeaveGroupMessage(byte groupToLeaveCode, byte userId) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(LEAVE_GROUP_MESSAGE_TYPE);
		outputStream.write(groupToLeaveCode);
		outputStream.write(userId);
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a request to the server to completely remove a group
	 *
	 * @param groupToRemoveCode The code of the group to be removed
	 * @param userId            The id of the user deleting the group
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildRemoveGroupMessage(byte groupToRemoveCode, byte userId) throws IOException {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(REMOVE_GROUP_MESSAGE_TYPE);
		outputStream.write(groupToRemoveCode);
		outputStream.write(userId);
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a user chat message sent by the client to the server
	 *
	 * @param message The user chat message to be sent
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to create the packet
	 */
	public static byte[] buildServerUserChatMessage(ChatGroupMessage message) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(SERVER_USER_CHAT_MESSAGE_TYPE);
		outputStream.write(message.getChatGroup().getCode());
		outputStream.write(message.getUserSender().getId());
		outputStream.write(message.getMessageContent().getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a request to register a new user
	 *
	 * @param userName  The name of the new user who wants to register
	 * @param ipAddress The address of the user who wants to register
	 * @return The array of data to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildServerNewUserMessage(String userName, InetAddress ipAddress)
			throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, 20 for the name, others
		// for the ip

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(SERVER_NEW_USER_MESSAGE_TYPE);
		outputStream.write((byte) 0); //groupCode which is not present
		outputStream.write((byte) 0);
		outputStream.write(userName.getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		System.out.println("Created NewUserMessage with user name: " + userName);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a test message to be sent from the client to the server
	 *
	 * @param test An optional test content (String)
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildServerTestMessage(String test, byte userId) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(SERVER_TEST_MESSAGE_TYPE);
		outputStream.write((byte) 0); //groupCode which is not present
		outputStream.write(userId);
		outputStream.write(test.getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	public static byte[] buildServerEndConnectionMessage() throws IOException {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(0);
		outputStream.write(SERVER_END_CONNECTION_MESSAGE_TYPE);
		outputStream.write((byte) 0); //groupCode which is not present
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	//message from the server to the client

	/**
	 * Build a packet which sent when a new group is created
	 *
	 * @param groupCode The code of the just created group
	 * @param groupName The name of the just created group
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildClientNewGroupMessage(byte groupCode, String groupName) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(0);
		outputStream.write(CLIENT_NEW_GROUP_MESSAGE_TYPE);
		outputStream.write(groupCode);
		outputStream.write((byte) 0); //userId which is not present
		groupName = groupName.replaceAll("\0", "");
		outputStream.write(groupName.strip().getBytes());
		outputStream.write(new byte[PACKET_SIZE - groupName.length() - 3]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a message to the client that a new user has been added to a group
	 *
	 * @param groupCode The code of the group the new user has just joined
	 * @param userId    The id of the user who just joined the group
	 * @param userName  The name of the user who just joined the group
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildClientNewUserMessage(byte groupCode, byte userId, String userName) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(CLIENT_NEW_USER_MESSAGE_TYPE);
		outputStream.write(groupCode);
		outputStream.write(userId);
		outputStream.write(userName.getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet containing a message that a group has been completely removed
	 *
	 * @param groupCode The code of the group just deleted
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildRemovedGroupMessage(byte groupCode) throws IOException {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(GROUP_REMOVED_MESSAGE_TYPE);
		outputStream.write(groupCode);
		outputStream.write((byte) 0); //userId which is not present
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains the id of a just registered user.
	 *
	 * @param userId The id of the just registered user
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildUserIdMessage(byte userId) throws IOException {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(USER_ID_MESSAGE_TYPE);
		outputStream.write((byte) 0); //group code which is not present
		outputStream.write(userId);
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		System.out.println("Creating a message of type: UserIdMessage with user id = " + userId);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a user message to be sent to all users of a group.
	 *
	 * @param message The user chat message to be sent
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildClientUserChatMessage(ChatGroupMessage message) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(CLIENT_USER_CHAT_MESSAGE_TYPE);
		outputStream.write(message.getChatGroup().getCode());
		outputStream.write(message.getUserSender().getId());
		outputStream.write(message.getMessageContent().getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains an generic error to be communicated to the client
	 *
	 * @param error The error encountered
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildErrorMessage(String error) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(ERROR_MESSAGE_TYPE);
		outputStream.write((byte) 0); //group code which is not present
		outputStream.write((byte) 0); // userId which is not present
		outputStream.write(error.getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * Build a packet which contains a test message to be sent from the server to the client
	 *
	 * @param text The optional text of the test message
	 * @return The array of bytes to be sent
	 * @throws IOException Thrown when failing to build the packet
	 */
	public static byte[] buildClientTestMessage(String text) throws IOException
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(CLIENT_TEST_MESSAGE_TYPE);
		outputStream.write((byte) 0); //group code which is not present
		outputStream.write((byte) 0); // userId which is not present
		outputStream.write(text.getBytes());
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

	/**
	 * @param userId    Id of the user who left
	 * @param groupCode Group the user left
	 * @return The array of bytes to be sent
	 */
	public static byte[] buildUserLeftMessage(byte userId, byte groupCode) throws IOException {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(USER_LEFT_MESSAGE_TYPE);
		outputStream.write(groupCode); //group code which is not present
		outputStream.write(userId); // userId which is not present
		outputStream.write(new byte[PACKET_SIZE - outputStream.size()]);

		return outputStream.toByteArray();
	}

}
