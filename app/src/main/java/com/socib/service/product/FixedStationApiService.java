package com.socib.service.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.model.FixedStation;
import com.socib.service.product.converter.FixedStationConverter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        final MutableLiveData<List<FixedStation>> fixedStations = new MutableLiveData<>();
        getApiOperation.getProducts(platformType, TRUE, apiKey)
                .subscribeOn(Schedulers.io())
                .map(productsResponse -> getFixedStation(productsResponse))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((fixedStation)-> {
                    fixedStations.setValue(fixedStation);
                });
        return fixedStations;
    }

    private List<FixedStation> getFixedStation(GetProductsResponse productResponse) {
        List<FixedStation> fixedStationsList = new ArrayList<>();
        for (Product product : productResponse.getResults()){
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .map(dataSourceResponse -> getCoordenates(product, dataSourceResponse)));
        }
        return fixedStationsList;
    }

    private Observable<FixedStation> getCoordenates(Product product, GetDataSourceResponse dataSourceResponse) {
        return Observable.just(fixedStationConverter.toApiModel(product, dataSourceResponse.getResults(), FixedStation.class));
    }


    private List<Observable<GetDataSourceResponse>> getDataSourceObservable(GetApiOperation getApiOperation, List<Product> products){
        List<Observable<GetDataSourceResponse>> dataSources = new ArrayList<>();
        for (Product product : products){
            Observable<GetDataSourceResponse> dataSource = getApiOperation
                    .getDataSource(product.getId(), TRUE, apiKey);
        }
        return  dataSources;
    }
}
