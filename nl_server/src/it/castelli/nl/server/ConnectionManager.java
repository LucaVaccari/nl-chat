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

        while (isRunning)
        {
            for (Connection connection : allConnections)
            {
                if (connection.getSocket().isConnected())
                {
                    try
                    {
                        OutputStream out = connection.getSocket().getOutputStream();
                        for (byte[] message : connection.getAdvancedUser().getIncomingMessages())
                            out.write(message);
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
