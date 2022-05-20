package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ShoppingCartTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_cost = 0;

        // Invoke
        ShoppingCart cart = new ShoppingCart();

        // Analyze
        assertEquals(expected_cost,cart.getTotalCost());
        assertFalse(cart.getIsOrderComplete());
    }

    @Test
    public void testTotalCost() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();

        double expected_cost = 22.95;

        // Invoke        
        cart.setTotalCost(expected_cost);

        // Analyze
        assertEquals(expected_cost,cart.getTotalCost());
    }

    @Test
    public void testIsOrderComplete() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();

        // Invoke        
        cart.setIsOrderComplete(true);

        // Analyze
        assertTrue(cart.getIsOrderComplete());
    }

    @Test
    public void testAddToCartFalse() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();

        // Invoke        
        boolean result = cart.addToCart(null);

        // Analyze
        assertFalse(result);
    }

    @Test
    public void testAddToCartIncomplete() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);

        // Invoke        
        boolean result = cart.addToCart(product);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testAddToCartComplete() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.setIsOrderComplete(true);

        // Invoke        
        boolean result = cart.addToCart(product);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testAddToCartContains() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.addToCart(product);

        // Invoke        
        boolean result = cart.addToCart(product);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testRemoveFromCartMultiple() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.addToCart(product);
        cart.addToCart(product);

        // Invoke        
        boolean result = cart.removeFromCart(product);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testRemoveFromCart() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.addToCart(product);

        // Invoke        
        boolean result = cart.removeFromCart(product);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testRemoveFromCartFalse() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);

        // Invoke        
        boolean result = cart.removeFromCart(product);

        // Analyze
        assertFalse(result);
    }

    @Test
    public void testGetUpdatedProductsNull() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 1, 1, 1);
        cart.addToCart(product);
        cart.addToCart(product);


        // Analyze
        assertFalse(cart.getupdatedProducts());
    }

    @Test
    public void testGgetUpdatedProducts() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 1, 1, 1);
        cart.addToCart(product);


        // Analyze
        assertEquals(true, cart.getupdatedProducts());
    }

    @Test
    public void testCompletePurchase() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.addToCart(product);

        // Invoke        
        cart.completePurchase();

        // Analyze
        assertTrue(cart.getIsOrderComplete());
    }

    @Test
    public void testToString() {
        // Setup      
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("bowl", 3, 3, 3);
        cart.addToCart(product);
        String expected = "ShoppingCart [isOrderComplete=false, shoppingCart={name: bowl, price: 3.0, quantity: 3, engraving: =1}, totalCost=3.0]";

        // Invoke        
        String actual = cart.toString();

        // Analyze
        assertEquals(expected, actual);
    }
    
    
}
