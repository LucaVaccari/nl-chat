package it.castelli.nl.server.messages;


import it.castelli.nl.server.Connection;

import java.util.Arrays;

public class TestMessage extends Message
{

	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
		String text = new String(contentOfMessage).strip();
		System.out.println(text);
	}
}
