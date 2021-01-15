package it.castelli.nl.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

public class ConnectionManager implements Runnable
{
	private final HashSet<Connection> allConnections = new HashSet<>();
	private boolean isRunning = true;

	public HashSet<Connection> getAllConnections()
	{
		return allConnections;
	}

	@Override
	public void run()
	{

        System.out.println("ConnectionManager is working correctly");
        while (isRunning)
        {
            for (Connection connection : allConnections)
            {
                if (connection.getSocket().isConnected() && !connection.getSocket().isClosed())
                {
                    try
                    {
                        OutputStream out = connection.getSocket().getOutputStream();

                        UsersManager.AdvancedUser advancedUser = connection.getAdvancedUser();
                        if (advancedUser != null)
                        {
                            //System.out.println("the connection has an existing user " + advancedUser.getUser().getId() + " who has a queue of size " + advancedUser.getIncomingMessages().size());
                            byte[] message = advancedUser.getIncomingMessages().poll();
                            if (message != null)
                            {
                                out.write(message);
                                System.out.println("Reply sent to user with code: " + advancedUser.getUser().getId());
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        System.out.println("Connection has ended");
                        connection.interrupt();
                        allConnections.remove(connection);
                    }
                }
                else
                {
                    System.out.println("Connection has ended");
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
