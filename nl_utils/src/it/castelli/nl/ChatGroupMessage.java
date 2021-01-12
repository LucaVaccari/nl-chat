package it.castelli.nl;

public class ChatGroupMessage
{
	private User userSender;
	private ChatGroup chatGroup;
	private String messageContent;

	public User getUserSender()
	{
		return userSender;
	}

	public ChatGroupMessage(User userSender, ChatGroup chatGroup, String messageContent)
	{
		this.userSender = userSender;
		this.chatGroup = chatGroup;
		this.messageContent = messageContent;
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
}
