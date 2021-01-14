package it.castelli.nl.server.messages;

import it.castelli.nl.User;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.UsersManager;

/**
 * Interface for every class that will process an incoming message
 */
public abstract class Message
{
	/**
	 * Called when a packet is received
	 *
	 * @param data The content of the packet received
	 */
	public void onReceive(byte[] data, Connection connection)
	{
		byte userId = data[2];
		User thisUser = UsersManager.getUserFromId(userId);
		if (connection.getAdvancedUser() == null)
			connection.setUser(thisUser);
	}
}