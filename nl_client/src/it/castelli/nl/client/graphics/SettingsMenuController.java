package it.castelli.nl.client.graphics;

import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientReceiver;
import it.castelli.nl.client.ConnectionHandler;
import it.castelli.nl.client.NLClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * FXML controller for the settings menu
 */
public class SettingsMenuController
{
	public TextField serverIpTextField;

	@FXML
	public void initialize()
	{
		serverIpTextField.setText(ClientData.getInstance().getServerAddress().getHostAddress());

		serverIpTextField.setOnAction(event -> {
			String ipTextFieldValue = serverIpTextField.getText();
			if (ipTextFieldValue.matches("((\\d{1,3}\\.){3}\\d{1,3})|localhost"))
			{
				try
				{
					ClientData.getInstance().setServerAddress(InetAddress.getByName(ipTextFieldValue));
					ConnectionHandler.startConnection();
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				serverIpTextField.setText("");
			}
		});
	}
}
