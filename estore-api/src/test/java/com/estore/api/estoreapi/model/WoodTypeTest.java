package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WoodTypeTest {
    @Test
    public void testToString() {
        // Setup
        String expected_string = "Cherry";

        // Invoke
        WoodType woodType = WoodType.CHERRY;

        // Analyze
        assertEquals(expected_string, woodType.toString());
    }
    
}
