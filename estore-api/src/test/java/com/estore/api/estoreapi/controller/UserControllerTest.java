package com.estore.api.estoreapi.controller;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO  mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupInvenoryController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetUser() throws IOException {  // getUser may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Carlos", true, cart);
        // When the same id is passed in, our mock user DAO will return the user object
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // getUser may throw IOException
        // Setup
        int userId = 10;
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no User found
        when(mockUserDAO.getUser(userId)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 10;
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(userId)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 10;
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(userId)).thenReturn(false);
        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);
    }

    @Test
    public void testGetUsers() throws IOException { // getUsers may throw IOException
        // Setup
        User[] users = new User[2];
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        users[0] = new User(1, "Carlos", true, cart1);
        users[1] = new User(2, "Juan", false, cart2);
        // When getUsers is called return the users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testGetUsersHandleException() throws IOException { // getUsers may throw IOException
        // Setup
        // When getUsers is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException { // findUsers may throw IOException
        // Setup
        String searchString = "ow";
        User[] users = new User[2];
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        // name, price, quantitiy, id, cart
        users[0] = new User(2, "Juan", false, cart1);
        users[1] = new User(3, "Carlos", true, cart2);
        // When findUsers is called with the search string, return the two users above
        when(mockUserDAO.findUsers(searchString)).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }
    
    @Test
    public void testSearchUsersHandleException() throws IOException { // findUsers may throw IOException
        // Setup
        String searchString = "an";
        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).findUsers(searchString);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testGetUserHandleException() throws Exception { // createUser may throw IOException
        // Setup
        int userId = 99;
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        String name = "Test-Name";
        boolean isLoggedIn = false;
        int id = 56;
        Product[] cart = new Product[100];
        User testUser = new User(id, name, isLoggedIn, cart);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(testUser)).thenReturn(testUser);
        ResponseEntity<User> response = userController.updateUser(testUser);
        testUser.setUsername("New-Name");

        // Invoke
        response = userController.updateUser(testUser);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(testUser,response.getBody());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 10;
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        String name = "Test-Name";
        boolean isLoggedIn = true;
        int id = 56;
        Product[] cart = new Product[100];
        User testUser = new User(id, name, isLoggedIn, cart);
        // When updateUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(testUser);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(testUser);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Carlos", true, cart);
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(0, "admin", true, cart);
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] cart = new Product[100];
        User user = new User(0, "admin", true, cart);

        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    
}
