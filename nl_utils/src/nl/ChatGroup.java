package nl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ChatGroup implements Serializable
{

    private ArrayList<User> users;
    private ArrayList<User> superUsers;
    private final byte id;
    private String name;
    private final ChatGroupContent chatGroupContent = new ChatGroupContent();

    public ChatGroup(String name, byte id)
    {
        this.name = name;
        this.id = id;
    }

    public byte getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<User> getUsers()
    {
        return users;
    }

    public ChatGroupContent getChatGroupContent()
    {
        return chatGroupContent;
    }

    public ArrayList<User> getSuperUsers()
    {
        return superUsers;
    }
}
