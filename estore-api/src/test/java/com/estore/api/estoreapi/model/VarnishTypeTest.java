package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VarnishTypeTest {
    @Test
    public void testToString() {
        // Setup
        String expected_string = "Semi-Gloss";

        // Invoke
        VarnishType varnishType = VarnishType.SEMIGLOSS;

        // Analyze
        assertEquals(expected_string, varnishType.toString());
    }
    
}
