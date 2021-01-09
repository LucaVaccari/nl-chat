package nl;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender
{
    public static final int SERVER_SEND_PORT = 3400;
    public static final int SERVER_RECEIVE_PORT = 3401;
    public static final int CLIENT_SEND_PORT = 3500;
    public static final int CLIENT_RECEIVE_PORT = 3501;

    private static void send(byte[] data, InetAddress ipAddress, int sourcePort, int destinationPort)
    {
        try(DatagramSocket socket = new DatagramSocket(sourcePort))
        {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, destinationPort);
            socket.send(packet);

        } catch (BindException i)
        {
            System.out.println("");
            i.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // send the message to all the users in the group except for the given ip address
    private static void send(byte[] data, User senderUser, ChatGroup group, int sourcePort, int destinationPort)
    {
        group.getUsers().forEach( (user -> {
            if(user.getIpAddress() != senderUser.getIpAddress())
            {
                try
                {
                    DatagramSocket socket = new DatagramSocket(sourcePort);
                    DatagramPacket packet = new DatagramPacket(data, data.length, user.getIpAddress(), destinationPort);
                    socket.send(packet);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }));
    }

    public static void sendToClient(byte[] data, User senderUser, ChatGroup group)
    {
        send(data, senderUser, group, SERVER_SEND_PORT, CLIENT_RECEIVE_PORT);
    }

    public static void sendToClient(byte[] data, InetAddress ipAddress)
    {
        send(data, ipAddress, SERVER_SEND_PORT, CLIENT_RECEIVE_PORT);
    }

    public static void sendToServer(byte[] data, User senderUser, ChatGroup group)
    {
        send(data, senderUser, group, CLIENT_SEND_PORT, SERVER_RECEIVE_PORT);
    }

    public static void sendToServer(byte[] data, InetAddress ipAddress)
    {
        send(data, ipAddress, CLIENT_SEND_PORT, SERVER_RECEIVE_PORT);
    }

}


