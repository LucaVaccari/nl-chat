package nl;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable
{
    private final byte id;

    private String name;
    private InetAddress ipAddress;

    public User(String name, InetAddress ipAddress, byte id)
    {
        this.name = name;
        this.ipAddress = ipAddress;
        this.id = id;
    }

    public User(String name, byte id)
    {
        this.name = name;
        this.id = id;
    }

    public User(InetAddress ipAddress, byte id)
    {
        this.ipAddress = ipAddress;
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

    public InetAddress getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress)
    {
        this.ipAddress = ipAddress;
    }
}


