package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.User;
import it.castelli.nl.client.*;
import it.castelli.nl.messages.MessageBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class FXMLController
{
	public static final String SETTINGS_MENU_FXML_PATH = "src/it/castelli/nl/client/graphics/settingsMenu.fxml";

	private static FXMLController mainFXMLController;
	private static ChatGroup selectedChatGroup;
	public Stage settingsStage;

	// FXML elements
	public Button createGroupButton;
	public Button joinGroupButton;
	public MenuItem settingsMenuItem;
	public MenuItem closeMenuItem;
	public MenuItem newGroupMenuItem;
	public MenuItem joinGroupMenuItem;
	public MenuItem leaveGroupMenuItem;
	public MenuItem removeGroupMenuItem;
	public MenuItem clearGroupContentMenuItem;
	public MenuItem deleteMessageMenuItem;
	public MenuItem copyMessageMenuItem;
	public MenuItem helpMenuItem;
	public ListView<ChatGroupComponent> chatGroupListView;
	public Pane chatElementParent;

	/**
	 * Singleton getter
	 *
	 * @return The only existing instance of FXMLController
	 */
	public static FXMLController get()
	{
		return mainFXMLController;
	}

	@FXML
	public void initialize()
	{
		mainFXMLController = this;

		String userName;
		if (ClientData.getInstance().getThisUser() == null)
		{
			userName = SettingsMenuController.askUserName("Welcome to nl-chat! Choose a user name");
			try
			{
				ClientData.getInstance().setThisUser(new User(userName, (byte) 0));
				byte[] data = MessageBuilder.buildServerNewUserMessage(userName);
				Sender.addMessageToQueue(data);
				System.out.println("Message added to the queue");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}


		// assign functions tu buttons of the interface
		createGroupButton.setOnAction(this::OnCreateNewGroupButtonClick);
		joinGroupButton.setOnAction(this::OnJoinGroupButtonCLick);

		settingsMenuItem.setOnAction(event -> {
			settingsStage = new Stage();
			settingsStage.setTitle("Settings");
			settingsStage.setAlwaysOnTop(true);
			settingsStage.initModality(Modality.APPLICATION_MODAL);
			settingsStage.setResizable(false);
			Parent root = NLClient.loadFXML(SETTINGS_MENU_FXML_PATH);
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

		clearGroupContentMenuItem.setOnAction(this::OnClearGroupContentButtonClick);
		deleteMessageMenuItem.setOnAction(this::OnDeleteMessageButtonClick);
		copyMessageMenuItem.setOnAction(this::OnCopyMessageButtonClick);

		helpMenuItem.setOnAction(event -> AlertUtil.showInformationAlert("Help", "If you want help...",
				"Contact the developers if you can't " +
						"understand."));

		chatGroupListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatGroupListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
			{
				selectedChatGroup = newValue.getChatGroup();
				newValue.showChat();
			} else
			{
				selectedChatGroup = null;
				oldValue.hideChat();
			}
		});

		// set tooltips (text that appears when holding the cursor on a button)
		createGroupButton.setTooltip(new Tooltip(
				"Create a new chat where you can write messages to other people.\n" +
						"You can invite the others by sharing the group identification code."));

		joinGroupButton.setTooltip(new Tooltip(
				"Join an existing group by specifying its code. Only the messages sent after you join will be visible"
		));
	}

	/**
	 * Callback of a Create New Group button click
	 */
	private void OnCreateNewGroupButtonClick(ActionEvent actionEvent)
	{
		User thisUser = ClientData.getInstance().getThisUser();

		if (thisUser.getId() <= 0)
		{
			AlertUtil.showInformationAlert("Action not available offline", "You are offline so you can't perform this action now", "You can't create groups right now because you are offline. Try changing the IP address in the settings or try later.");
			return;
		}

		// ask for the name of the group
		TextInputDialog dialog = new TextInputDialog("MyGroup");
		dialog.setTitle("New group");
		dialog.setHeaderText("Choose a name");
		dialog.setContentText("Enter a name for the group\n (with less than 20 characters)");

		// if the name is invalid, ask again
		Optional<String> result;
		String groupName;
		do
		{
			dialog.setResult("");
			result = dialog.showAndWait();

			groupName = result.orElse("");

			// if the user pressed the "cancel" button, exit this method
			if (result.isEmpty()) return;

			groupName = groupName.strip();

		} while (groupName.length() > 20 || groupName.length() <= 0);

		try
		{
			System.out.println("User with id " + thisUser.getId() + " is trying to create the group " + result.get());
			byte[] packet = MessageBuilder
					.buildCreateGroupMessage(thisUser.getId(), result.get());
			Sender.addMessageToQueue(packet);
			Sender.send();
		}
		catch (IOException | NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Callback of a Join Group button click
	 */
	private void OnJoinGroupButtonCLick(ActionEvent actionEvent)
	{
		byte userId = ClientData.getInstance().getThisUser().getId();

		if (userId <= 0)
		{
			AlertUtil.showInformationAlert("Action not available offline", "You are offline so you can't perform this action now", "You can't join groups right now because you are offline. Try changing the IP address in the settings or try later.");
			return;
		}

		Optional<String> result;
		int resultNumber = 0;
		boolean askAgain;

		// ask for the code of the group
		do
		{
			result = AlertUtil
					.showTextInputDialogue("0", "Join group", "Insert the code of the group you want to join",
							"Code: ");

			// if the users pressed the "cancel" button, return
			if (result.isEmpty())
			{
				return;
			}

			askAgain = false;
			if (result.get().matches("\\d{1,3}"))
			{
				if ((resultNumber = Integer.parseInt(result.get(), 10)) > 255)
				{
					askAgain = true;
				}
			} else
			{
				askAgain = true;
			}
		} while (askAgain);

		try
		{
			byte[] packet = MessageBuilder.buildJoinGroupMessage((byte) resultNumber, userId);
			Sender.addMessageToQueue(packet);
			Sender.send();
		}
		catch (NullPointerException | IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Callback of a Leave Group menu item click
	 */
	private void OnLeaveGroupButtonClick(ActionEvent actionEvent)
	{
		if (selectedChatGroup == null)
			return;

		// ask for a confirmation
		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Leave group", "Are you sure?",
				"You will lose all the messages of this chat" +
						".\nDo you really want to leave?");
		// if the window wasn't closed
		if (result.isPresent())
		{
			// if the user pressed "ok" and not "cancel"
			if (result.get() == ButtonType.OK)
			{
				byte[] packet = new byte[0];
				try
				{
					packet = MessageBuilder.buildLeaveGroupMessage(selectedChatGroup.getCode(),
							ClientData.getInstance().getThisUser().getId());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				Sender.addMessageToQueue(packet);
				Sender.send();
			}
		}
	}

	/**
	 * Callback of a Remove Group menu item click
	 */
	private void OnRemoveGroupButtonClick(ActionEvent actionEvent)
	{
		if (selectedChatGroup == null)
			return;

		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Remove group", "Are you sure?",
				"You will destroy this group. All the members " +
						"will be ejected and all the messages lost.\n" +
						"Are you REALLY sure?");

		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				Optional<String> confirmation =
						AlertUtil.showTextInputDialogue("", "Remove group", "Are you really sure?",
								"Write 'delete' to delete your group");
				if (confirmation.isEmpty())
					return;

				if (confirmation.get().equals("delete"))
				{
					byte[] packet = new byte[0];
					try
					{
						packet = MessageBuilder.buildRemoveGroupMessage(selectedChatGroup.getCode(),
								ClientData.getInstance().getThisUser()
										.getId());
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					Sender.addMessageToQueue(packet);
					Sender.send();
				} else
				{
					AlertUtil.showInformationAlert("Canceled", "The operation was canceled",
							"You didn't write 'delete' correctly");
				}
			}
		}
	}

	/**
	 * Callback of a Clear Group Content menu item click
	 */
	private void OnClearGroupContentButtonClick(ActionEvent actionEvent)
	{
		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Clear content", "Remove all messages",
				"Are you sure you want to delete all the " +
						"messages of this group? You won't " +
						"be able to see them again. The other members " +
						"of the group will not be affected.");
		if (result.isEmpty())
			return;

		if (result.get().equals(ButtonType.OK))
		{
			ChatGroupComponent selectedChatGroupComponent = chatGroupListView.getSelectionModel().getSelectedItem();
			if (selectedChatGroupComponent != null)
			{
				selectedChatGroupComponent.getChatComponent().getMessageListView().getItems().clear();

				// update last message label
				selectedChatGroupComponent.getLastMessageLabel().setText("");
			}
		}
	}

	/**
	 * Callback of a Delete Group Content menu item click
	 */
	private void OnDeleteMessageButtonClick(ActionEvent actionEvent)
	{
		Optional<ButtonType> result = AlertUtil.showConfirmationAlert("Delete message", "Remove a single message",
				"This will remove the selected message only " +
						"for you. You won't be able to see it again. " +
						"The other members of the group will not be " +
						"affected.");
		if (result.isEmpty())
			return;

		if (result.get().equals(ButtonType.OK))
		{
			ChatGroupComponent selectedChatGroupComponent = chatGroupListView.getSelectionModel().getSelectedItem();
			ListView<ChatMessageComponent> messageListView =
					selectedChatGroupComponent.getChatComponent().getMessageListView();
			messageListView.getItems().remove(
					messageListView.getSelectionModel().getSelectedItem());

			// update last message label
			ArrayList<ChatGroupMessage> userMessages =
					selectedChatGroupComponent.getChatGroup().getChatGroupContent().getUserMessages();
			ClientGroupManager.updateLastMessageLabel(selectedChatGroupComponent, userMessages);
		}
	}

	/**
	 * Callback of a Copy Message menu item click
	 */
	private void OnCopyMessageButtonClick(ActionEvent actionEvent)
	{
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		ChatComponent chatComponent = chatGroupListView.getSelectionModel().getSelectedItem().getChatComponent();
		ChatMessageComponent selectedMessage =
				chatComponent.getMessageListView().getSelectionModel().getSelectedItem();
		content.putString(selectedMessage.getMessageLabel().getText());
		clipboard.setContent(content);
	}

}
