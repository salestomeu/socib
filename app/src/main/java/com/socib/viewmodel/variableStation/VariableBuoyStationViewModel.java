package com.socib.viewmodel.variableStation;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class VariableBuoyStationViewModel extends AbstractVariableStationViewModel {
    public VariableBuoyStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        super(variableStationApiService, schedulerProvider);
    }
}
