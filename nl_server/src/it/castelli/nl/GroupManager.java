package it.castelli.nl;

import nl.ChatGroup;
import nl.serialization.Serializer;

import java.io.File;
import java.util.ArrayList;

public class GroupManager
{
    private static ArrayList<ChatGroup> allGroups;
    public static final String GROUPS_FILE_PATH = "allGroups.bin";

    static {
        allGroups = (ArrayList<ChatGroup>) Serializer.deserialize(GROUPS_FILE_PATH);
    }

    public static ArrayList<ChatGroup> getAllGroups()
    {
        return allGroups;
    }
}
