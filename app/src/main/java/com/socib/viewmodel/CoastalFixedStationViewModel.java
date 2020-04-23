package com.socib.viewmodel;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class CoastalFixedStationViewModel extends AbstractFixedStationViewModel{

    public CoastalFixedStationViewModel(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider){
        super(fixedStationApiService, schedulerProvider);
    }

    protected StationType getStationType() {
        return StationType.COASTALSTATION;
    }
}
