package it.castelli.nl.client.graphics;

import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ConnectionHandler;
import it.castelli.nl.client.Sender;
import it.castelli.nl.messages.MessageBuilder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

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
			askServerIp();
		});

		changeUserNameButton.setOnAction(event -> {
			String newUserName = askUserName("Insert a new username");
			try
			{
				byte[] packet = MessageBuilder.buildUsernameChangeMessage(ClientData.getInstance().getThisUser().getId(), newUserName);
				Sender.addMessageToQueue(packet);
				Sender.send();

				ClientData.getInstance().getThisUser().setName(newUserName);

				// change the new username for all messages
				for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
				{
					ObservableList<ChatMessageComponent> chatMessageComponents =
							chatGroupComponent.getChatComponent().getMessageListView().getItems();
					for (ChatMessageComponent messageComponent : chatMessageComponents)
					{
						messageComponent.getUserNameLabel().setText(newUserName);
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
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
					messageComponent.getUserNameLabel().setTextFill(Color.web(userColor));
				}
			}
		});
	}

	/**
	 * Ask for the server IP and tries to connect
	 */
	public static void askServerIp()
	{
		String hostAddress = ClientData.getInstance().getServerAddress().getHostAddress();
		if (hostAddress == null) hostAddress = "";

		Optional<String> ipText;
		boolean askAgain;
		do
		{
			askAgain = false;
			ipText = AlertUtil.showTextInputDialogue(hostAddress, "Server IP", "Insert the IP of the " +
					"server", "Server IP: ");
			if (ipText.isEmpty())
			{
				AlertUtil.showInformationAlert("Insert address", "Address is mandatory", "Pleas insert an address");
				askAgain = true;
				continue;
			}

			if (!ipText.get().matches("((\\d{1,3}\\.){3}\\d{1,3})|localhost"))
			{
				AlertUtil.showInformationAlert("Invalid address", "Wrong syntax", "An address is identified by 4 " +
						"numbers under 255 separated by " +
						"dots (.).\nIf the server is on " +
						"your machine you can write " +
						"'localhost'");
				askAgain = true;
			}
		} while (askAgain);

		try
		{
			ClientData.getInstance().setServerAddress(InetAddress.getByName(ipText.get()));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		ConnectionHandler.startConnection();
	}

	public static String askUserName(String headerText)
	{
		// register screen
		boolean askAgain;
		String userName;
		do
		{
			askAgain = false;
			Optional<String> name = AlertUtil
					.showTextInputDialogue("Pinco Pallino", "Set username", headerText,
							"Username: ");

			userName = name.orElse("");

			// if the user pressed the "cancel" button, exit the application
			if (name.isEmpty()) System.exit(0);

			userName = userName.strip();

			if (userName.length() <= 0 || userName.length() > 20)
			{
				askAgain = true;
				AlertUtil.showErrorAlert("Invalid name", "Check the size of your username",
						"The size should be between 0 and 20");
			}
			if (!userName.matches("[\\w\\s]+[\\d\\s]*"))
			{
				askAgain = true;
				AlertUtil.showErrorAlert("Invalid name", "The name uses an invalid format",
						"The name must start with a letter and could be followed by digits");
			}
		} while (askAgain);

		return userName;
	}
}
