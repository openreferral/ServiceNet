package org.benetech.servicenet.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntityValidatorTest {

    @Test
    public void shouldReturnFalseForNull() {
        assertFalse(EntityValidator.isValid(null, null, null));
    }

    @Test
    public void shouldReturnTrueForNull() {
        assertTrue(EntityValidator.isNotValid(null, null, null));
    }
}
