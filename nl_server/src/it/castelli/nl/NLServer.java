package it.castelli.nl;

import it.castelli.nl.serialization.Serializer;

public class NLServer
{
    public static void main(String[] args) throws InterruptedException {

        boolean running = true;
        int counter = 0;
        int lifeTime = 100; // in seconds

        Thread serverThread = new Thread(new ServerReceiver(), "serverThread");
        serverThread.start();

        do
        {

            Thread.sleep(1000);
            counter++;
            if (counter >= lifeTime) running = false;

        } while (running);

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
