package it.castelli.nl;

import nl.serialization.Serializer;

public class NLServer
{
    public static void main(String[] args) throws InterruptedException {

        String input = "test";

        do
        {
            System.out.println(input);
            /*Thread.sleep(5000);
            input = "stop";*/
        } while (!input.equals("stop"));

        Serializer.serialize(ServerGroupManager.getAllGroups(), ServerGroupManager.GROUPS_FILE_PATH);
        Serializer.serialize(UsersManager.getAllUsers(), UsersManager.USERS_FILE_PATH);
        Serializer.serialize(ServerData.getInstance(), ServerData.SERVER_DATA_FILE_PATH);


        Thread serverThread = new Thread(new ServerReceiver(), "serverThread");
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
