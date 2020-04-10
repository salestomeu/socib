package com.socib.integrationSocib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataSource {
    private String id;
    private String instrument;
    private CoverageBoundingBox coverage_bounding_box;

}
