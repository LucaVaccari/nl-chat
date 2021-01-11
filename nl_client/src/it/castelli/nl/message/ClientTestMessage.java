package it.castelli.nl.message;

import it.castelli.nl.graphics.AlertUtil;
import it.castelli.nl.messages.IMessage;

public class ClientTestMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		System.out.println("è stato ricevuto un messaggio di test Client");
		AlertUtil.showInformationAlert("Server", "Test message from the server", "A message has been sent.");
	}
}
