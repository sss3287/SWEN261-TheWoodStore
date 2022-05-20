package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 */
@Tag("Peersistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUser;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUser = new User[3];
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        Product[] cart3 = new Product[100];
        testUser[0] = new User(0, "admin", false, cart1);
        testUser[1] = new User(1, "Teags", false, cart2);
        testUser[2] = new User(2, "Juan", true, cart3);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the user array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUser);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testCreateUser() throws IOException { //may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Carlos", true, cart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(result.getId());
        assertEquals(actual.getId(),result.getId());
        assertEquals(actual.getUsername(),user.getUsername());
        assertEquals(actual.getIsLoggedIn(),user.getIsLoggedIn());

    }

    @Test
    public void testCreateUserFailed() throws IOException { //may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Teags", true, cart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }


    @Test
    public void testGetUsers() throws IOException { //may throw IOException
        // Invoke
        User[] users = userFileDAO.getUsers();

        // Analyze
        assertEquals(users.length,users.length);
        for (int i = 0; i < users.length;++i)
            assertEquals(users[i],users[i]);
    }

    @Test
    public void testFindUsers() throws IOException { //may throw IOException
        // Invoke
        User[] users = userFileDAO.findUsers("n");

        // Analyze
        assertEquals(users.length,2);
        assertEquals(users[0],testUser[0]);
        assertEquals(users[1],testUser[2]);
    }

    @Test
    public void testGetUser() throws IOException { //may throw IOException
        // Invoke
        User user = userFileDAO.getUser(0);

        // Analzye
        assertEquals(user,testUser[0]);
    }

    @Test
    public void testGetUserFailed() throws IOException { //may throw IOException
        // Invoke
        User nullUser = userFileDAO.getUser(99);

        //Analyze
        assertNull(nullUser);
    }

    
    @Test
    public void testDeleteUserFailedZero() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(0),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        assertEquals(userFileDAO.users.size(),testUser.length);
    }

    @Test
    public void testDeleteUserFailed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(3),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        assertEquals(userFileDAO.users.size(),testUser.length);
    }

    @Test
    public void testDeleteUser() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test user array - 1 (because of the delete)
        assertEquals(userFileDAO.users.size(),testUser.length-1);
    }


    @Test
    public void testUpdateUser() throws IOException { //may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(2, "Bender", false, cart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getId());
        assertEquals(actual,user);
    }

    // -A 3/1
    @Test
    public void testUpdateUserNotFound() throws JsonProcessingException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Zapp", false, cart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));
                Product[] cart = new Product[100];
                User user = new User(3, "Zoidberg", false, cart);

        assertThrows(IOException.class,
                        () -> userFileDAO.createUser(user),
                        "IOException not thrown");
    }
  

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    
}
