package com.socib.viewmodel;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class VariableCosatalStationViewModel extends AbstractVariableStationViewModel {
    public VariableCosatalStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        super(variableStationApiService, schedulerProvider);
    }
}
