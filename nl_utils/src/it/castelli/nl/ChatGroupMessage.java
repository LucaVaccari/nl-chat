package it.castelli.nl;

import java.io.Serializable;

public class ChatGroupMessage implements Serializable
{
	private User userSender;
	private transient ChatGroup chatGroup;
	private String messageContent;

	public ChatGroupMessage(User userSender, ChatGroup chatGroup, String messageContent)
	{
		this.userSender = userSender;
		this.chatGroup = chatGroup;
		this.messageContent = messageContent;
	}

	public User getUserSender()
	{
		return userSender;
	}

	public void setUserSender(User userSender)
	{
		this.userSender = userSender;
	}

	public ChatGroup getChatGroup()
	{
		return chatGroup;
	}

	public void setChatGroup(ChatGroup chatGroup)
	{
		this.chatGroup = chatGroup;
	}

	public String getMessageContent()
	{
		return messageContent;
	}

	public void setMessageContent(String messageContent)
	{
		this.messageContent = messageContent;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ChatGroupMessage)
		{
			return (((ChatGroupMessage) obj).getUserSender().equals(this.getUserSender()) &&
					((ChatGroupMessage) obj).getChatGroup().equals(this.getChatGroup()) &&
					((ChatGroupMessage) obj).getMessageContent().equals(this.getMessageContent()));
		}
		return false;
	}

	/*@Override
	public int hashCode()
	{
		System.out.println("HashCode was called");
		return 1 + 3 * userSender.getId() + 5 * chatGroup.getCode();
	}*/
}
