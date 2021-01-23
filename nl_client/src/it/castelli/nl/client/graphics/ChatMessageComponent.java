package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.client.NLClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * UIComponent containing a message present in the list of messages of a ChatComponent
 */
public class ChatMessageComponent extends VBox
{
	public Label userNameLabel;
	public Label messageLabel;

	private final ChatGroupMessage chatGroupMessage;

	public ChatMessageComponent(ChatGroupMessage message)
	{
		this.chatGroupMessage = message;

		FXMLLoader loader = NLClient.getFXMLLoader("src/it/castelli/nl/client/graphics/chatMessage.fxml");
		assert loader != null;
		loader.setController(this);
		loader.setRoot(this);

		try
		{
			loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		userNameLabel.setText(message.getUserSender().getName());
		messageLabel.setText(message.getMessageContent());
	}

	public Label getUserNameLabel()
	{
		return userNameLabel;
	}

	public Label getMessageLabel()
	{
		return messageLabel;
	}

	public ChatGroupMessage getChatGroupMessage()
	{
		return chatGroupMessage;
	}
}
