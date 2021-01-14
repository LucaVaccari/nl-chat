package it.castelli.nl.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Objects;

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
                            System.out.println("the connection has an existing user " + advancedUser.getUser().getId() + " who has a queue of size " +
                                    advancedUser.getIncomingMessages().size());
                            out.write(Objects.requireNonNull(advancedUser.getIncomingMessages().poll()));
                            //todo rewrite the line above
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
