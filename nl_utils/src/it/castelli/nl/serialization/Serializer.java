package it.castelli.nl.serialization;

import java.io.*;

/**
 * Util class providing serialization methods
 */
public class Serializer
{

	/**
	 * Read a file and deserialize it
	 *
	 * @param path The path of the file to read
	 * @return The just deserialized Serializable object
	 */
	public static Serializable deserialize(String path) throws IOException, ClassNotFoundException
	{
		Serializable object;
		try (FileInputStream file = new FileInputStream(path);
		     ObjectInputStream in = new ObjectInputStream(file))
		{
			object = (Serializable) in.readObject();
			return object;
		}
	}

	/**
	 * Serialize an object and store it on a file
	 *
	 * @param obj  The Serializable object to be serialized
	 * @param path The path of the file to store the serialized object in
	 */
	public static void serialize(Serializable obj, String path)
	{
		try
		{
			//Saving of object in a file
			FileOutputStream file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
			out.writeObject(obj);

			out.close();
			file.close();
		}
		catch (IOException ex)
		{
			System.out.println("Error serializing the object");
		}
	}

}
