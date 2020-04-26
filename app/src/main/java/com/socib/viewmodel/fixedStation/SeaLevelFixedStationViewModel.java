package com.socib.viewmodel.fixedStation;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.fixedStation.AbstractFixedStationViewModel;

public class SeaLevelFixedStationViewModel extends AbstractFixedStationViewModel {

    public SeaLevelFixedStationViewModel(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider){
        super(fixedStationApiService, schedulerProvider);
    }

    protected StationType getStationType() {
        return StationType.SEALEVEL;
    }
}
