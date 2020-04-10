package com.socib.integrationSocib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String description;
    private String data_sources;
    private String entries;
}
