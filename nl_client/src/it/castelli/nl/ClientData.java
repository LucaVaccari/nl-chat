package it.castelli.nl;

import nl.User;
import nl.serialization.Serializer;
import java.io.Serializable;

public class ClientData implements Serializable
{
    public static final String CLIENT_DATA_FILE_PATH = "clientData.bin";
    private static ClientData instance;

    private User thisUser;

    private ClientData(){}

    public static ClientData getInstance() {
        if(instance == null)
            instance = (ClientData) Serializer.deserialize(CLIENT_DATA_FILE_PATH);
        if (instance == null)
            instance = new ClientData();

        return instance;
    }

    public User getThisUser()
    {
        return thisUser;
    }

    public void setThisUser(User user)
    {
        thisUser = user;
    }
}
