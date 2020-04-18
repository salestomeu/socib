package com.socib.model;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobileStation {
    private String id;
    private String name;
    private Date lastUpdateDate;
    private Double latitude;
    private Double longitude;
    private Set<String> dataSourceId;
    private int icon;

    public boolean isValid(){
        return  latitude != null  && longitude != null && !dataSourceId.isEmpty();
    }
}
