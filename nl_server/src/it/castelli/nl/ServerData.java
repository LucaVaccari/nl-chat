package it.castelli.nl;

import nl.serialization.Serializer;
import java.io.Serializable;

public class ServerData implements Serializable
{
    public static final String SERVER_DATA_FILE_PATH = "serverData.bin";
    private static ServerData instance;

    private byte lastUserId = 1;
    private byte lastGroupCode = 1;

    private ServerData(){}

    public static ServerData getInstance() {
        if(instance == null)
            instance = (ServerData) Serializer.deserialize(SERVER_DATA_FILE_PATH);
        if (instance == null)
            instance = new ServerData();

        return instance;
    }

    public byte getLastUserId() {
        return lastUserId;
    }

    public byte getLastGroupCode() {
        return lastGroupCode;
    }

    public void incrementLastUserId()
    {
        lastUserId++;
    }

    public void incrementLastGroupCode()
    {
        lastGroupCode++;
    }
}
