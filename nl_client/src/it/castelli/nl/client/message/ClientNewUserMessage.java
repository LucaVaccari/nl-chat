package it.castelli.nl.client.message;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.client.ClientGroupManager;
import it.castelli.nl.client.graphics.AlertUtil;
import javafx.application.Platform;

import java.util.Arrays;

/**
 * A new user has entered a group (message to the client)
 */
public class ClientNewUserMessage implements IMessage
{

	@Override
	public void onReceive(byte[] data)
	{
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		byte[] contentOfMessage = Arrays.copyOfRange(data, 3, data.length);
		String userName = new String(contentOfMessage).strip();
		ChatGroup thisGroup = ClientGroupManager.getGroupFromCode(groupCode);
		User newUser = new User(userName, userId);

		System.out.println(userName + " entered " + thisGroup.getName());

		Platform.runLater(() -> {
			AlertUtil.showInformationAlert("A new User has joined", "A new User joined one of your groups",
					"The User " + userName + " has joined the group + " + thisGroup.getName() + ", go and say hello!");
		});

		thisGroup.getUsers().add(newUser);
	}
}
