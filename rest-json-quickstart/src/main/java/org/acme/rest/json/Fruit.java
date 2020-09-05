package org.acme.rest.json;

import org.jboss.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Fruit {

    public String name;
    public String description;
    public Family family;
    @JsonbTypeAdapter(Family.BeanAdapter2.class)
    public Set<Family> families = new HashSet<>(Arrays.asList(Family.values()));

    public Fruit() {
    }

    @JsonbCreator
    public Fruit(final String name, final String description, final Family family) {
        this.name = name;
        this.description = description;
        this.family = family;
    }

    @javax.json.bind.annotation.JsonbTypeAdapter(Family.BeanAdapter.class)
    public enum Family {
        AGGREGATE("aggregate"), SIMPLE("simple"), COMPLEX("complex"), _4TEST("4test");


        private String value;

        Family(final String value) {
            this.value = value;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @JsonbCreator
        @Override
        public String toString() {
            return this.value;
        }

        public static Family fromValue(final String value) {
            for (final Family b : Family.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }


        @Provider
        static public class BeanAdapter implements JsonbAdapter<Family, String> {

            private static final Logger LOGGER = Logger.getLogger(BeanAdapter.class);

            @Override
            public String adaptToJson(final Family e) throws Exception {
                LOGGER.infov("adaptToJson(e={0})", e.name());
                return e.value;
            }

            @Override
            public Family adaptFromJson(final String value) throws Exception {
                LOGGER.infov("adaptFromJson(value={0})", value);
                return Family.fromValue(value);
            }

        }

        @Provider
        static public class BeanAdapter2 implements JsonbAdapter<Set<Family>, JsonArray> {

            private static final Logger LOGGER = Logger.getLogger(BeanAdapter.class);

            @Override
            public JsonArray adaptToJson(final Set<Family> e) throws Exception {
                return Json.createArrayBuilder(e.stream().map(s -> s.value).collect(Collectors.toList())).build();
            }

            @Override
            public Set<Family> adaptFromJson(final JsonArray value) throws Exception {
                return new HashSet<>(Arrays.asList(Family.values()));
            }

        }
    }
}
