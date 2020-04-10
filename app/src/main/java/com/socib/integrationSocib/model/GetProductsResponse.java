package com.socib.integrationSocib.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<Product> results;
}
