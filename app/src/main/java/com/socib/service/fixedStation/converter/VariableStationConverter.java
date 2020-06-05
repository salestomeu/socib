package com.socib.service.fixedStation.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.Variable;
import com.socib.model.Profile;
import com.socib.model.UnitType;
import com.socib.model.VariableStation;
import com.socib.service.profile.ProfileService;

import java.text.DecimalFormat;

public class VariableStationConverter extends AbstractModelConverter<Variable, VariableStation> {

    private static final String METERS_SECONDS_API = "m s-1";
    private static final double CONVERSOR_MS_TO_KM = 3.6;
    private static final double CONVERSOR_MS_TO_KNOTS = 1.943844;
    private final DecimalFormat df = new DecimalFormat("###.##");

    public VariableStation toDomainModel(Variable apiModel, String dataSourceId, Class<VariableStation> domainClass) {
        VariableStation variableStation = super.toDomainModel(apiModel, domainClass);
        variableStation.setDataSourceId(dataSourceId);
        variableStation.setName(apiModel.getParam_name());
        variableStation.setStandard(!VariableStation.NONE.equals(apiModel.getStandard_name()));
        Profile userProfile = ProfileService.getInstance().getProfile();
        if (METERS_SECONDS_API.equals(variableStation.getUnits())){
            if (UnitType.KILOMETERS_HOUR.equals(userProfile.getUnits())){
                double dataMeterSeconds = Double.parseDouble(apiModel.getData());
                variableStation.setData(df.format(dataMeterSeconds * CONVERSOR_MS_TO_KM));
            }
            if (UnitType.KNOTS.equals(userProfile.getUnits())){
                double dataMeterSeconds = Double.parseDouble(apiModel.getData());
                variableStation.setData(df.format(dataMeterSeconds * CONVERSOR_MS_TO_KNOTS));
            }
            variableStation.setUnits(userProfile.getUnits().unitType());
        }
        return  variableStation;
    }
}
