package it.castelli.nl.client;

import it.castelli.nl.GeneralData;
import it.castelli.nl.client.graphics.AlertUtil;
import it.castelli.nl.client.message.ClientMessageManager;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Thread for receiving messages from the server
 */
public class ClientReceiver implements Runnable
{
	public static final int RECEIVE_WINDOW = 2048;
	private boolean isRunning = true;

	/**
	 * The run() function from the Runnable interface is called when the thread starts.
	 * This one infinitely waits for packets, until the end of the program.
	 */
	public void run()
	{
		try (Socket socket = new Socket(ClientData.getInstance().getServerAddress(), GeneralData.SERVER_RECEIVE_PORT);
		     InputStream inStream = socket.getInputStream();
		     OutputStream outStream = socket.getOutputStream())
		{
			byte[] receiveBuffer = new byte[RECEIVE_WINDOW];
			Sender.setOutStream(outStream);
			while (isRunning)
			{
				System.out.println("ClientReceiver is working on port: " + GeneralData.SERVER_RECEIVE_PORT);
				//noinspection ResultOfMethodCallIgnored
				inStream.read(receiveBuffer);
				System.out.println("A packet has been received from " + socket.getInetAddress().getHostAddress());
				ClientMessageManager.getMessageReceiver(receiveBuffer[0]).OnReceive(receiveBuffer);
			}
		}
		catch (ConnectException e)
		{
			Platform.runLater(() -> AlertUtil.showErrorAlert("Connection error", "Cannot connect to the server",
			                                                 "The server is offline or unreachable"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Called when the thread is interrupted (not sure)
	 */
	public void interrupt()
	{
		isRunning = false;
	}
}
