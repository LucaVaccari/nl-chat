package it.castelli.nl.client.message;

import it.castelli.nl.client.graphics.AlertUtil;
import javafx.application.Platform;

import java.util.Arrays;

/**
 * Error message received by the client
 */
public class InformationMessage implements IMessage
{
	@Override
	public void onReceive(byte[] data)
	{
		byte[] contentOfMessage;
		contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String errorMessage = new String(contentOfMessage).strip();
		Platform.runLater(() -> AlertUtil.showInformationAlert("Error",
				"An error has occurred on the server", errorMessage));
	}
}
