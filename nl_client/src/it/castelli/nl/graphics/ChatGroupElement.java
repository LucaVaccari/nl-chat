package it.castelli.nl.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.NLClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controller for any ChatGroup graphic object
 */
public class ChatGroupElement extends VBox
{
	private static final String FXML_FILE_PATH = "src/it/castelli/nl/graphics/chatGroup.fxml";

	public Label groupNameLabel;
	public Label groupCodeLabel;
	public Label lastMessageLabel;

	private ChatGroup chatGroup;

	public ChatGroupElement(ChatGroup chatGroup)
	{
		this.chatGroup = chatGroup;

		FXMLLoader loader = NLClient.getFXMLLoader(FXML_FILE_PATH);
		assert loader != null;
		loader.setRoot(this);
		loader.setController(this);

		try
		{
			loader.load();

			groupNameLabel.setText(chatGroup.getName());
			groupCodeLabel.setText(String.valueOf(chatGroup.getCode()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the chat group bound to this UI element
	 * @return The chat group bound to this UI element
	 */
	public ChatGroup getChatGroup()
	{
		return chatGroup;
	}
}
