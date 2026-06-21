package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonContextMapContractTest {

    @Test
    public void map_with_suppress_exceptions_returns_null_on_missing_path() {
        Configuration config = Configuration.builder()
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        String json = "{\"1\":{\"2\":null}}";
        DocumentContext documentContext = JsonPath.using(config).parse(json);
        JsonPath query = JsonPath.compile("$.1.2.a.b.c");
        DocumentContext result = documentContext.map(query, (object, configuration) -> object);
        Assertions.assertNull(result);
    }

    @Test
    public void map_without_suppress_exceptions_throws_on_missing_path() {
        Configuration config = Configuration.builder()
                .build();
        String json = "{\"1\":{\"2\":null}}";
        DocumentContext documentContext = JsonPath.using(config).parse(json);
        JsonPath query = JsonPath.compile("$.1.2.a.b.c");
        Assertions.assertThrows(PathNotFoundException.class, () ->
                documentContext.map(query, (object, configuration) -> object));
    }

    @Test
    public void map_string_overload_consistent_with_path_overload_suppress() {
        Configuration config = Configuration.builder()
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        String json = "{\"1\":{\"2\":null}}";
        DocumentContext documentContext = JsonPath.using(config).parse(json);
        DocumentContext result = documentContext.map("$.1.2.a.b.c", (object, configuration) -> object);
        Assertions.assertNull(result);
    }

    @Test
    public void map_string_overload_consistent_with_path_overload_no_suppress() {
        Configuration config = Configuration.builder()
                .build();
        String json = "{\"1\":{\"2\":null}}";
        DocumentContext documentContext = JsonPath.using(config).parse(json);
        Assertions.assertThrows(PathNotFoundException.class, () ->
                documentContext.map("$.1.2.a.b.c", (object, configuration) -> object));
    }

    @Test
    public void map_returns_this_on_existing_path() {
        Configuration config = Configuration.defaultConfiguration();
        String json = "{\"store\":{\"book\":[{\"price\":5}]}}";
        DocumentContext documentContext = JsonPath.parse(json);
        DocumentContext result = documentContext.map("$.store.book[0].price", (object, configuration) -> object);
        Assertions.assertSame(documentContext, result);
    }

    @Test
    public void map_string_overload_returns_this_on_existing_path() {
        Configuration config = Configuration.defaultConfiguration();
        String json = "{\"store\":{\"book\":[{\"price\":5}]}}";
        DocumentContext documentContext = JsonPath.parse(json);
        DocumentContext result = documentContext.map("$.store.book[0].price", (object, configuration) -> object);
        Assertions.assertSame(documentContext, result);
    }
}
