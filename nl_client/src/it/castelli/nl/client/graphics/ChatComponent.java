package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.ChatGroupMessage;
import it.castelli.nl.client.ClientData;
import it.castelli.nl.client.NLClient;
import it.castelli.nl.client.Sender;
import it.castelli.nl.messages.MessageBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * UIComponent for holding the chat, with all the messages
 */
public class ChatComponent extends VBox
{
	public static final String CHAT_FXML_PATH = "src/it/castelli/nl/client/graphics/chat.fxml";
	public final ChatGroup chatGroup;

	public Label chatGroupName;
	public Label chatGroupCode;
	public ListView<String> messageListView;
	public TextField messageInputField;
	public Button sendMessageButton;

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

		messageInputField.setOnAction(this::OnMessageSend);
		sendMessageButton.setOnAction(this::OnMessageSend);

		chatGroupName.setText(chatGroup.getName());
		chatGroupCode.setText(String.valueOf(chatGroup.getCode()));
	}

	public ListView<String> getMessageListView()
	{
		return messageListView;
	}

	/**
	 * Callback of a Send button click
	 */
	private void OnMessageSend(ActionEvent actionEvent)
	{
		String text = messageInputField.getText();

		if (!text.equals(""))
		{
			try
			{
				byte[] packet = MessageBuilder.buildServerUserChatMessage(
						new ChatGroupMessage(ClientData.getInstance().getThisUser(), chatGroup, text));
				Sender.addMessageToQueue(packet);
				Sender.send();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		messageInputField.setText("");
	}
}
