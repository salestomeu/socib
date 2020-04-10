package com.socib.integrationSocib.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CoverageBoundingBox {
    private String type;
    private List<List<List<Double>>> coordinates;
}
