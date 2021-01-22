package it.castelli.nl.client.graphics;

import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.Sender;
import it.castelli.nl.messages.MessageBuilder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * FXML controller for the settings menu
 */
public class SettingsMenuController
{
	public Button setIpButton;
	public Button changeUserNameButton;
	public ColorPicker userColorPicker;

	@FXML
	public void initialize()
	{
		setIpButton.setOnAction(event -> {
			FXMLController.askServerIp();
		});

		changeUserNameButton.setOnAction(event -> {
			AlertUtil.showInformationAlert("Not implemented", "Cannot change user name",
			                               "This function is not implemented yet.");
		});

		userColorPicker.setOnAction(event -> {

			User thisUser = ClientData.getInstance().getThisUser();

			if (thisUser.getId() <= 0)
			{
				AlertUtil.showErrorAlert("Error", "You are not registered",
				                         "You have not been registered. Wait until the server starts and your " +
				                         "registration will perform automatically.");
				return;
			}

			Color selectedColor = userColorPicker.getValue();

			String hexColor = String.format("#%02X%02X%02X",
			                                (int) (selectedColor.getRed() * 255),
			                                (int) (selectedColor.getGreen() * 255),
			                                (int) (selectedColor.getBlue() * 255));
			try
			{
				byte[] packet = MessageBuilder.buildUserSetColorMessage(thisUser.getId(), hexColor);
				Sender.addMessageToQueue(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			thisUser.setColor(hexColor);

			// change the color for all messages
			for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
			{
				ObservableList<ChatMessageComponent> chatMessageComponents =
						chatGroupComponent.getChatComponent().getMessageListView().getItems();
				for (ChatMessageComponent messageComponent : chatMessageComponents)
				{
					String userColor = thisUser.getColor();
					messageComponent.getMessageLabel().setTextFill(Color.web(userColor));
				}
			}
		});
	}
}
