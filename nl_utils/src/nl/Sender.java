package nl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender
{
    public static final int PORT = 3482;

    //send a message to one ip (used for the communications with the server)
    public static void send(byte[] data, InetAddress serverIp)
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(PORT);
            DatagramPacket packet = new DatagramPacket(data, data.length, serverIp, PORT);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // send the message to all the users in the group except for the given ip address
    public static void send(byte[] data, User senderUser, ChatGroup group)
    {
        group.getUsers().forEach( (user -> {
            if(user.getIpAddress() != senderUser.getIpAddress())
            {
                try
                {
                    DatagramSocket socket = new DatagramSocket(PORT);
                    DatagramPacket packet = new DatagramPacket(data, data.length, user.getIpAddress(), PORT);
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


