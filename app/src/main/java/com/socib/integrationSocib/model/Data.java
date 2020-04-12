package com.socib.integrationSocib.model;

import java.util.List;

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
public class Data {
    private String feature_type;
    private List<Variable> variables;
    private String data_mode;
    private String processing_level;
}
