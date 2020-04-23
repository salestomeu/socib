package com.socib.viewmodel;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class VariableWeatherStationViewModel extends AbstractVariableStationViewModel{
    public VariableWeatherStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        super(variableStationApiService, schedulerProvider);
    }
}
