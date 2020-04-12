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
public class Variable {
    private String long_name;
    private  String units;
    private String data;
    private String precision;
    private String standard_name;
}
