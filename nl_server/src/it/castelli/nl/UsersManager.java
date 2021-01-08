package it.castelli.nl;

import nl.User;
import nl.serialization.Serializer;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersManager {

    private static HashMap<Integer, User> allUsers;
    public static final String USERS_FILE_PATH = "allUser.bin";

    static {
        allUsers = (HashMap<Integer, User>) Serializer.deserialize(USERS_FILE_PATH);
    }

    public static HashMap<Integer, User> getAllUsers()
    {
        return allUsers;
    }

    public static User getUserFromId(int id)
    {
        return allUsers.get(id);
    }
}
