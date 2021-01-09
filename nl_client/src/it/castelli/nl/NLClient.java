package it.castelli.nl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.serialization.Serializer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class NLClient extends Application
{
	private Thread clientThread;

	private static Stage primaryStage;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		clientThread = new Thread(new ClientReceiver(), "ClientThread");
		NLClient.primaryStage = primaryStage;
		Parent root = loadFXML("src/index.fxml");
		assert root != null;
		Scene mainScene = new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception
	{
		clientThread.interrupt();
		Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);
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

	public static Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public static Parent loadFXML(String path)
	{
		FXMLLoader loader = new FXMLLoader();
		try
		{
			loader.setLocation(new File(path).toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		try
		{
			return loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}