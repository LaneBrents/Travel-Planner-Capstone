package com.example.d308vacationplanner;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsDateTest {

    @Test
    public void validDateIsAccepted() {
        assertTrue(ValidationUtils.isValidDate("08/15/2026"));
    }

    @Test
    public void invalidDateIsRejected() {
        assertFalse(ValidationUtils.isValidDate("02/30/2026"));
    }
}