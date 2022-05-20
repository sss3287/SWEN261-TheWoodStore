package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testCtor() throws JsonProcessingException {
        // Setup
        int expected_id = 11;
        String expected_name = "Amy";
        boolean expected_logOn = false;
        Product[] expected_cart = new Product[100];

        // Invoke
        
        User user = new User(expected_id, expected_name, expected_logOn, expected_cart);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_name,user.getUsername());
        assertEquals(expected_logOn,user.getIsLoggedIn());
        assertEquals(expected_cart, user.getCart());
        assertNotNull(user.getShoppingCart());
    }

    @Test
    public void testCtorAdmin() throws JsonProcessingException {
        // Setup
        int expected_id = 12;
        String expected_name = "admin";
        boolean expected_logIn = true;
        Product[] expected_cart = new Product[100];

        // Invoke
        User user = new User(expected_id, expected_name, expected_logIn, expected_cart);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_name,user.getUsername());
        assertEquals(expected_logIn,user.getIsLoggedIn());
        assertNull(user.getShoppingCart());
    }

    @Test
    public void testName() throws JsonProcessingException {
        // Setup      
        String username = "Wi-Fire";
        int id = 10;        
        boolean expected_logIn = false;
        Product[] cart = new Product[100];
        User user = new User(id, username, expected_logIn, cart);

        String expected_name = "Wi-Fire";

        // Invoke        
        user.setUsername(expected_name);

        // Analyze
        assertEquals(expected_name,user.getUsername());
    }

    @Test
    public void testIsLoggedIn() throws JsonProcessingException {
        // Setup      
        String username = "Wi-Fire";
        int id = 10;        
        boolean logIn = false;
        Product[] cart = new Product[100];
        User user = new User(id, username, logIn, cart);

        // Invoke        
        user.setLoggedIn(true);

        // Analyze
        assertTrue(user.getIsLoggedIn());
    }

    @Test
    public void testisUserAdmin() throws JsonProcessingException {
        // Setup      
        String username = "admin";
        int id = 0;        
        boolean logIn = false;
        Product[] cart = new Product[100];
        User user = new User(id, username, logIn, cart);

        // Invoke        
        boolean userAdmin = user.isUserAdmin(user);

        // Analyze
        assertTrue(userAdmin);
    }

    @Test
    public void testisUserAdminNullCart() throws JsonProcessingException {
        // Setup      
        String username = "admin";
        int id = 0;        
        boolean logIn = false;
        Product[] cart = new Product[100];
        User user = new User(id, username, logIn, cart);

        // Invoke        
        Product[] userCart = user.getCart();

        // Analyze
        assertNull(userCart);
    }

    @Test
    public void testisUserAdminFalse() throws JsonProcessingException {
        // Setup      
        String username = "Leela";
        int id = 1;        
        boolean logIn = false;
        Product[] cart = new Product[100];
        User user = new User(id, username, logIn, cart);

        // Invoke        
        boolean userAdmin = user.isUserAdmin(user);

        // Analyze
        assertFalse(userAdmin);
    }

    @Test
    public void testEquals() throws JsonProcessingException {
        // Setup      
        String username = "admin";
        int id = 0;        
        boolean logIn = false;
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        User user = new User(id, username, logIn, cart1);
        User user2 = new User(id, username, logIn, cart2);

        // Invoke        
        boolean equalUsers = user.equals(user2);

        // Analyze
        assertTrue(equalUsers);
    }

    @Test
    public void testEqualsFalseName() throws JsonProcessingException {
        // Setup      
        String username = "admin";
        int id = 0;        
        boolean logIn = false;
        String username2 = "Fry";
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        User user = new User(id, username, logIn, cart1);
        User user2 = new User(id, username2, logIn, cart2);

        // Invoke        
        boolean equalUsers = user.equals(user2);

        // Analyze
        assertFalse(equalUsers);
    }

    @Test
    public void testEqualsFalseId() throws JsonProcessingException {
        // Setup      
        String username = "admin";
        int id = 0;        
        boolean logIn = false;
        int id2 = 1;
        Product[] cart1 = new Product[100];
        Product[] cart2 = new Product[100];
        User user = new User(id, username, logIn, cart1);
        User user2 = new User(id2, username, logIn, cart2);

        // Invoke        
        boolean equalUsers = user.equals(user2);

        // Analyze
        assertFalse(equalUsers);
    }
    
}
