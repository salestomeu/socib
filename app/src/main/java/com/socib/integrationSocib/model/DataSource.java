package com.socib.integrationSocib.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataSource {
    private String id;
    private String instrument;
    private Date end_datetime;
    private CoverageBoundingBox coverage_bounding_box;
    private List<String> feature_types;

}
