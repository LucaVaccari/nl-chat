package it.castelli.nl.message;

import it.castelli.nl.graphics.AlertUtil;
import it.castelli.nl.messages.IMessage;
import javafx.application.Platform;

/**
 * Test message received by the client
 */
public class ClientTestMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		System.out.println("A message has been received from the server");
		Platform.runLater(() -> AlertUtil.showInformationAlert("Server", "Test message from the server", "A message " +
		                                                                                                 "has been " +
		                                                                                                 "sent."));
	}
}
