package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.client.NLClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChatComponent extends VBox
{
	public static final String CHAT_FXML_PATH = "src/it/castelli/nl/client/graphics/chat.fxml";
	public final ChatGroup chatGroup;

	public Label chatGroupName;
	public Label chatGroupCode;
	public ListView<String> messageListView;

	public ChatComponent(ChatGroup chatGroup)
	{
		this.chatGroup = chatGroup;

		FXMLLoader loader = NLClient.getFXMLLoader(CHAT_FXML_PATH);
		assert loader != null;
		loader.setRoot(this);
		loader.setController(this);
		try
		{
			loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		chatGroupName.setText(chatGroup.getName());
		chatGroupCode.setText(String.valueOf(chatGroup.getCode()));
	}
}
