package com.socib.service.fixedStation.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.Variable;
import com.socib.model.VariableStation;

public class VariableStationConverter extends AbstractModelConverter<Variable, VariableStation> {
    @Override
    public VariableStation toDomainModel(Variable apiModel, Class<VariableStation> domainClass) {
        VariableStation variableStation = super.toDomainModel(apiModel, domainClass);
        variableStation.setName(apiModel.getParam_name());
        variableStation.setStandard(!VariableStation.NONE.equals(apiModel.getStandard_name()));
        return  variableStation;
    }
}
