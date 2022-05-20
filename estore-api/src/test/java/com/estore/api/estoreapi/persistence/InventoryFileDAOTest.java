package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Inventory File DAO class
 * 
 * @author Team Error 451 (2b)
 */
@Tag("Persistence-tier")
public class InventoryFileDAOTest {
    InventoryFileDAO inventoryFileDAO;
    Product[] testInventory;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupInventoryFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testInventory = new Product[3];
        testInventory[0] = new Product("Small bowl", 5, 25, 10);
        testInventory[1] = new Product("Big plate", 3, 30, 7);
        testInventory[2] = new Product("Small plate", 34, 20, 3);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testInventory);
        inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testCreateProduct() throws IOException { //may throw IOException
        // Setup
        Product product = new Product("Cutting Board", 5, 3, 11);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = inventoryFileDAO.getProduct(result.getId());
        assertEquals(actual.getId(),result.getId());
        assertEquals(actual.getName(),product.getName());
        assertEquals(actual.getPrice(),product.getPrice());
        assertEquals(actual.getQuantity(),product.getQuantity());
        assertEquals(actual.getWoodType(),product.getWoodType());
        assertEquals(actual.getEngraving(),product.getEngraving());
    }

    @Test
    public void testCreateProductFailedName() throws IOException { //may throw IOException
        // Setup
        Product product = new Product("Small bowl", 5, 3, 11);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testCreateProductFailedQuantity() throws IOException { //may throw IOException
        // Setup
        Product product = new Product("Cutting Board", 5, 0, 11);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testGetProducts() throws IOException { //may throw IOException
        // Invoke
        Product[] products = inventoryFileDAO.getProducts();

        // Analyze
        assertEquals(products.length,products.length);
        for (int i = 0; i < products.length;++i)
            assertEquals(products[i],products[i]);
    }

    @Test
    public void testFindProducts() throws IOException { //may throw IOException
        // Invoke
        Product[] products = inventoryFileDAO.findProducts("la", null);

        // Analyze
        assertEquals(products.length,2);
        assertEquals(products[0],testInventory[2]);
        assertEquals(products[1],testInventory[1]);

        // Invoke
        Product[] productsPrice = inventoryFileDAO.findProducts(null, 9);

        // Analyze
        assertEquals(productsPrice.length,3);
        assertEquals(productsPrice[0],testInventory[2]);
        assertEquals(productsPrice[1],testInventory[1]);
    }

    @Test
    public void testGetProduct() throws IOException { //may throw IOException
        // Invoke
        Product product = inventoryFileDAO.getProduct(10);

        // Analzye
        assertEquals(product,testInventory[0]);
    }

    @Test
    public void testGetProductFailed() throws IOException { //may throw IOException
        // Invoke
        Product nullProduct = inventoryFileDAO.getProduct(99);

        //Analyze
        assertNull(nullProduct);
    }

    
    @Test
    public void testDeleteProductFailed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        assertEquals(inventoryFileDAO.products.size(),testInventory.length);
    }

    @Test
    public void testDeleteProduct() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct(10),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test inventory array - 1 (because of the delete)
        assertEquals(inventoryFileDAO.products.size(),testInventory.length-1);
    }


    @Test
    public void testUpdateProduct() throws IOException { //may throw IOException
        // Setup
        Product product = new Product("Small bowl", 5, 25, 10);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.updateProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = inventoryFileDAO.getProduct(product.getId());
        assertEquals(actual,product);
    }

    // -A 3/1
    @Test
    public void testUpdateProductNotFound() {
        // Setup
        Product product = new Product("Medium bowl", 3, 4, 5);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.updateProduct(product),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testUpdateProductFailedQuantity() {
        // Setup
        Product product = new Product("Small bowl", 5, -1, 10);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.updateProduct(product),
                                                "Unexpected exception thrown");
        Product nullProduct = assertDoesNotThrow(() -> inventoryFileDAO.getProduct(5),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
        assertNull(nullProduct);
    }

    
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

                Product product = new Product("Large Cutting Board", 8, 10 , 20);

        assertThrows(IOException.class,
                        () -> inventoryFileDAO.createProduct(product),
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
        // from the HeroFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}

