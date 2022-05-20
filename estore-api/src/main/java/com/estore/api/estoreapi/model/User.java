package com.estore.api.estoreapi.model;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Representation of estore user accounts
 * username is used for login
 * shoppingCart no longer in use
 */

public class User {

    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("isLoggedIn")
    private boolean isLoggedIn;
    @JsonProperty("cart")
    private Product[] cart;
    @JsonIgnore
    private ShoppingCart shoppingCart;
    @JsonIgnore
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    public User(@JsonProperty("id") int id, @JsonProperty("username") String username,
            @JsonProperty("isLoggedIn") boolean isLoggedIn, @JsonProperty("cart") Product[] cart) throws JsonProcessingException {
        this.id = id;
        this.username = username;
        this.isLoggedIn = isLoggedIn;
        if (this.username.equals("admin")) {
            this.shoppingCart = null;
            this.cart = null;
        } else {
            this.shoppingCart = new ShoppingCart();
            this.cart = cart;
        }
    }

    /**
     * returns user's unique id number
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * updates user's cart
     * @param cart
     */
    public void setCart(Product[] cart) {
        this.cart = cart;
    }

    /**
     * returns user specific cart
     * @return cart
     */
    public Product[] getCart() {
        return cart;
    }

    /**
     * returns user's unique name
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * updates user's name
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets user's log in status
     * @return boolean isLoggedIn
     */
    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    /**
     * updates user's log in status
     * @param isLoggedIn
     */
    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    /**
     * returns the user specific shoppingCart
     * @return shoppingCart
     */
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * adds product to shoppingCart
     * @param product
     * @return
     */
    public boolean addToCart(Product product) {
        if (this.id != 0 && isLoggedIn) {
            return shoppingCart.addToCart(product);
        }
        LOG.info("User - addToCart() failed. Invalid User(admin/loggedOff)");
        return false;
    }

    public boolean removeFromCart(Product product) {
        if (this.id != 0 && isLoggedIn) {
            return shoppingCart.removeFromCart(product);
        }
        LOG.info("User - removeFromCart() failed. Invalid User(admin/loggedOff)");
        return false;

    }

    /**
     * user must not be admin and must be logged in to complete their purchase
     * @return boolean valid order
     */
    public boolean completeOrder() {
        boolean isValid = false;
        if (this.id != 0 && isLoggedIn) {
            if (shoppingCart.completePurchase()) {
                isValid = true;
            }
        }else{
            LOG.info("User - completeOrder() failed. Invalid User(admin/loggedOff)");
        }        
        return isValid;
    }

    /**
     * Check to see if the user is the admin (admin has an id value of 0)
     * 
     * @param user user object
     * @return
     */
    public boolean isUserAdmin(User user) {
        if (user.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    /**
     * {inherit doc}
     */
    public int hashCode() {
        return this.id + this.username.hashCode();
    }

    @Override
    /**
     * {inherit doc}
     */
    public boolean equals(Object other) {
        boolean result = true;
        if (other instanceof User) {
            User otherUser = (User) other;
            result = this.username.equals(otherUser.username) && this.id == otherUser.id;
        }
        return result;
    };

}
