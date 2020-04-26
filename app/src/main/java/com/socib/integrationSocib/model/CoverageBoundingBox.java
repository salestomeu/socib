package com.socib.integrationSocib.model;

import java.util.List;

import lombok.Data;

@Data
public class CoverageBoundingBox {
    private String type;
    private List<List<List<Double>>> coordinates;
}
