package it.castelli.nl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.Sender;
import nl.messages.MessageBuilder;
import nl.serialization.Serializer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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
		clientThread.start();

		//start test

		String test = "Hello World!";
		try
		{
			byte[] packet = MessageBuilder.buildServerTestMessage(test);
			Sender.sendToServer(packet, InetAddress.getLocalHost());
			System.out.println("è stato inviato un messaggio all'indirizzo: " + InetAddress.getLocalHost().toString() +
			                   "alla porta: "
			                   + Sender.SERVER_RECEIVE_PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		//end test

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