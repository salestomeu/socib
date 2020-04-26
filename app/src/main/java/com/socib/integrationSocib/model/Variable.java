package com.socib.integrationSocib.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Variable {
    private String long_name;
    private String units;
    private String data;
    private String precision;
    private String standard_name;
    private String param_name;
}
