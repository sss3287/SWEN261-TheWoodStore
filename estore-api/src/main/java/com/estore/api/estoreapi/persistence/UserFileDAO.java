package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * updates user information, including their cart
 */
@Component
public class UserFileDAO implements UserDAO{
    
    Map<Integer, User> users; // Provides a local cache of the user objects
                               // so that we don't need to read from the file each time                               

    private ObjectMapper objectMapper; // Provides conversion between User
    // objects and JSON text format written to the file

    private static int nextId;  // The next Id to assign to a new user in the inventory
    private String filename; // Filename to read from and write to

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load users from the file
    }
    
    /**
     * Generates the next id for a new {@linkplain User user}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Loads {@linkplain User users} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();        

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename), User[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (User user : userArray) {
            users.put(user.getId(), user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map for any
     * {@linkplain User users} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User users}
     * in the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getUsername().contains(containsText)) {
                userArrayList.add(user);
            }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Saves the {@linkplain User users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), userArray);
        return true;
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User[] getUsers() throws IOException {
        synchronized(users) {
            return getUsersArray();
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     ** called searchUser on Trello card
     */
    public User[] findUsers(String containsText) throws IOException {
        synchronized(users) {
            return getUsersArray(containsText);
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User getUser(int id) throws IOException {
        synchronized(users) {
            if (users.containsKey(id))
                return users.get(id);
            else
                return null;
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User createUser(User user) throws IOException {
        synchronized(users) {
            if (getUsersArray(user.getUsername()).length == 0) {
                User newUser = new User (nextId(),user.getUsername(), user.getIsLoggedIn(), user.getCart());
                users.put(newUser.getId(), newUser);
                save(); // may throw an IOException
                return newUser; 
            }
            else {
                return null;
            }
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User updateUser(User user) throws IOException {
        synchronized(user) {
            if (users.containsKey(user.getId()) == false){
                return null;  // user does not exist
            }else{
                users.put(user.getId(),user);
                save(); // may throw an IOException
                return user;
            }
        }
    }
    
    @Override
    /**
    ** {@inheritDoc}
    */
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (id > 0 && users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }

}
