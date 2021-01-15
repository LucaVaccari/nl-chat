package it.castelli.nl.server.messages;

import it.castelli.nl.server.Connection;
import it.castelli.nl.server.UserManager;

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
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		byte userId = data[2];
		UserManager.AdvancedUser thisUser = UserManager.getAllUsers().get(userId);
		if (connection.getAdvancedUser() == null)
			connection.setUser(thisUser);
	}
}