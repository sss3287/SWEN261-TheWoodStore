package com.estore.api.estoreapi.model;

import java.util.HashMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * this version of the shopping cart is no longer in used
 */
public class ShoppingCart {

    // Key-Value pair product/quantity
    // Key might change to product id depending on how controller is implemented for
    // shoppingCart
    // Dont need
    // @JsonProperty("shoppingCart")
    // @JsonDeserialize(keyUsing = shoppingCartKeyDeserializer.class)
    private HashMap<Product, Integer> shoppingCart; // shoppingCart contains a product and the amount of that product we
                                                    // have

    @JsonProperty("isOrderComplete")
    private boolean isOrderComplete;

    @JsonProperty("totalCost")
    private double totalCost;

    @JsonCreator
    public ShoppingCart() {
        this.shoppingCart = new HashMap<>();
        this.isOrderComplete = false;
        this.totalCost = 0;
    }

    @JsonIgnore
    private static final Logger LOG = Logger.getLogger(ShoppingCart.class.getName());

    @JsonIgnore
    Product [] updatedProducts;


    public Product[] getUpdatedProducts() {
        return updatedProducts;
    }

    public HashMap<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * 
     * @return
     */
    public boolean getIsOrderComplete() {
        return isOrderComplete;
    }

    /**
     * 
     * @param isOrderComplete
     */
    public void setIsOrderComplete(boolean isOrderComplete) {
        this.isOrderComplete = isOrderComplete;
    }

    /**
     * Retrieves the shopping cart
     * 
     * @return shopping cart
     */
    // public HashMap<Product, Integer> getCart() {
    // return shoppingCart;
    // }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Remove products from cart
     * 
     * @param product The product
     */
    @JsonIgnore
    public boolean addToCart(Product product) {
        if (product == null) {
            LOG.info("ShoppingCart - addToCart() failed. Product DNE");
            return false;
        } else if (shoppingCart.containsKey(product)) {
            int newQuantity = shoppingCart.get(product) + 1;
            shoppingCart.put(product, newQuantity);
            setFinalTotalCost();
            return true;
        } else {
            if (this.isOrderComplete == true) {
                this.isOrderComplete = false;
            }
            shoppingCart.put(product, 1);
            setFinalTotalCost();
            return true;
        }
    }

    /**
     * 
     * @param product
     * @return
     */
    @JsonIgnore
    public boolean removeFromCart(Product product) {
        if (product == null) {
            LOG.info("ShoppingCart - removeFromCart() failed. Product DNE");
            return false;
        } else if (shoppingCart.containsKey(product)) {
            if (shoppingCart.get(product) == 1) {
                shoppingCart.remove(product);
            } else {
                int newQuantity = shoppingCart.get(product) - 1;
                shoppingCart.put(product, newQuantity);

            }
            setFinalTotalCost();
            return true;
        } else {
            LOG.info("ShoppingCart - removeFromCart() failed. cart does not contain this product.");
            return false;
        }
    }

    /**
     * Calculate total cost of products in the shopping cart
     * 
     * @return total cost of products
     */
    @JsonIgnore
    public void setFinalTotalCost() {
        this.totalCost = 0;
        for (HashMap.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPrice();
            this.totalCost += (price * quantity);
        }
        setTotalCost(this.totalCost);
    }

    @JsonIgnore
    public boolean getupdatedProducts() {
        this.updatedProducts = new Product[shoppingCart.keySet().toArray().length];
        int newQuantity;
        Product newProduct;
        boolean validPurchase = true;
        int i = 0;
        if(shoppingCart.entrySet().size() == 0){
            LOG.info("ShoppingCart - getUpdatedProducts() failed. Empty Cart");
            validPurchase = false;
            return validPurchase;
        }
        for (HashMap.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (product.getQuantity() >= quantity) {
                newQuantity = product.getQuantity() - quantity;
                newProduct = new Product(product.getName(), product.getPrice(), newQuantity, product.getId());
                updatedProducts[i++] = newProduct;
            } else {
                LOG.info("ShoppingCart - getUpdatedProducts() failed. Invalid purchase, update quantity amount of " + product.getName());
                validPurchase = false;
                break;
            }
        }
        return validPurchase;
    }

    /**
     * Will clear the cart once purchase is complete.
     */
    public boolean completePurchase() {
        if(getupdatedProducts()){
            this.shoppingCart.clear();
            this.isOrderComplete = true;
            return true;
        }
        else{
           return false;
        }        
    }

    @Override
    public String toString() {
        return "ShoppingCart [isOrderComplete=" + isOrderComplete + ", shoppingCart=" + shoppingCart + ", totalCost="
                + totalCost + "]";
    }
}
