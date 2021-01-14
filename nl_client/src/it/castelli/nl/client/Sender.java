package it.castelli.nl.client;

import java.io.OutputStream;

public class Sender
{
	private static OutputStream outStream;

	private Sender() {}

	/**
	 * Setter for the OutputStream of the class
	 * @param outStream The new OutputStream
	 */
	public static void setOutStream(OutputStream outStream)
	{
		Sender.outStream = outStream;
	}
}
