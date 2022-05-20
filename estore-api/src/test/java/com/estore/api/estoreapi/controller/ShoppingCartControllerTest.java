package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;

//import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.InventoryDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Shopping Cart Controller class
 * 
 * @author Caitlyn Cyrek
 */
@Tag("Controller-tier")
public class ShoppingCartControllerTest {
    private ShoppingCartController shoppingCartController;
    private UserDAO mockUserDAO;
    private InventoryDAO mockInventoryDAO;

    /**
     * Before each test, create a new Shopping Cart Controller object and inject
     * a mock Shopping Cart DAO
     */
    @BeforeEach
    public void setupShoppingCartController() {
        mockUserDAO = mock(UserDAO.class);
        mockInventoryDAO = mock(InventoryDAO.class);
        shoppingCartController = new ShoppingCartController(mockInventoryDAO, mockUserDAO);
    }

   @Test
    public void testGetShoppingCart() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(100, "tester", true, cart);
        // When the same user id is passed in, our mock shopping cart DAO will return
        // the cart object
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        ShoppingCart shoppingCart = user.getShoppingCart();
        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shoppingCart, response.getBody());
    }

    @Test
    public void testGetShoppingCartForAdmin() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(0, "admin", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        ShoppingCart shoppingCart = user.getShoppingCart();
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(user.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(shoppingCart, response.getBody());
    }

    @Test
    public void testGetShoppingCartForNull() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(3, "Freddy", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        ShoppingCart shoppingCart = user.getShoppingCart();
        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shoppingCart, response.getBody());
    }

    @Test
    public void testGetShoppingCartHandleException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int id = 99;
        doThrow(new IOException()).when(mockUserDAO).getUser(id);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddToShoppingCart() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(102, "testerThree", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(user.getId(), product.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getShoppingCart(), response.getBody());
    }

    @Test
    public void testAddToShoppingCartIsNull() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(0, "admin", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(user.getId(), product.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(user.getShoppingCart(), response.getBody());
    }

    @Test
    public void testAddToShoppingCartProductNotFound() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(103, "testerFour", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        // When addToShoppingCart is called, return the mock shoppingCart
        int id = 100; // product that does not exists

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(user.getId(), 100);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testAddToShoppingCartUserisLoggedOff() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(103, "testerFour", false, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);
        // When addToShoppingCart is called, return the mock shoppingCart

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(user.getId(), product.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testAddToShoppingCartHandleExceptionProductID() throws IOException { // getProducts may throw
                                                                                     // IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int userID = 1;
        int productID = 99;
        doThrow(new IOException()).when(mockUserDAO).getUser(userID);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(userID, productID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddToShoppingCartHandleExceptionUserID() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int userID = 99;
        int productID = 13;
        doThrow(new IOException()).when(mockUserDAO).getUser(userID);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.addToShoppingCart(userID, productID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromShoppingCart() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(102, "testerThree", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);
        user.addToCart(product);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(user.getId(),
                product.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getShoppingCart(), response.getBody());
    }

    @Test
    public void testRemoveFromShoppingCartError() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(102, "testerThree", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(user.getId(),
                product.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testRemoveFromShoppingCartHandleExceptionProductID() throws IOException { // getProducts may throw
        // IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int userID = 1;
        int productID = 99;
        doThrow(new IOException()).when(mockUserDAO).getUser(userID);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(userID, productID);
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromShoppingCartHandleExceptionUserID() throws IOException { // getProducts may throw
                                                                                       // IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int userID = 99;
        int productID = 13;
        doThrow(new IOException()).when(mockUserDAO).getUser(userID);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(userID, productID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromShoppingCartIsNull() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(0, "admin", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        // When addToShoppingCart is called, return the mock shoppingCart
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(user.getId(),
                product.getId());
        if (user.getShoppingCart() == null) {
            // Analyze
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(user.getShoppingCart(), response.getBody());

        } else {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(user.getShoppingCart(), response.getBody());
        }
    }

    @Test
    public void testRemoveFromShoppingCartUserisLoggedOff() throws IOException {
        // Setup
        Product[] cart = new Product[100];
        User user = new User(103, "testerFour", false, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);
        // When addToShoppingCart is called, return the mock shoppingCart

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.removeFromShoppingCart(user.getId(),
                product.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCompleteOrderAdmin() throws IOException {

        // Setup
        Product[] cart = new Product[100];
        User user = new User(4, "Frank", true, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        Product product = new Product("Winter bowl", 10, 5, 10);
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);
        user.addToCart(product);

        // Product[] products = shoppingCart.getupdatedProducts();
        ResponseEntity<ShoppingCart> response = shoppingCartController.completeOrder(user.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getShoppingCart(), response.getBody());

    }

    @Test
    public void testCompleteOrderLoggedOff() throws IOException {

        // Setup
        Product[] cart = new Product[100];
        User user = new User(1, "Juan", false, cart);
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);
        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.completeOrder(user.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCompleteOrderException() throws IOException {

        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        int userID = 99;        
        doThrow(new IOException()).when(mockUserDAO).getUser(userID);

        // Product[] products = shoppingCart.getupdatedProducts();
        ResponseEntity<ShoppingCart> response = shoppingCartController.completeOrder(userID);
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

}
