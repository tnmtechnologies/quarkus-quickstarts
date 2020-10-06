package org.acme.rest.json;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.ws.rs.ext.Provider;

public class Fruit {

    public String name;
    public String description;
    public Family family;
    @JsonbTypeAdapter(Family.FamiliesJsonbAdapter.class)
    public Set<Family> families = null;

    public Fruit() {
    }


    public Fruit(final String name, final String description, final Family family, final Set<Family> families) {
        this.name = name;
        this.description = description;
        this.family = family;
        this.families = families;
    }

    @javax.json.bind.annotation.JsonbTypeAdapter(Family.BeanAdapter.class)
    public enum Family {
        AGGREGATE("aggregate")
        , SIMPLE("simple")
        , COMPLEX("complex")
        , _4TEST("4test")
        ;


        private String value;

        Family(final String value) {
            this.value= value;
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


        @javax.ws.rs.ext.Provider
        static public class BeanAdapter implements JsonbAdapter<Family, String> {

            @Override
            public String adaptToJson(final Family e) throws Exception {
                return e.value;
            }

            @Override
            public Family adaptFromJson(final String value) throws Exception {
                return Family.fromValue(value);
            }

          }

        @Provider
        static public class FamiliesJsonbAdapter implements JsonbAdapter<Set<Family>, JsonArray> {

            @Override
            public JsonArray adaptToJson(final Set<Family> e) throws Exception {
                return Json.createArrayBuilder(e.stream().map(s -> s.value).collect(Collectors.toList())).build();
            }

            @Override
            public Set<Family> adaptFromJson(final JsonArray value) throws Exception {
                final Set<Family> families = new HashSet<>();
                value.forEach(jsonValue -> {
                    families.add(Family.fromValue(jsonValue.toString().replace("\"", "")));
                });

                return families;
            }

        }

    }
}
