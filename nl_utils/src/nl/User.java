package nl;

import java.net.InetAddress;

public class User
{
    private static byte lastUserId = 0;

    private final byte id;

    private String name;
    private InetAddress ipAddress;

    public User(String name, InetAddress ipAddress)
    {
        this.name = name;
        this.ipAddress = ipAddress;
        id = lastUserId++;
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


