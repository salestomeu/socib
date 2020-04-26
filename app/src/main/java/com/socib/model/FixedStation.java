package com.socib.model;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class FixedStation {
    private String id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Set<String> dataSourceId;
    private int icon;
    private String type;
    private Date lastUpdateDate;

    public boolean isValid(){
        return  latitude != null  && longitude != null && !dataSourceId.isEmpty();
    }
}
