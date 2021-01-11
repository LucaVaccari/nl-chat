package it.castelli.nl.message;

import it.castelli.nl.ClientData;
import it.castelli.nl.User;
import it.castelli.nl.messages.IMessage;

public class UserIdMessage implements IMessage
{

	@Override
	public void OnReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte userId = data[2];
		User currentUser = ClientData.getInstance().getThisUser();
		ClientData.getInstance().setThisUser(new User(currentUser.getName(), currentUser.getIpAddress(), userId));
		System.out.println("User id received: " + String.valueOf(userId));
	}
}
