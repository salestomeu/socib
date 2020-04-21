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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableStation that = (VariableStation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
