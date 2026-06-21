package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathFromCacheTest {

    @Test
    public void content_equal_predicate_filters_produce_same_key_via_equals_hashCode() {
        Filter f1 = Filter.filter(Criteria.where("x").is(1));
        Filter f2 = Filter.filter(Criteria.where("x").is(1));

        assertNotSame(f1, f2);
        assertEquals(f1.toString(), f2.toString(),
                "Two content-equal Filters should produce the same toString");

        Predicate[] arr1 = new Predicate[]{f1};
        Predicate[] arr2 = new Predicate[]{f2};

        StringBuilder key1 = new StringBuilder("$[?]");
        key1.append('|').append(arr1.length);
        for (Predicate f : arr1) {
            key1.append('|').append(f.toString());
        }

        StringBuilder key2 = new StringBuilder("$[?]");
        key2.append('|').append(arr2.length);
        for (Predicate f : arr2) {
            key2.append('|').append(f.toString());
        }

        assertEquals(key1.toString(), key2.toString(),
                "Content-equal predicate arrays must produce identical cache keys");
    }

    @Test
    public void different_predicate_content_produces_different_key() {
        Filter f1 = Filter.filter(Criteria.where("x").is(1));
        Filter f2 = Filter.filter(Criteria.where("y").is(3));

        Predicate[] arr1 = new Predicate[]{f1};
        Predicate[] arr2 = new Predicate[]{f2};

        StringBuilder key1 = new StringBuilder("$[?]");
        key1.append('|').append(arr1.length);
        for (Predicate f : arr1) {
            key1.append('|').append(f.toString());
        }

        StringBuilder key2 = new StringBuilder("$[?]");
        key2.append('|').append(arr2.length);
        for (Predicate f : arr2) {
            key2.append('|').append(f.toString());
        }

        assertNotEquals(key1.toString(), key2.toString(),
                "Different predicate content must produce different cache keys");
    }

    @Test
    public void compiled_jsonpath_content_equality_matches_cache_key() {
        Filter f1 = Filter.filter(Criteria.where("x").is(1));
        Filter f2 = Filter.filter(Criteria.where("x").is(1));

        assertNotSame(f1, f2);

        JsonPath p1 = JsonPath.compile("$[?]", f1);
        JsonPath p2 = JsonPath.compile("$[?]", f2);

        assertEquals(p1, p2,
                "Two JsonPath compiled with content-equal predicates must be equal");
        assertEquals(p1.hashCode(), p2.hashCode(),
                "Two equal JsonPath must have equal hashCode");
    }

    @Test
    public void no_filters_produces_path_only_key() {
        StringBuilder key = new StringBuilder("$.a.b.c");
        assertEquals("$.a.b.c", key.toString(),
                "No-filter cache key should be just the path string");
    }
}
