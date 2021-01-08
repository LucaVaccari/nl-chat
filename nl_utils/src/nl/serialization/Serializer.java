package nl.serialization;

import java.io.*;

public class Serializer {

    public static Serializable deserialize(String path)
    {
        Serializable object;
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            try {
                object = (Serializable) in.readObject();
                return object;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }

        return null;
    }

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

            System.out.println("Object has been serialized");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

}
