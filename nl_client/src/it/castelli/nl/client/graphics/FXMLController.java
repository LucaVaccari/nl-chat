package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.NLClient;
import it.castelli.nl.client.Sender;
import it.castelli.nl.messages.MessageBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

public class FXMLController
{
	public static final String SETTINGS_MENU_FXML_PATH = "src/it/castelli/nl/client/graphics/settingsMenu.fxml";

	private static FXMLController mainFXMLController;
	private static ChatGroup selectedChatGroup;
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
	public ListView<ChatGroupComponent> chatGroupListView;
	public Pane chatElementParent;
	public Stage settingsStage;

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

		// register screen
		boolean askAgain;
		if (ClientData.getInstance().getThisUser() == null)
		{
			String userName;
			do
			{
				askAgain = false;
				Optional<String> name = AlertUtil
						.showTextInputDialogue("Pinco Pallino", "Welcome", "Welcome to nl-chat! Choose a user name",
						                       "Name:");

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

			try
			{
				ClientData.getInstance().setThisUser(new User(userName, (byte) 0));
				byte[] data = MessageBuilder.buildServerNewUserMessage(userName, InetAddress.getLocalHost());
				Sender.addMessageToQueue(data);
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

		helpMenuItem.setOnAction(event -> AlertUtil.showInformationAlert("Help", "If you want help...",
		                                                                 "Contact the developers if you can't " +
		                                                                 "understand."));

		chatGroupListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatGroupListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
			{
				selectedChatGroup = newValue.getChatGroup();
				newValue.showChat();
			}
			else
			{
				oldValue.hideChat();
			}
		});

		// set tooltips (text that appears when holding the cursor on a button)
		createGroupButton.setTooltip(new Tooltip(
				"Create a new chat where you can write" + " messages to other people.\n" +
				"You can invite the others by sharing the" + " group identification code."));
	}

	/**
	 * Callback of a Create New Group button click
	 */
	private void OnCreateNewGroupButtonClick(ActionEvent actionEvent)
	{
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
			result = dialog.showAndWait();

			groupName = result.orElse("");

			// if the user pressed the "cancel" button, exit this method
			if (result.isEmpty()) return;

			groupName.strip();

		} while (groupName.length() > 20 || groupName.length() <= 0);

		try
		{
			User thisUser = ClientData.getInstance().getThisUser();
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
				return;

			askAgain = false;
			if (result.get().matches("\\d{1,3}"))
			{
				if ((resultNumber = Integer.parseInt(result.get(), 10)) > 255)
					askAgain = true;
			}
			else
				askAgain = true;
		} while (askAgain);

		try
		{
			//waits until the user has a valid userId
			byte userId = ClientData.getInstance().getThisUser().getId();
			//while((userId = ClientData.getInstance().getThisUser().getId()) == 0);
			byte[] packet = MessageBuilder.buildJoinGroupMessage((byte) resultNumber, userId);
			Sender.addMessageToQueue(packet);
			Sender.send();
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Callback of a Leave Group menu item click
	 */
	private void OnLeaveGroupButtonClick(ActionEvent actionEvent)
	{
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
				byte[] packet = MessageBuilder.buildLeaveGroupMessage(selectedChatGroup.getCode(),
				                                                      ClientData.getInstance().getThisUser().getId());
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
					byte[] packet = MessageBuilder.buildRemoveGroupMessage(selectedChatGroup.getCode(),
					                                                       ClientData.getInstance().getThisUser()
							                                                       .getId());
					Sender.addMessageToQueue(packet);
					Sender.send();
				}
				else
				{
					AlertUtil.showInformationAlert("Canceled", "The operation was canceled",
					                               "You didn't write 'delete' correctly");
				}
			}
		}
	}
}
