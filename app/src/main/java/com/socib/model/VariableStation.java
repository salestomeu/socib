package com.socib.model;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VariableStation {

    public static final String NONE = "__NONE__";

    private String name;
    private String data;
    private String units;
    private Boolean standard;
    private String dataSourceId;


    public String getValue(){
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
}
