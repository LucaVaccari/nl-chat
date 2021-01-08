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

        Serializer.serialize(GroupManager.getAllGroups(), GroupManager.GROUPS_FILE_PATH);

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
