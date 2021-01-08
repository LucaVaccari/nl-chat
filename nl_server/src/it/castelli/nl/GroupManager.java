package it.castelli.nl;

import nl.ChatGroup;

import java.io.File;
import java.util.ArrayList;

public class GroupManager
{
    private static ArrayList<ChatGroup> allGroups;
    private static File groupsFile;

    public static ArrayList<ChatGroup> getAllGroups()
    {
        return allGroups;
    }
}
