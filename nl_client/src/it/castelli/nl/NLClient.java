package it.castelli.nl;

import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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

		//start test

		String test = "Piero";
		try
		{
			byte[] packet = MessageBuilder.buildServerNewUserMessage(test, InetAddress.getLocalHost());
			Sender.sendToServer(packet, InetAddress.getLocalHost());
			System.out.println("è stato mandato nome: " + test);
			System.out.println("è stato inviato un messaggio all'indirizzo: " + InetAddress.getLocalHost().toString() +
					"alla porta: "
					+ Sender.SERVER_RECEIVE_PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		//end test
	}

	@Override
	public void start(Stage primaryStage)
	{
		clientThread = new Thread(new ClientReceiver(), "ClientThread");
		clientThread.start();


		NLClient.primaryStage = primaryStage;
		Parent root = loadFXML("src/it/castelli/nl/graphics/index.fxml");
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