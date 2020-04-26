package com.socib.integrationSocib.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetDataResponse {
    private String feature_type;
    private List<Variable> variables;
    private String data_mode;
    private String processing_level;
}
