package it.castelli.nl.server.messages;

import it.castelli.nl.ChatGroup;
import it.castelli.nl.User;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.server.Connection;
import it.castelli.nl.server.GroupManager;
import it.castelli.nl.server.Sender;

import java.io.IOException;

public class JoinGroupMessage extends Message
{
	@Override
	public synchronized void onReceive(byte[] data, Connection connection)
	{
		super.onReceive(data, connection);
		// syntax: 1 byte for the type of message, 1 for the group code, 1 for the user id, others

		byte groupCode = data[1];
		byte userId = data[2];
		ChatGroup groupToJoin = GroupManager.getGroupFromCode(groupCode);
		User thisUser = connection.getAdvancedUser().getUser();

		if (groupToJoin == null)
		{
			//the group doesn't exist -> error message
			try
			{
				byte[] errorReply = MessageBuilder
						.buildErrorMessage("The group with code: " + String.valueOf(groupCode) + " does not exist.");
				Sender.sendToUser(errorReply, thisUser);
				System.out.println("Sent an error message");
			}
			catch (IOException e)
			{
				System.out.println("IOException in " + this.toString() + " during the error reply creation");
				e.printStackTrace();
			}
		}
		else
		{
			//the group exists

			//the user is added to the group if he's not a participant yet

			if (!groupToJoin.getUsers().contains(thisUser))
			{
				groupToJoin.getUsers().add(thisUser);

				//communication to inform others that a new User joined the group and transmission of the group to the
				// user
				try
				{
					//send group
					byte[] reply = MessageBuilder.buildClientNewGroupMessage(groupCode, groupToJoin.getName());
					System.out.println("Created ClientNewGroupMessage from JoinGroupMessage in the onReceive method");
					Sender.sendToUser(reply, thisUser);

					//sends Users who are in the group to the new one and sends the new one to others
					for (User user : groupToJoin.getUsers())
					{
						reply = MessageBuilder.buildClientNewUserMessage(groupCode, user.getId(), user.getName());
						System.out
								.println("Created ClientNewUserMessage from JoinGroupMessage in the onReceive method");
						if (user == thisUser)
							Sender.sendToOthersInGroup(reply, thisUser, groupToJoin);
						else
							Sender.sendToUser(reply, thisUser);
					}
				}
				catch (IOException e)
				{
					System.out.println(
							"IOException in " + this.toString() + " during reply creation in JoinGroupMessage");
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					byte[] errorReply = MessageBuilder
							.buildErrorMessage("You are already in the group " + groupCode);
					Sender.sendToUser(errorReply, thisUser);
				}
				catch (IOException e)
				{
					System.out.println("IOException in " + this.toString() + " during the error reply creation");
					e.printStackTrace();
				}
			}
		}
	}
}
