package com.socib.service.product.converter;

import com.socib.R;
import com.socib.commons.AbstractModelConverter;
import com.socib.model.FixedStation;
import com.socib.model.SeaLevelStation;
import com.socib.model.StationType;

public class SeaLevelStationConverter extends AbstractModelConverter<SeaLevelStation, FixedStation> {
    public SeaLevelStation toApiModel(FixedStation domainModel, Class<SeaLevelStation> apiClass) {
        SeaLevelStation seaLevelStation = super.toApiModel(domainModel, apiClass);
        seaLevelStation.setIcon(R.drawable.ic_map_sea_level);
        seaLevelStation.setType(StationType.SEALEVEL.stationType());
        return seaLevelStation;
    }
}
