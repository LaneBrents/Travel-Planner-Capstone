package com.example.d308vacationplanner;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTitleTest {

    @Test
    public void validTitleIsAccepted() {
        assertTrue(ValidationUtils.isValidTitle("Beach Vacation"));
    }

    @Test
    public void titleOverMaximumLengthIsRejected() {
        String longTitle = "A".repeat(101);

        assertFalse(ValidationUtils.isValidTitle(longTitle));
    }
}
