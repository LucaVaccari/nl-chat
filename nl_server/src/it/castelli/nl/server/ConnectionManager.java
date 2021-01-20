package it.castelli.nl.server;

import it.castelli.nl.messages.MessageBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectionManager implements Runnable
{
	private final CopyOnWriteArraySet<Connection> allConnections = new CopyOnWriteArraySet<>();
	private boolean isRunning = true;

	public CopyOnWriteArraySet<Connection> getAllConnections()
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

                        UserManager.AdvancedUser advancedUser = connection.getAdvancedUser();
                        if (advancedUser != null)
                        {
                            //System.out.println("the connection has an existing user " + advancedUser.getUser().getId() + " who has a queue of size " + advancedUser.getIncomingMessages().size());
                            byte[] message = advancedUser.getIncomingMessages().poll();
                            if (message != null)
                            {
                                out.write(message);
                                Thread.sleep(50);
                                System.out.println("Reply sent to user with code: " + advancedUser.getUser().getId());
                            }
                        }
                    }
                    catch (IOException | InterruptedException e)
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
