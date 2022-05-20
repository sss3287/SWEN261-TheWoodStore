package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * representation of each product in inventory
 */
public class Product {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;
    @JsonProperty("price")
    private double price;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("woodType")
    private WoodType woodType;
    @JsonProperty("varnishType")
    private VarnishType varnishType;
    @JsonProperty("engraving")
    private String engraving;

    /**
     * Create a product with a name, price, and quantity
     * 
     * @param name     The name of the product
     * @param price    The price of the product
     * @param quantity How much of this product is available
     * @param id       The id number of the product
     */
    public Product(@JsonProperty("name") String name, @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity, @JsonProperty("id") int id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
        engraving = "";
    }

    public Product(String all) {
        String[] units = all.split(",");
        this.id = Integer.parseInt(units[0].trim());
        this.name = units[1].trim();
        this.price = Double.parseDouble(units[2].trim());
        this.quantity = Integer.parseInt(units[3].trim());
        engraving = units[4].trim();
    }

    /**
     * Retrieves the id of the product
     * 
     * @return The id of the product
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the product
     * 
     * @return The name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product
     * 
     * @param name name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the price of the product
     * 
     * @return The price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the price of the product
     * 
     * @param price price of the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the quantity of the product
     * 
     * @return The quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of the product
     * 
     * @param quantity Number of product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the text for engraving on the product
     * 
     * @return The text to be engraved on the product
     */
    public String getEngraving() {
        return engraving;
    }

    /**
     * Puts what the customer wants engraved on the product
     * 
     * @param engraving The text to be engraved on the product
     */
    public void setEngraving(String engraving) {
        this.engraving = engraving;
    }

    /**
     * Retrieves the type of wood of the product
     * 
     * @return The type of wood of the product
     */
    public WoodType getWoodType() {
        return woodType;
    }

    /**
     * Sets the type of wood for the product
     * 
     * @param woodType The type of wood for the product
     */
    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

    /**
     * returns varnish type of product
     * (gloss, semi-gloss, satin, matte, or null)
     * 
     * @return varnish type
     */
    public VarnishType getVarnishType() {
        return varnishType;
    }

    /**
     * Sets the type of varnish for the product
     * @param varnishType
     */
    public void setVarnishType(VarnishType varnishType) {
        this.varnishType = varnishType;
    }

    @Override
    /**
     * {inherit doc}
     */
    public int hashCode() {
        return this.id + this.name.hashCode() + this.quantity + (int) this.price + this.engraving.hashCode();
    }

    @Override
    /**
     * {inherit doc}
     */
    public boolean equals(Object other) {
        if (!(other instanceof Product)) {
            return false;
        }
        else {
            Product otherProduct = (Product) other;
            boolean result = this.name.equals(otherProduct.name) && this.id == otherProduct.id;
            return result;
        }
    };

    /**
     * String representation of product
     */
    @Override
    // @JsonValue
    public String toString() {
        return "name: " + name + ", price: " + price + ", quantity: " + quantity + ", engraving: " + engraving;
    }
}
