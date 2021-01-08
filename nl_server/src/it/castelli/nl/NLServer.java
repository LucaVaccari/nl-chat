package it.castelli.nl;

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
