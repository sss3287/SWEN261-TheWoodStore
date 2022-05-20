package com.estore.api.estoreapi.model;

/**
 * Listing of available wood types for product customization
 */
public enum WoodType {
    ASH ("Ash"),
    BIRCH ("Birch"),
    CEDAR ("Cedar"),
    CHERRY ("Cherry"),
    FIR ("Fir"),
    MAHOGONY ("Mahogany"),
    MAPLE ("Maple"),
    OAK ("Oak"),
    PINE ("Pine"),
    POPLAR ("Poplar"),
    REDWOOD ("Redwood"),
    TEAK ("Teak"),
    WALNUT ("Walnut");

    private String woodType;

    private WoodType (String woodType) {
        this.woodType = woodType;
    }

    /**
     * Only the first letter of the wood type will
     * be capitalized
     */
    @Override
    public String toString() {
        return woodType;
    }
    
}
