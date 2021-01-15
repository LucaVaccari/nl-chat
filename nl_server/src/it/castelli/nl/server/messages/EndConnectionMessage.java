package it.castelli.nl.server.messages;

import it.castelli.nl.server.Connection;

/**
 * Message representing a client closing the connection with the server
 */
public class EndConnectionMessage extends Message
{
	@Override
	public void onReceive(byte[] data, Connection connection)
	{
		connection.interrupt();
	}
}
