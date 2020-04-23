package com.socib.viewmodel;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class VariableSeaLevelStationViewModel extends AbstractVariableStationViewModel {
    public VariableSeaLevelStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        super(variableStationApiService, schedulerProvider);
    }
}
