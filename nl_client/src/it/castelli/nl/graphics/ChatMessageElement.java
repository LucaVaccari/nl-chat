package it.castelli.nl.graphics;

import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.NLClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;

public class ChatMessageElement extends VBox
{
	public Label userName;
	public Label message;

	public ChatMessageElement(ChatGroupMessage message)
	{
		FXMLLoader loader = NLClient.getFXMLLoader("src/it/castelli/nl/graphics/chatMessage.fxml");
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

		userName.setText(message.getUserSender().getName());
		this.message.setText(message.getMessageContent());
	}
}
