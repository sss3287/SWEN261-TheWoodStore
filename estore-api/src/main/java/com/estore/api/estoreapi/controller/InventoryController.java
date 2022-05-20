package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles REST API requests for the Product resource
 */
@RestController
@RequestMapping("products")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDao;

    /**
     * Creates a REST API controller to repond to requests
     * 
     * @param inventoryDao The {@link InventoryDAO Inventory Data Access Object} to
     *                     perform CRUD operations
     *                     <br>
     *                     This dependency is injected by the Spring Framework
     */
    public InventoryController(InventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Responds to the GET request for all {@linkplain Product product} whose name
     * contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the
     *             {@link Product product}
     * 
     * @return ResponseEntity with array of {@link Product product} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all heroes that contain the text "ma"
     *         GET http://localhost:8080/heroes/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Product[]> searchProducts(@RequestParam Map<String, String> allParams) {
        String name = allParams.get("name");
        Integer price;
        try{
            price = Integer.parseInt(allParams.get("price"));
        }
        catch (Exception e){
            price = null;
        }
        LOG.info("GET /product/?name=" + name  + "&price=" + price);

        try {
            Product[] products = inventoryDao.findProducts(name, price);
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Product product} with the provided product object
     * 
     * @param product The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product product} object and HTTP
     *         status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Product
     *         product} object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        LOG.info("POST /products " + product);
        try {
            Product newProduct = inventoryDao.createProduct(product);
            if (newProduct != null)
                return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain product product} with the provided
     * {@linkplain product product} object, if it exists
     * 
     * @param product The {@link product product} to update
     * 
     * @return ResponseEntity with updated {@link product product} object and HTTP
     *         status of OK if updated
     *         ResponseEntity with HTTP status of OK if product does not initially
     *         exist but is created and added to inventory
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        LOG.info("PUT /products " + product);
        try {
            Product updatedProduct = inventoryDao.updateProduct(product);
            return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given
     * id
     * 
     * @param id The id used to locate the {@link Product product}
     * 
     * @return ResponseEntity with {@link Product product} object and HTTP status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        LOG.info("GET /products/" + id);
        try {
            Product product = inventoryDao.getProduct(id);
            if (product != null)
                return new ResponseEntity<Product>(product, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain PRODUCT products}
     *
     * @return ResponseEntity with array of {@link Product product} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET /products");

        try {
            Product[] products = inventoryDao.getProducts();
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the given id
     * 
     * @param id The id of the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id) {
        LOG.info("DELETE /products/" + id);
        try {
            boolean product = inventoryDao.deleteProduct(id);
            if (product != false)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
