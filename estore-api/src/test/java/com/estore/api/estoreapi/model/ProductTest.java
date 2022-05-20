package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 11;
        String expected_name = "Owl-Bowl";
        double expected_price = 10;        
        int expected_quantity = 50;

        // Invoke
        Product product = new Product(expected_name, expected_price, expected_quantity, expected_id);

        // Analyze
        assertEquals(expected_id,product.getId());
        assertEquals(expected_name,product.getName());
        assertEquals(expected_price, product.getPrice());
        assertEquals(expected_quantity, product.getQuantity());
    }

    @Test
    public void testProductAll() {
        // Setup
        String str = "11, Owl-Bowl, 12.99, 10, Breathe";

        // Invoke
        Product product = new Product(str);

        // Analyze
        assertEquals(11,product.getId());
        assertEquals("Owl-Bowl",product.getName());
        assertEquals(12.99, product.getPrice());
        assertEquals(10, product.getQuantity());
        assertEquals("Breathe", product.getEngraving());
    }

    @Test
    public void testName() {
        // Setup      
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        String expected_name = "Owl-Bowl";

        // Invoke        
        product.setName(expected_name);

        // Analyze
        assertEquals(expected_name,product.getName());
    }
  
    @Test
    public void testPrice() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        int expected_price = 15;

        // Invoke
        product.setPrice(expected_price);

        // Analyze
        assertEquals(expected_price,product.getPrice());
    }
    @Test
    public void testQuantity() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        int expected_quantity = 100;
        // Invoke
        product.setQuantity(expected_quantity);

        // Analyze
        assertEquals(expected_quantity,product.getQuantity());
    }

    @Test
    public void testToString() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);
        String expected_string = "name: Wi-Fire, price: 10.0, quantity: 50, engraving: ";

        // Invoke
        String actual_string = product.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
    
    @Test
    public void testSetWoodType() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        WoodType expected_woodType = WoodType.WALNUT;

        // Invoke
        product.setWoodType(WoodType.WALNUT);

        // Analyze
        assertEquals(expected_woodType, product.getWoodType());
    }

    @Test
    public void testSetVarnishType() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        VarnishType expected_varnishType = VarnishType.GLOSS;

        // Invoke
        product.setVarnishType(VarnishType.GLOSS);

        // Analyze
        assertEquals(expected_varnishType, product.getVarnishType());
    }

    @Test
    public void testEngraving() {
        // Setup
        String name = "Wi-Fire";
        double price = 10;        
        int quantity = 50;
        int id = 11;
        Product product = new Product(name, price, quantity, id);

        String expected_engraving = "Best Dad <3";

        // Invoke
        product.setEngraving("Best Dad <3");

        // Analyze
        assertEquals(expected_engraving, product.getEngraving());
    }

    @Test
    public void testEquals() throws JsonProcessingException {
        // Setup      
        String name = "bowl";
        int id = 1;     
        Product product = new Product(name, 12, 3, id);
        Product product2 = new Product(name, 15, 2, id);

        // Invoke        
        boolean result = product.equals(product2);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void testEqualsFailed() throws JsonProcessingException {
        // Setup      
        String name = "bowl";
        int id = 1;     
        Product product = new Product(name, 12, 3, id);
        Object product2 = new Object();

        // Invoke        
        boolean result = product.equals(product2);

        // Analyze
        assertFalse(result);
    }

    @Test
    public void testEqualsFalseName() throws JsonProcessingException {
        // Setup      
        String name = "bowl";
        String name2 = "spoon";
        int id = 1;     
        Product product = new Product(name, 12, 3, id);
        Product product2 = new Product(name2, 12, 3, id);

        // Invoke        
        boolean result = product.equals(product2);

        // Analyze
        assertFalse(result);
    }
    @Test
    public void testEqualsFalseId() throws JsonProcessingException {
        // Setup      
        String name = "bowl";
        int id = 1;
        int id2 = 2;     
        Product product = new Product(name, 12, 3, id);
        Product product2 = new Product(name, 12, 3, id2);

        // Invoke        
        boolean result = product.equals(product2);

        // Analyze
        assertFalse(result);
    }
}
