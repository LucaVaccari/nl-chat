package it.castelli.nl.client.graphics;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.client.NLClient;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * UIComponent (present in the left panel) holding information about a chat group. When clicked shows a ChatComponent
 */
public class ChatGroupComponent extends VBox
{
	private static final String FXML_FILE_PATH = "src/it/castelli/nl/client/graphics/chatGroup.fxml";
	private final ChatGroup chatGroup;
	private final ChatComponent chatComponent;
	public Label groupNameLabel;
	public Label groupCodeLabel;
	public Label lastMessageLabel; // TODO

	public ChatGroupComponent(ChatGroup chatGroup)
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

		chatComponent = new ChatComponent(chatGroup);
	}

	/**
	 * Getter for the chat group bound to this UI element
	 *
	 * @return The chat group bound to this UI element
	 */
	public ChatGroup getChatGroup()
	{
		return chatGroup;
	}

	/**
	 * Shows the messages and some information of the ChatGroup in the UI
	 */
	public void showChat()
	{
		ObservableList<Node> chatGroupElementChildren = FXMLController.get().chatElementParent.getChildren();
		chatGroupElementChildren.clear();
		chatGroupElementChildren.add(chatComponent);
	}
}
