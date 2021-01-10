package it.castelli.nl.message;

import it.castelli.nl.graphics.AlertUtil;
import nl.messages.IMessage;

import java.util.Arrays;

public class ErrorMessage implements IMessage
{
	@Override
	public void OnReceive(byte[] data)
	{
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length - 1);
		String errorMessage = new String(contentOfMessage);
		AlertUtil.showErrorAlert("Error", "An error has occurred on the server", errorMessage);
	}
}
