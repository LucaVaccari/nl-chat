package it.castelli.nl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class NLClient extends Application
{
	private Thread clientThread;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		clientThread = new Thread(new ClientReceiver(), "ClientThread");

		FXMLLoader loader = new FXMLLoader();
		try
		{
			loader.setLocation(new File("src/index.fxml").toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		Parent root;
		try
		{
			root = loader.load();
			assert root != null;
			Scene mainScene = new Scene(root);
			primaryStage.setScene(mainScene);
			primaryStage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception
	{
		clientThread.interrupt();
		try
		{
			clientThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		super.stop();
	}
}
