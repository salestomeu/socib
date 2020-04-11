package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.product.CoastalStationApiService;
import com.socib.service.product.FixedStationApiService;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.List;

public class CoastalStationViewModel extends AndroidViewModel {
    private LiveData<List<FixedStation>> fixedStations;
    private FixedStationApiService coastStationApiService;

    public CoastalStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        if (fixedStations == null){
            fixedStations = new MutableLiveData<>();
            coastStationApiService = new CoastalStationApiService(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        }
        fixedStations = coastStationApiService.getDataProducts(StationType.COASTALSTATION.stationType());
        return fixedStations;
    }
}
