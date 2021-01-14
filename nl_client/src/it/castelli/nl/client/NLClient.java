package it.castelli.nl.client;

import it.castelli.nl.serialization.Serializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		NLClient.primaryStage = primaryStage;
		Parent root = loadFXML("src/it/castelli/nl/client/graphics/index.fxml");
		assert root != null;
		Scene mainScene = new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("nl-chat");
		primaryStage.show();

		ClientReceiver receiver = new ClientReceiver();
		clientThread = new Thread(receiver, "ClientThread");
		clientThread.start();

		try
		{
			ClientGroupManager.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		primaryStage.setOnCloseRequest(event -> {
			receiver.interrupt();
			Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);
			try
			{
				clientThread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			System.exit(0);
		});
	}

	/**
	 * Getter for the primary javaFX stage
	 *
	 * @return The primary javaFX stage
	 */
	public static Stage getPrimaryStage()
	{
		return primaryStage;
	}

	/**
	 * Load an FXML file (FX Markup Language, javaFX XML file)
	 *
	 * @param path The path of the FXML file
	 * @return The loaded JavaFX component
	 */
	public static Parent loadFXML(String path)
	{
		if (!Files.exists(Paths.get(path)))
			return null;

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

	/**
	 * Load an FXML file and return its loader
	 *
	 * @param path The path of the FXML file
	 * @return The FXMLLoader object used to load the file
	 */
	public static FXMLLoader getFXMLLoader(String path)
	{
		if (!Files.exists(Paths.get(path)))
			return null;

		FXMLLoader loader = new FXMLLoader();
		try
		{
			loader.setLocation(new File(path).toURI().toURL());
			return loader;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}