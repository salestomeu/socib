package com.socib.model;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum UnitType {
    METERS_SECONDS("m/s"),
    KILOMETERS_HOUR("km/h"),
    KNOTS("knots"),;


    private String value;

    private static Map<String, UnitType> formatMap = Stream
            .of(UnitType.values())
            .collect(toMap(s -> s.value, Function.identity()));

    public String unitType() {
        return value;
    }

    UnitType(String value) {
        this.value = value;
    }
}
