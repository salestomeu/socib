package com.socib.service.fixedStation.converter;

import com.socib.R;
import com.socib.commons.AbstractModelConverter;
import com.socib.model.CoastalStation;
import com.socib.model.FixedStation;
import com.socib.model.StationType;

public class CoastalStationConverter extends AbstractModelConverter<CoastalStation, FixedStation> {
    public CoastalStation toApiModel(FixedStation domainModel, Class<CoastalStation> apiClass){
        CoastalStation coastalStation = super.toApiModel(domainModel, apiClass);
        coastalStation.setIcon(R.drawable.ic_map_station);
        coastalStation.setType(StationType.COASTALSTATION.stationType());
        return coastalStation;
    }
}
