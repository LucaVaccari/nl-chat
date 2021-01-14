package it.castelli.nl.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

public class ConnectionManager implements Runnable
{
    private HashSet<Connection> allConnections = new HashSet<>();
    private boolean isRunning = true;

    public HashSet<Connection> getAllConnections() {
        return allConnections;
    }

    @Override
    public void run() {

        System.out.println("ConnectionManager is working correctly");
        while (isRunning)
        {
            for (Connection connection : allConnections)
            {
                if (connection.getSocket().isConnected())
                {
                    try
                    {
                        OutputStream out = connection.getSocket().getOutputStream();

                        UsersManager.AdvancedUser advancedUser = connection.getAdvancedUser();
                        if (advancedUser != null)
                        {
                            System.out.println("the connection has an existing user");
                            System.out.println("the length of the queue is: " +
                                    advancedUser.getIncomingMessages().size());
                            for (byte[] message : advancedUser.getIncomingMessages())
                            {
                                System.out.println("the length of the queue is: " +
                                        advancedUser.getIncomingMessages().size());
                                out.write(message);
                                System.out.println("a message in the queue has been sent");
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    connection.interrupt();
                    allConnections.remove(connection);
                }
            }
        }
    }

    public void interrupt()
    {
        isRunning = false;
        for (Connection connection : allConnections)
        {
            connection.interrupt();
        }
    }

}
