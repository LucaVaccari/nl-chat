package it.castelli.nl;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXMLController
{
	public Button createChatButton; //TODO
	public MenuItem settingsMenuItem; //TODO
	public MenuItem closeMenuItem;
	public MenuItem newChatMenuItem; //TODO
	public MenuItem clearChatContentMenuItem; //TODO
	public MenuItem leaveChatMenuItem; //TODO
	public MenuItem removeChatMenuItem; //TODO
	public MenuItem helpMenuItem;
	public VBox chatsVBox; //TODO
	public Label groupNameLabel; //TODO
	public Label groupIdLabel; //TODO
	public ListView<String> messageList; //TODO
	public TextField messageInputField; //TODO

	@FXML
	public void initialize()
	{
		closeMenuItem.setOnAction(event -> {
			Stage primaryStage = NLClient.getPrimaryStage();
			primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		helpMenuItem.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("If you want help...");
			alert.setContentText("Contact the developers if you can't understand.");
			alert.show();
		});

		createChatButton.setTooltip(new Tooltip(
				"Create a new chat where you can write" + " messages to other people.\n" +
				"You can invite the others by sharing the" + " group identification code."));
	}
}
