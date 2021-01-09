package nl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender
{
    public static final int SERVER_PORT = 3482;
    public static final int CLIENT_PORT = 3483;


    public static void send(byte[] data, InetAddress ipAddress, int port)
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // send the message to all the users in the group except for the given ip address
    public static void send(byte[] data, User senderUser, ChatGroup group, int port)
    {
        group.getUsers().forEach( (user -> {
            if(user.getIpAddress() != senderUser.getIpAddress())
            {
                try
                {
                    DatagramSocket socket = new DatagramSocket(port);
                    DatagramPacket packet = new DatagramPacket(data, data.length, user.getIpAddress(), port);
                    socket.send(packet);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }));
    }
}


