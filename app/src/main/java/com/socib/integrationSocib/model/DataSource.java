package com.socib.integrationSocib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    private String id;
    private String instrument;
    private CoverageBoundingBox coverageBoundingBox;

}
