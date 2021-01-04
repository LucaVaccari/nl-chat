package it.castelli.nl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ChatGroup implements Serializable
{
    private static byte lastId = 0;

    private ArrayList<User> users;
    private final byte id;
    private String name;
    private File file;

    public ChatGroup(String name)
    {
        this.name = name;
        id = lastId++;
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
}
