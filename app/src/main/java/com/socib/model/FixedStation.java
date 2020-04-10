package com.socib.model;

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
public class FixedStation {
    private  String id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String dataSourceId;
    private String type;
}
