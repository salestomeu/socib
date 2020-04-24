package com.socib.model;

import com.socib.R;
import com.socib.integrationSocib.model.Variable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
