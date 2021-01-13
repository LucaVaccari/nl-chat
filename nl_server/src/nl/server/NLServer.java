package nl.server;

import it.castelli.nl.*;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;

import java.io.IOException;
import java.net.InetAddress;

public class NLServer
{
    public static void main(String[] args) throws InterruptedException {

        boolean running = true;
        int counter = 0;
        int lifeTime = 100; // in seconds

        Thread serverThread = new Thread(new ConnectionReceiver(), "serverThread");
        serverThread.start();

        try
        {
            byte[] packet = MessageBuilder.buildClientTestMessage("Test");
            Sender.sendToClient(packet, InetAddress.getLocalHost());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        while (running)
        {
            //send all the messages in the users queues
        }

        Serializer.serialize(ServerGroupManager.getAllGroups(), ServerGroupManager.GROUPS_FILE_PATH);
        Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);
        Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);


        serverThread.interrupt();
        try
        {
            serverThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
