package com.socib.model;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum StationType {
    COASTALSTATION("Coastal Station"),
    SEALEVEL("Sea Level"),
    WEATHERSTATION("Weather Station"),
    BUOY("Oceanographic Buoy");

    private String value;

    private static Map<String, StationType> formatMap = Stream
            .of(StationType.values())
            .collect(toMap(s -> s.value, Function.identity()));

    public String stationType(){
        return value;
    }

    StationType(String value){
        this.value = value;
    }
}
