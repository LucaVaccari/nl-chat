package it.castelli.nl;

import nl.User;
import nl.serialization.Serializer;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersManager {

    private static HashMap<Byte, User> allUsers;
    public static final String USERS_FILE_PATH = "allUser.bin";

    static {
        allUsers = (HashMap<Byte, User>) Serializer.deserialize(USERS_FILE_PATH);
    }

    public static HashMap<Byte, User> getAllUsers()
    {
        return allUsers;
    }

    public static User getUserFromId(int id)
    {
        return allUsers.get(id);
    }
}
