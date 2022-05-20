package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Inventory Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class InventoryControllerTest {
    private InventoryController inventoryController;
    private InventoryDAO  mockInventoryDAO;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock Inventory DAO
     */
    @BeforeEach
    public void setupInvenoryController() {
        mockInventoryDAO = mock(InventoryDAO.class);
        inventoryController = new InventoryController(mockInventoryDAO);
    }

    @Test
    public void testGetProduct() throws IOException {  // getProduct may throw IOException
        // Setup
        Product product = new Product("Winter Bowl", 10, 5, 10);
        // When the same id is passed in, our mock product DAO will return the product object
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(product.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testGetProductNotFound() throws Exception { // getProduct may throw IOException
        // Setup
        int productId = 10;
        // When the same id is passed in, our mock Product DAO will return null, simulating
        // no Product found
        when(mockInventoryDAO.getProduct(productId)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(productId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteProduct() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 10;
        // when deleteInventory is called return true, simulating successful deletion
        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 10;
        // when deleteProduct is called return false, simulating failed deletion
        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(false);
        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);
    }

    @Test
    public void testGetProducts() throws IOException { // getProducts may throw IOException
        // Setup
        Product[] products = new Product[2];
        products[0] = new Product("Small Bowl", 45, 4, 10);
        products[1] = new Product("Big Bowl", 50, 6, 11);
        // When getProducts is called return the products created above
        when(mockInventoryDAO.getProducts()).thenReturn(products);

        // Invoke
        ResponseEntity<Product[]> response = inventoryController.getProducts();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(products,response.getBody());
    }

    @Test
    public void testGetProductsHandleException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getProducts();

        // Invoke
        ResponseEntity<Product[]> response = inventoryController.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // @Test
    // public void testSearchProducts() throws IOException { // findProducts may throw IOException
    //     // Setup
    //     String searchString = "ow";
    //     Product[] products = new Product[2];
    //     // name, price, quantitiy, id
    //     products[0] = new Product("Fall Bowl", 10, 3, 11);
    //     products[1] = new Product("Summer Bowl", 10, 8, 12);
    //     // When findProducts is called with the search string, return the two products above
    //     when(mockInventoryDAO.findProducts(searchString, null)).thenReturn(products);

    //     Map<String, String> searchTestMap = new HashMap<String, String>();
    //     searchTestMap.put(searchString, null);
        
    //     // Invoke
    //     ResponseEntity<Product[]> response = inventoryController.searchProducts(searchTestMap);

    //     // Analyze
    //     assertEquals(HttpStatus.OK,response.getStatusCode());
    //     assertEquals(products,response.getBody());
    // }
    
    // @Test
    // public void testSearchProductsHandleException() throws IOException { // findProducts may throw IOException
    //     // Setup
    //     String searchString = "an";
    //     // When createProduct is called on the Mock Product DAO, throw an IOException
    //     doThrow(new IOException()).when(mockInventoryDAO).findProducts(searchString, null);

    //     Map<String, String> searchTestMap = new HashMap<String, String>();
    //     searchTestMap.put(searchString, null);

    //     // Invoke
    //     ResponseEntity<Product[]> response = inventoryController.searchProducts(searchTestMap);

    //     // Analyze
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    // }


    @Test
    public void testGetProductHandleException() throws Exception { // createProduct may throw IOException
        // Setup
        int userId = 99;
        // When getProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(userId);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateProduct() throws IOException { // updateProduct may throw IOException
        // Setup
        String name = "Test-Name";
        double price = 2.50;
        int quantity = 0;
        int id = 56;
        Product testProduct = new Product(name, price, quantity, id);
        // when updateProduct is called, return true simulating successful
        // update and save
        when(mockInventoryDAO.updateProduct(testProduct)).thenReturn(testProduct);
        ResponseEntity<Product> response = inventoryController.updateProduct(testProduct);
        testProduct.setName("New-Name");

        // Invoke
        response = inventoryController.updateProduct(testProduct);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(testProduct,response.getBody());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 10;
        // When deleteProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).deleteProduct(productId);

        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);
    }

    @Test
    public void testUpdateProductHandleException() throws IOException { // updateProduct may throw IOException
        // Setup
        String name = "Test-Name";
        double price = 2.50;
        int quantity = 0;
        int id = 56;
        Product testProduct = new Product(name, price, quantity, id);
        // When updateProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).updateProduct(testProduct);

        // Invoke
        ResponseEntity<Product> response = inventoryController.updateProduct(testProduct);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateProduct() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product("Bowl", 12.99, 1, 1);
        // when createProduct is called, return true simulating successful
        // creation and save
        when(mockInventoryDAO.createProduct(product)).thenReturn(product);

        // Invoke
        ResponseEntity<Product> response = inventoryController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testCreateProductFailed() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product("Bowl", 12.99, 1, 1);
        // when createProduct is called, return false simulating failed
        // creation and save
        when(mockInventoryDAO.createProduct(product)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = inventoryController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateProductHandleException() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product("Bowl", 12.99, 1, 1);

        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).createProduct(product);

        // Invoke
        ResponseEntity<Product> response = inventoryController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
        

}
