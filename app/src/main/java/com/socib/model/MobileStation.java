package com.socib.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class MobileStation {
    private String id;
    private String name;
    private Date lastUpdateDate;
    private Set<String> dataSourceId;
    private Coordinates actualPosition;
    private Coordinates initialPosition;
    private List<Coordinates> wayPoints;
    private List<Coordinates> trajectory;
    private int icon;
}
