package it.castelli.nl;

import nl.ChatGroup;
import nl.serialization.Serializer;

import java.util.HashMap;

public class ClientGroupManager
{
    private static HashMap<Byte, ChatGroup> allGroups;
    public static final String GROUPS_FILE_PATH = "allGroups.bin";
    public static int lastGroupCode;

    static {
        allGroups = (HashMap<Byte, ChatGroup>) Serializer.deserialize(GROUPS_FILE_PATH);
    }

    public static HashMap<Byte, ChatGroup> getAllGroups()
    {
        return allGroups;
    }

    public static ChatGroup getGroupFromCode(int code)
    {
        return allGroups.get(code);
    }
}
