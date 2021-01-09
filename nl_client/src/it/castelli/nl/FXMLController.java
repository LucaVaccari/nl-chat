package it.castelli.nl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.ChatGroup;
import nl.Sender;
import nl.messages.MessageBuilder;

import java.io.IOException;
import java.util.Optional;

public class FXMLController
{
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
	public VBox groupsVBox; //TODO
	public Label groupNameLabel; //TODO
	public Label groupIdLabel; //TODO
	public TextField messageInputField;
	public Button sendMessageButton;

	private static ChatGroup selectedChatGroup;

	@FXML
	public void initialize()
	{
		createGroupButton.setOnAction(this::OnCreateNewGroupButtonClick);
		joinGroupButton.setOnAction(this::OnJoinGroupButtonCLick);

		messageInputField.setOnAction(this::OnMessageSend);
		sendMessageButton.setOnAction(this::OnMessageSend);

		closeMenuItem.setOnAction(event -> {
			Stage primaryStage = NLClient.getPrimaryStage();
			primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		newGroupMenuItem.setOnAction(this::OnCreateNewGroupButtonClick);
		joinGroupMenuItem.setOnAction(this::OnJoinGroupButtonCLick);
		leaveGroupMenuItem.setOnAction(this::OnLeaveGroupButtonClick);
		removeGroupMenuItem.setOnAction(this::OnRemoveGroupButtonClick);

		helpMenuItem.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("If you want help...");
			alert.setContentText("Contact the developers if you can't understand.");
			alert.show();
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
			Sender.send(packet, ClientData.getInstance().getServerAddress(), Sender.SERVER_PORT);
		}
		catch (IOException | NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	private void OnJoinGroupButtonCLick(ActionEvent actionEvent)
	{
		TextInputDialog dialog = new TextInputDialog("434");
		dialog.setTitle("Join group");
		dialog.setHeaderText("Insert the code of the group you want to join");
		dialog.setContentText("Code");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent())
		{
			try
			{
				byte[] packet = MessageBuilder.buildJoinGroupMessage(Byte.parseByte(result.get(), 10),
				                                                     ClientData.getInstance().getThisUser().getId());
				Sender.send(packet, ClientData.getInstance().getServerAddress(), Sender.SERVER_PORT);
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void OnLeaveGroupButtonClick(ActionEvent actionEvent)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Leave group");
		alert.setHeaderText("Are you sure?");
		alert.setContentText("You will lose all the messages of this chat.\n" + "Do you really want to leave?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				byte[] packet = MessageBuilder.buildLeaveGroupMessage(selectedChatGroup.getCode(),
				                                                      ClientData.getInstance().getThisUser()
				                                                                .getId());
				Sender.send(packet, ClientData.getInstance().getServerAddress(), Sender.SERVER_PORT);
			}
		}
	}

	private void OnRemoveGroupButtonClick(ActionEvent actionEvent)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Remove group");
		alert.setHeaderText("Are you sure?");
		alert.setContentText("You will destroy this group. All the members will be ejected and all the messages lost.\n" +
		                     "Are you REALLY sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				byte[] packet = MessageBuilder.buildRemoveGroupMessage(selectedChatGroup.getCode(),
				                                                      ClientData.getInstance().getThisUser()
				                                                                .getId());
				Sender.send(packet, ClientData.getInstance().getServerAddress(), Sender.SERVER_PORT);
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
				byte[] packet = MessageBuilder.buildServerUserChatMessage(
						selectedChatGroup.getCode(),
						ClientData.getInstance().getThisUser().getId(),
						text
				);
				Sender.send(packet, ClientData.getInstance().getServerAddress(), Sender.SERVER_PORT);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		messageInputField.setText("");
	}
}
