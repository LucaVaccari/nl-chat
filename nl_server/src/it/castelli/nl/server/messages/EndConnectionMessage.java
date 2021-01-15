package it.castelli.nl.server.messages;

import it.castelli.nl.server.Connection;

/**
 * Message representing a client closing the connection with the server
 */
public class EndConnectionMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		connection.interrupt();
		System.out.println("Connection with " + connection.getAdvancedUser().getUser().getName() + " ended");
	}
}
