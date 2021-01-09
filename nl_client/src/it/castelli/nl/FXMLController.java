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
	public MenuItem removeGroupMenuItem; //TODO
	public MenuItem deleteMessageMenuItem; //TODO
	public MenuItem copyMessageMenuItem; //TODO
	public MenuItem helpMenuItem;
	public VBox chatsVBox; //TODO
	public Label groupNameLabel; //TODO
	public Label groupIdLabel; //TODO
	public TextField messageInputField; //TODO

	private static ChatGroup selectedChatGroup;

	@FXML
	public void initialize()
	{
		createGroupButton.setOnAction(FXMLController::OnCreateNewGroupButtonClick);
		joinGroupButton.setOnAction(FXMLController::OnJoinGroupButtonCLick);

		closeMenuItem.setOnAction(event -> {
			Stage primaryStage = NLClient.getPrimaryStage();
			primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		newGroupMenuItem.setOnAction(FXMLController::OnCreateNewGroupButtonClick);
		joinGroupMenuItem.setOnAction(FXMLController::OnJoinGroupButtonCLick);
		leaveGroupMenuItem.setOnAction(FXMLController::OnLeaveGroupButtonClick);

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

	private static void OnCreateNewGroupButtonClick(ActionEvent actionEvent)
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
			Sender.send(packet, ClientData.getInstance().getServerAddress());
		}
		catch (IOException | NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	private static void OnJoinGroupButtonCLick(ActionEvent actionEvent)
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
				Sender.send(packet, ClientData.getInstance().getServerAddress());
			}
			catch (IOException | NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void OnLeaveGroupButtonClick(ActionEvent actionEvent)
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
				try
				{
					byte[] packet = MessageBuilder.buildLeaveGroupMessage(selectedChatGroup.getId(),
					                                                      ClientData.getInstance().getThisUser()
					                                                                .getId());
					Sender.send(packet, ClientData.getInstance().getServerAddress());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
