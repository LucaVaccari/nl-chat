package it.castelli.nl;

import java.util.Scanner;

public class NLServer
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        Thread serverThread = new Thread(new Receiver(), "serverThread");

        String input;
        do
        {
            input = sc.next();
        } while (!input.equals("stop"));

        sc.close();
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
