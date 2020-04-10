package com.socib.service.product;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.service.product.converter.FixedStationConverter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FixedStationApiService {
    private static final String TRUE = "true";

    private static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";
    private final FixedStationConverter fixedStationConverter;
    private GetApiOperation getApiOperation;

    public FixedStationApiService(Retrofit retrofit, FixedStationConverter fixedStationConverter){
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.fixedStationConverter = fixedStationConverter;
    }

    public LiveData<List<FixedStation>> getDataProducts(String platformType){
        final MutableLiveData<List<FixedStation>> fixedStationsAdapter = new MutableLiveData<>();
        List<FixedStation> fixedStations = new ArrayList<>();
        Disposable result = getApiOperation.getProducts(platformType, TRUE, apiKey)
                .subscribeOn(Schedulers.io())
                .map(GetProductsResponse::getResults)
                .map(this::getFixedStation)
                .flatMap(Observable::merge)
                .doOnNext(fixedStation -> Log.i("doOnNext:", fixedStation.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fixedStations::add);
        fixedStationsAdapter.setValue(fixedStations);
        return fixedStationsAdapter;
    }

    private List<Observable<FixedStation>> getFixedStation(List<Product> products) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products){
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .map(getDataSourceResponse -> fixedStationConverter.toApiModel(product, getDataSourceResponse.getResults(), FixedStation.class)));
        }
        return fixedStationsList;
    }
}
