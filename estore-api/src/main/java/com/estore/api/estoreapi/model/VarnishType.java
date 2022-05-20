package com.estore.api.estoreapi.model;

/**
 * Listing of available varnish types for product customization
 */
public enum VarnishType {
    GLOSS ("Gloss"),
    SEMIGLOSS ("Semi-Gloss"),
    SATIN ("Satin"),
    MATTE ("Matte");

    private String varnishType;

    private VarnishType (String varnishType) {
        this.varnishType = varnishType;
    }

    /**
     * Only the first letter of the varnish type will
     * be capitalized
     */
    @Override
    public String toString() {
        return varnishType;
    }
    
}
