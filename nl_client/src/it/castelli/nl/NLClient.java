package it.castelli.nl;

import java.util.Scanner;

public class NLClient
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        Thread clientThread = new Thread(new Receiver(), "ClientThread");

        String input;
        do
        {
            input = sc.next();
        } while (!input.equals("stop"));

        sc.close();
        clientThread.interrupt();
        try
        {
            clientThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
