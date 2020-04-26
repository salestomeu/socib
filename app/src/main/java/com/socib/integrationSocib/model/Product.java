package com.socib.integrationSocib.model;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private String data_sources;
    private String entries;
}
