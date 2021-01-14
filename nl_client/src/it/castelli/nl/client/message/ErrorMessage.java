package it.castelli.nl.client.message;

import it.castelli.nl.client.graphics.AlertUtil;

import java.util.Arrays;

/**
 * Error message received by the client
 */
public class ErrorMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String errorMessage = new String(contentOfMessage);
		AlertUtil.showErrorAlert("Error", "An error has occurred on the server", errorMessage);
	}
}