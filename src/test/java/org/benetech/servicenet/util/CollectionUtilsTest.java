package org.benetech.servicenet.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CollectionUtilsTest {

    private static final int THREE = 3;

    @Test
    public void shouldRemoveNullsFromSet() {
        Set<String> entry = new HashSet<>();
        entry.add("one");
        entry.add("two");
        entry.add(null);

        assertEquals(THREE, entry.size());

        assertEquals(2, CollectionUtils.filterNulls(entry).size());
    }

    @Test
    public void shouldRemoveNullsFromList() {
        List<String> entry = new ArrayList<>();
        entry.add("one");
        entry.add("two");
        entry.add(null);

        assertEquals(THREE, entry.size());

        assertEquals(2, CollectionUtils.filterNulls(entry).size());
    }
}
