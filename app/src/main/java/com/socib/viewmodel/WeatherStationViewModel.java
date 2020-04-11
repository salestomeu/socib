package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.product.FixedStationApiService;
import com.socib.service.product.WeatherStationApiService;

import java.util.List;

public class WeatherStationViewModel extends AndroidViewModel {
    private LiveData<List<FixedStation>> fixedStations;
    private FixedStationApiService weatherStationApiService;
    public WeatherStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        if (fixedStations == null){
            fixedStations = new MutableLiveData<>();
            weatherStationApiService = new WeatherStationApiService(IntegrationOperationFactory.getAdapter());
        }
        fixedStations = weatherStationApiService.getDataProducts(StationType.WEATHERSTATION.stationType());
        return  fixedStations;
    }
}
