package it.castelli.nl.client.graphics;

import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientReceiver;
import it.castelli.nl.client.ConnectionHandler;
import it.castelli.nl.client.NLClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * FXML controller for the settings menu
 */
public class SettingsMenuController {
    public Button setIpButton;
    public Button changeUserNameButton;
	public ColorPicker userColorPicker;

	@FXML
    public void initialize() {
        setIpButton.setOnAction(event -> {
            FXMLController.askServerIp();
        });

        changeUserNameButton.setOnAction(event -> {
            AlertUtil.showInformationAlert("Not implemented", "Cannot change user name", "This function is not implemented yet.");
        });

        userColorPicker.setOnAction(event -> {
            Color selectedColor = userColorPicker.getValue();
            // send message to the server
            ClientData.getInstance().getThisUser().setColor();
        });
    }
}
