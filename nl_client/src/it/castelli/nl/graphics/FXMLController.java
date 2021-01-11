package it.castelli.nl.graphics;

import it.castelli.nl.*;
import it.castelli.nl.message.ClientMessageManager;
import it.castelli.nl.messages.MessageBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

public class FXMLController
{
	private static FXMLController mainFXMLController;

	public Button createGroupButton;
	public Button joinGroupButton;
	public MenuItem settingsMenuItem; //TODO
	public MenuItem closeMenuItem;
	public MenuItem newGroupMenuItem;
	public MenuItem joinGroupMenuItem;
	public MenuItem clearGroupContentMenuItem; //TODO
	public MenuItem leaveGroupMenuItem;
	public MenuItem removeGroupMenuItem;
	public MenuItem deleteMessageMenuItem; //TODO
	public MenuItem copyMessageMenuItem; //TODO
	public MenuItem helpMenuItem;
	public ListView<ChatGroupElement> chatGroupListView; //TODO
	public Label groupNameLabel; //TODO
	public Label groupIdLabel; //TODO
	public TextField messageInputField;
	public Button sendMessageButton;

	private static ChatGroup selectedChatGroup;

	public Stage settingsStage;

	@FXML
	public void initialize()
	{
		mainFXMLController = this;

		// register screen
		if (ClientData.getInstance().getThisUser() == null)
		{
			String userName;
			do
			{
				Optional<String> name = AlertUtil
						.showTextInputDialogue("Pinco Pallino", "Welcome", "Welcome to nl-chat! Choose a user name",
						                       "Name:");

				userName = name.orElse("");

				if (name.isEmpty()) System.exit(0);
			} while (userName.length() <= 0 || userName.length() > 20);

			try
			{
				ClientData.getInstance().setThisUser(new User(userName, InetAddress.getLocalHost(), (byte) 0));
				byte[] data = MessageBuilder.buildServerNewUserMessage(userName, InetAddress.getLocalHost());
				Sender.sendToServer(data, ClientData.getInstance().getThisUser().getIpAddress());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		createGroupButton.setOnAction(this::OnCreateNewGroupButtonClick);
		joinGroupButton.setOnAction(this::OnJoinGroupButtonCLick);

		messageInputField.setOnAction(this::OnMessageSend);
		sendMessageButton.setOnAction(this::OnMessageSend);

		settingsMenuItem.setOnAction(event -> {
			settingsStage = new Stage();
			settingsStage.setTitle("Settings");
			settingsStage.setAlwaysOnTop(true);
			settingsStage.initModality(Modality.APPLICATION_MODAL);
			settingsStage.setResizable(false);
			Parent root = NLClient.loadFXML("src/it/castelli/nl/graphics/settingsMenu.fxml");
			assert root != null;
			Scene settingsScene = new Scene(root);
			settingsStage.setScene(settingsScene);
			settingsStage.showAndWait();
		});
		closeMenuItem.setOnAction(event -> {
			Stage primaryStage = NLClient.getPrimaryStage();
			primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		newGroupMenuItem.setOnAction(this::OnCreateNewGroupButtonClick);
		joinGroupMenuItem.setOnAction(this::OnJoinGroupButtonCLick);
		leaveGroupMenuItem.setOnAction(this::OnLeaveGroupButtonClick);
		removeGroupMenuItem.setOnAction(this::OnRemoveGroupButtonClick);

		helpMenuItem.setOnAction(event -> AlertUtil.showInformationAlert("Help", "If you want help...",
		                                                                 "Contact the developers if you can't " +
		                                                                 "understand."));

		chatGroupListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatGroupListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			selectedChatGroup = newValue.getChatGroup();
			// TODO show the panel with the messages
		});

		createGroupButton.setTooltip(new Tooltip(
				"Create a new chat where you can write" + " messages to other people.\n" +
				"You can invite the others by sharing the" + " group identification code."));
	}

	private void OnCreateNewGroupButtonClick(ActionEvent actionEvent)
	{
		TextInputDialog dialog = new TextInputDialog("MyGroup");
		dialog.setTitle("New group");
		dialog.setHeaderText("Choose a name");
		dialog.setContentText("Enter a name for the group\n (with less than 20 characters)");

		Optional<String> result;
		do
		{
			result = dialog.showAndWait();

			if (result.isEmpty()) return;
		} while (result.get().length() > 20);

		try
		{
			byte[] packet = MessageBuilder
					.buildCreateGroupMessage(ClientData.getInstance().getThisUser().getId(), result.get());
			Sender.sendToServer(packet, ClientData.getInstance().getServerAddress());
		}
		catch (IOException | NullPointerException e)
		{
			e.printStackTrace();
		}

		// TEMP TEMP TEMP TEMP
		System.out.println("TESTING GROUP CREATION");
		try
		{
			ClientMessageManager.getMessageReceiver(MessageBuilder.CLIENT_NEW_GROUP_MESSAGE_TYPE)
			                    .OnReceive(MessageBuilder.buildClientNewGroupMessage((byte) 3, result.get()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void OnJoinGroupButtonCLick(ActionEvent actionEvent)
	{
		Optional<String> result = AlertUtil
				.showTextInputDialogue("0", "Join group", "Insert the code of the group you want to join", "Code: ");

		if (result.isPresent())
		{
			try
			{
				byte[] packet = MessageBuilder.buildJoinGroupMessage(Byte.parseByte(result.get(), 10),
				                                                     ClientData.getInstance().getThisUser().getId());
				Sender.sendToServer(packet, ClientData.getInstance().getServerAddress());
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void OnLeaveGroupButtonClick(ActionEvent actionEvent)
	{
		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Leave group", "Are you sure?",
		                                                              "You will lose all the messages of this chat" +
		                                                              ".\nDo you really want to leave?");
		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				byte[] packet = MessageBuilder.buildLeaveGroupMessage(selectedChatGroup.getCode(),
				                                                      ClientData.getInstance().getThisUser().getId());
				Sender.sendToServer(packet, ClientData.getInstance().getServerAddress());
			}
		}
	}

	private void OnRemoveGroupButtonClick(ActionEvent actionEvent)
	{
		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Remove group", "Are you sure?",
		                                                              "You will destroy this group. All the members " +
		                                                              "will be ejected and all the messages lost.\n" +
		                                                              "Are you REALLY sure?");
		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				byte[] packet = MessageBuilder.buildRemoveGroupMessage(selectedChatGroup.getCode(),
				                                                       ClientData.getInstance().getThisUser().getId());
				Sender.sendToServer(packet, ClientData.getInstance().getServerAddress());
			}
		}
	}

	private void OnMessageSend(ActionEvent actionEvent)
	{
		String text = messageInputField.getText();

		if (!text.equals(""))
		{
			try
			{
				byte[] packet = MessageBuilder.buildServerUserChatMessage(selectedChatGroup.getCode(),
				                                                          ClientData.getInstance().getThisUser()
				                                                                    .getId(), text);
				Sender.sendToServer(packet, ClientData.getInstance().getServerAddress());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		messageInputField.setText("");
	}

	/**
	 * Singleton getter
	 *
	 * @return The only existing instance of FXMLController
	 */
	public static FXMLController get()
	{
		return mainFXMLController;
	}
}
