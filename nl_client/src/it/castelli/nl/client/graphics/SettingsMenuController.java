package it.castelli.nl.client.graphics;

import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.ConnectionHandler;
import it.castelli.nl.client.Sender;
import it.castelli.nl.graphics.RGBColor;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
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

	@FXML
	public void initialize()
	{
		setIpButton.setOnAction(event -> askServerIp());

		changeUserNameButton.setOnAction(event -> {
			String newUserName = askUserName("Insert a new username");
			try
			{
				User thisUser = ClientData.getInstance().getThisUser();
				byte[] packet = MessageBuilder.buildUsernameChangeMessage(thisUser.getId(), newUserName);
				Sender.addMessageToQueue(packet);
				Sender.send();

				thisUser.setName(newUserName);

				// change the new username for all messages
				for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
				{
					ObservableList<ChatMessageComponent> chatMessageComponents =
							chatGroupComponent.getChatComponent().getMessageListView().getItems();
					for (ChatMessageComponent messageComponent : chatMessageComponents)
					{
						if (messageComponent.getChatGroupMessage().getUserSender().equals(thisUser))
						{
							messageComponent.getUserNameLabel().setText(newUserName);
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});

		// set the color of the color picker
		RGBColor userColor = ClientData.getInstance().getThisUser().getColor();
		Color color = Color.color((double) userColor.getRed() / RGBColor.MAX_COLOR_SIZE,
				(double) userColor.getGreen() / RGBColor.MAX_COLOR_SIZE,
				(double) userColor.getBlue() / RGBColor.MAX_COLOR_SIZE);
		userColorPicker.setValue(color);

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
			RGBColor rgbColor = new RGBColor((byte) (selectedColor.getRed() * RGBColor.MAX_COLOR_SIZE),
					(byte) (selectedColor.getGreen() * RGBColor.MAX_COLOR_SIZE),
					(byte) (selectedColor.getBlue() * RGBColor.MAX_COLOR_SIZE));
			try
			{
				byte[] packet = MessageBuilder.buildUserSetColorMessage(thisUser.getId(), rgbColor);
				Sender.addMessageToQueue(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			thisUser.setColor(rgbColor);

			// change the color for all messages
			for (ChatGroupComponent chatGroupComponent : FXMLController.get().chatGroupListView.getItems())
			{
				ObservableList<ChatMessageComponent> chatMessageComponents =
						chatGroupComponent.getChatComponent().getMessageListView().getItems();
				for (ChatMessageComponent messageComponent : chatMessageComponents)
				{
					if (messageComponent.getChatGroupMessage().getUserSender().equals(thisUser))
					{
						RGBColor newUserColor = thisUser.getColor();
						Color newColor = Color.color((double) newUserColor.getRed() / RGBColor.MAX_COLOR_SIZE,
								(double) newUserColor.getGreen() / RGBColor.MAX_COLOR_SIZE,
								(double) newUserColor.getBlue() / RGBColor.MAX_COLOR_SIZE);
						messageComponent.getUserNameLabel().setTextFill(newColor);
						messageComponent.getChatGroupMessage().getUserSender().setColor(newUserColor);
					}
				}
			}

			Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);
			Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
		});
	}
}
