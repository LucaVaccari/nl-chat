package it.castelli.nl.client;

import it.castelli.nl.client.graphics.AlertUtil;

import java.io.IOException;
import java.io.OutputStream;

public class Sender
{
	private static OutputStream outStream;

	private Sender() {}

	public static void send(byte[] data)
	{
		try
		{
			if (outStream == null)
			{
				System.out.println("OutputStream to the server is null");
				return;
			}

			outStream.write(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			AlertUtil.showErrorAlert("Sending error", "Cannot talk to the server", "The server cannot be reached");
		}
	}

	/**
	 * Setter for the OutputStream of the class
	 *
	 * @param outStream The new OutputStream
	 */
	public static void setOutStream(OutputStream outStream)
	{
		Sender.outStream = outStream;
	}
}
