package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles REST API requests for the ShoppingCart resource
 * (no longer used)
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    private InventoryDAO inventoryDAO;
    private UserDAO userDAO;
    private InventoryController inventoryController;

    public ShoppingCartController(InventoryDAO inventoryDAO, UserDAO userDAO) {
        this.inventoryDAO = inventoryDAO;
        inventoryController = new InventoryController(inventoryDAO);
        this.userDAO = userDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain ShoppingCart shoppingCart} for
     * the given
     * user id
     * 
     * @param id The id used to locate the {@link User user} and get the users
     *           shoppingCart
     * 
     * @return ResponseEntity with {@link ShoppingCart shoppingCart} object and HTTP
     *         status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable int id) {
        LOG.info("GET /shoppingCart/" + id);
        try {
            User user = userDAO.getUser(id);
            if (!user.isUserAdmin(user)) {
                return new ResponseEntity<ShoppingCart>(user.getShoppingCart(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain ShoppingCart shoppingCart} for
     * the given
     * user id
     * 
     * @param id The id used to locate the {@link User user} and get the users
     *           shoppingCart
     * 
     * @return ResponseEntity with {@link ShoppingCart shoppingCart} object and HTTP
     *         status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/add/{userID}")
    public ResponseEntity<ShoppingCart> addToShoppingCart(@PathVariable int userID, @RequestParam int productID) {
        LOG.info("GET /shoppingCart/AddToCart " + userID);
        try {
            User user = userDAO.getUser(userID);
            Product product = inventoryDAO.getProduct(productID);
            if (user.addToCart(product)) {
                userDAO.updateUser(user);
                return new ResponseEntity<ShoppingCart>(user.getShoppingCart(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain ShoppingCart shoppingCart} for
     * the given
     * user id
     * 
     * @param id The id used to locate the {@link User user} and get the users
     *           shoppingCart
     * 
     * @return ResponseEntity with {@link ShoppingCart shoppingCart} object and HTTP
     *         status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/remove/{userID}")
    public ResponseEntity<ShoppingCart> removeFromShoppingCart(@PathVariable int userID, @RequestParam int productID) {
        LOG.info("GET /shoppingCart/RemoveFromCart " + userID);
        try {
            User user = userDAO.getUser(userID);
            Product product = inventoryDAO.getProduct(productID);
            if (user.removeFromCart(product)) {
                userDAO.updateUser(user);
                return new ResponseEntity<ShoppingCart>(user.getShoppingCart(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain ShoppingCart shoppingCart} for
     * the given
     * user id
     * 
     * @param id The id used to locate the {@link User user} and get the users
     *           shoppingCart
     * 
     * @return ResponseEntity with {@link ShoppingCart shoppingCart} object and HTTP
     *         status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/completeOrder/{userID}")
    public ResponseEntity<ShoppingCart> completeOrder(@PathVariable int userID) {
        LOG.info("GET /shoppingCart/CompleteOrder" + userID);
        try {
            User user = userDAO.getUser(userID);
            if (user.completeOrder()) {
                Product [] updateProducts = user.getShoppingCart().getUpdatedProducts();
                for(Product product: updateProducts){
                    inventoryController.updateProduct(product);
                }
                return new ResponseEntity<ShoppingCart>(user.getShoppingCart(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}