package nl;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Util class that provides methods and constants to communicate between the server and the clients (using UDP)
 */
public class Sender
{
	public static final int SERVER_SEND_PORT = 3400;
	public static final int SERVER_RECEIVE_PORT = 3401;
	public static final int CLIENT_SEND_PORT = 3500;
	public static final int CLIENT_RECEIVE_PORT = 3501;

	/**
	 * Send a packet over the network
	 *
	 * @param data            The data to be sent
	 * @param ipAddress       The address to be sent to
	 * @param sourcePort      The port from which to send the packet
	 * @param destinationPort The port to which to send the packet
	 */
	private static void send(byte[] data, InetAddress ipAddress, int sourcePort, int destinationPort)
	{
		try (DatagramSocket socket = new DatagramSocket(sourcePort))
		{
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, destinationPort);
			socket.send(packet);

		}
		catch (BindException i)
		{
			System.out.println("");
			i.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to all the users' clients in the group except for the sending one
	 * (used only by the server, should not be here)
	 *
	 * @param data            The data to be sent
	 * @param senderUser      The user which client is sending the message
	 * @param group           The group containing all the users which clients will receive the packets
	 * @param sourcePort      The source port
	 * @param destinationPort The destination port
	 */
	private static void send(byte[] data, User senderUser, ChatGroup group, int sourcePort, int destinationPort)
	{
		group.getUsers().forEach((user -> {
			if (user.getIpAddress() != senderUser.getIpAddress())
			{
				try
				{
					DatagramSocket socket = new DatagramSocket(sourcePort);
					DatagramPacket packet = new DatagramPacket(data, data.length, user.getIpAddress(),
					                                           destinationPort);
					socket.send(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}));
	}

	/**
	 * Send a message from the server to the client
	 *
	 * @param data      The packet to be sent
	 * @param ipAddress The IP address to be sent to
	 */
	public static void sendToClient(byte[] data, InetAddress ipAddress)
	{
		send(data, ipAddress, SERVER_SEND_PORT, CLIENT_RECEIVE_PORT);
	}

	/**
	 * Send a message from the server to all the clients of the users of a ChatGroup, except to the client of the
	 * sender user (used only by the server, should not be here)
	 *
	 * @param data The packet to be sent
	 * @param senderUser The user which client will be ignored when sending
	 * @param group The id of the group which users' clients will receive the message
	 */
	public static void sendToClient(byte[] data, User senderUser, ChatGroup group)
	{
		send(data, senderUser, group, SERVER_SEND_PORT, CLIENT_RECEIVE_PORT);
	}

	/**
	 * Send a message from the client to the server
	 * @param data The packet to be sent
	 * @param ipAddress The IP address of the server
	 */
	public static void sendToServer(byte[] data, InetAddress ipAddress)
	{
		send(data, ipAddress, CLIENT_SEND_PORT, SERVER_RECEIVE_PORT);
	}
}


