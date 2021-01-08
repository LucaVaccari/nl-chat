package it.castelli.nl;

import nl.ChatGroup;
import nl.User;
import nl.serialization.Serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerGroupManager
{
    private static HashMap<Integer, ChatGroup> allGroups;
    public static final String GROUPS_FILE_PATH = "allGroups.bin";
    public static int lastGroupCode;

    static {
        allGroups = (HashMap<Integer, ChatGroup>) Serializer.deserialize(GROUPS_FILE_PATH);
    }

    public static HashMap<Integer, ChatGroup> getAllGroups()
    {
        return allGroups;
    }

    public static ChatGroup getGroupFromCode(int code)
    {
        return allGroups.get(code);
    }
}
