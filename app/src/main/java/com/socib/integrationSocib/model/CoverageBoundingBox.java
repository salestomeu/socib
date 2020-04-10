package com.socib.integrationSocib.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoverageBoundingBox {
    private String type;
    private List<List<Double>> coordinates;
}
