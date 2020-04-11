package com.socib.service.product.converter;

import com.socib.R;
import com.socib.commons.AbstractModelConverter;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.model.WeatherStation;

public class WeatherStationConverter extends AbstractModelConverter<WeatherStation, FixedStation> {
    public WeatherStation toApiModel(FixedStation domainModel, Class<WeatherStation> apiClass) {
        WeatherStation weatherStation = super.toApiModel(domainModel, apiClass);
        weatherStation.setIcon(R.drawable.ic_map_meteo);
        weatherStation.setType(StationType.WEATHERSTATION.stationType());
        return weatherStation;
    }
}
