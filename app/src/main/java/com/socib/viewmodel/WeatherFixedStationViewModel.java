package com.socib.viewmodel;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class WeatherFixedStationViewModel extends AbstractFixedStationViewModel{

    public WeatherFixedStationViewModel(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider){
        super(fixedStationApiService, schedulerProvider);
    }

    protected StationType getStationType() {
        return StationType.WEATHERSTATION;
    }
}
