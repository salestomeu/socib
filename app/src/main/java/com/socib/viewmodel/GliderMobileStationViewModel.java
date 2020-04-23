package com.socib.viewmodel;

import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class GliderMobileStationViewModel extends AbstractMobileStationViewModel {
    public GliderMobileStationViewModel(MobileStationApiService mobileStationApiService, SchedulerProvider schedulerProvider) {
        super(mobileStationApiService, schedulerProvider);
    }
}
