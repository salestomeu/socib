package com.socib.integrationSocib.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetProductsResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<Product> results;
}
