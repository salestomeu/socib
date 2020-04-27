package com.socib.model;

import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VariableStation implements Comparable<VariableStation>{

    public static final String NONE = "__NONE__";

    private String name;
    private String data;
    private String units;
    private Boolean standard;
    private String dataSourceId;
    private String description;


    public String getValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(data);
        sb.append(" ");
        sb.append(units);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableStation that = (VariableStation) o;
        return name.equals(that.name) &&
                dataSourceId.equals(that.dataSourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataSourceId);
    }

    @Override
    public int compareTo(VariableStation o) {
        return description.compareTo(o.description);
    }
}
