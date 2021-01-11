package it.castelli.nl.messages;

import it.castelli.nl.ServerData;
import it.castelli.nl.UsersManager;
import it.castelli.nl.Sender;
import it.castelli.nl.User;
import it.castelli.nl.serialization.Serializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


public class ServerNewUserMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data) {
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id,  20 bytes for the name,
		// others for the ip

		byte[] temp = Arrays.copyOfRange(data, 3, 23 - 1);
		String name = new String(temp); //reads the new User name
		byte[] byteUserIP = Arrays.copyOfRange(data, 23, 23 + 4); //reads the ip address

		try
		{
			InetAddress userIP = InetAddress.getByAddress(byteUserIP);
			byte newId = ServerData.getInstance().getLastUserId();
			ServerData.getInstance().incrementLastUserId();

			User newUser = new User(name, userIP, newId);
			UsersManager.getAllUsers().put(newId, newUser);

			Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);

			System.out.println("new User created with name: " + name + ", IP address: " + userIP.getHostAddress() + " and userId: " + newId);

			byte[] reply = MessageBuilder.buildUserIdMessage(newId);
			Sender.sendToClient(reply, newUser.getIpAddress());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}
}
