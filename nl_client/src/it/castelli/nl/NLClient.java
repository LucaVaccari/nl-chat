package it.castelli.nl;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.Receiver;

public class NLClient extends Application
{
	private Thread clientThread;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		clientThread = new Thread(new Receiver(), "ClientThread");
	}

	@Override
	public void stop() throws Exception
	{
		clientThread.interrupt();
		try
		{
			clientThread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.stop();
	}
}
