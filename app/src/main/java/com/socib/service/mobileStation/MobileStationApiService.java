package com.socib.service.mobileStation;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.service.provider.SchedulerProvider;

import retrofit2.Retrofit;

public abstract class MobileStationApiService {

    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;

    public MobileStationApiService(Retrofit retrofit,
                                   SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.schedulerProvider = schedulerProvider;
    }
}
