package it.castelli.nl.client;

import it.castelli.nl.client.graphics.SettingsMenuController;
import it.castelli.nl.messages.MessageBuilder;
import it.castelli.nl.serialization.Serializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NLClient extends Application
{
	public static final String INDEX_FXML_FILE_PATH = "src/it/castelli/nl/client/graphics/index.fxml";
	private static Stage primaryStage;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		NLClient.primaryStage = primaryStage;
		Parent root = loadFXML(INDEX_FXML_FILE_PATH);
		if (root != null)
		{
			Scene mainScene = new Scene(root);
			primaryStage.setScene(mainScene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("nl-chat | server offline");

			Image image = new Image("file:src/nl-logo.png");
			primaryStage.getIcons().add(image);

			ConnectionHandler.startConnection();
			if (!ConnectionHandler.isConnected())
			{
				SettingsMenuController.askServerIp();
			}

			ClientGroupManager.init();

			primaryStage.show();
		} else
			System.out.println("Root is null in Application.start()");

		primaryStage.setOnCloseRequest(event -> {
			Serializer.serialize(ClientData.getInstance(), ClientData.CLIENT_DATA_FILE_PATH);
			Serializer.serialize(ClientGroupManager.getAllGroups(), ClientGroupManager.GROUPS_FILE_PATH);
			System.out.println("The length of the queue during serialization is: " + ClientData.getInstance().getMessageQueue().size());
			ConnectionHandler.endConnection();
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
			System.out.println("Cannot find file: " + path);
			e.printStackTrace();
			return null;
		}
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