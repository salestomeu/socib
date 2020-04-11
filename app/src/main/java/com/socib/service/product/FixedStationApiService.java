package com.socib.service.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.service.product.converter.FixedStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public abstract  class FixedStationApiService {
    private static final String TRUE = "true";

    private static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";

    protected FixedStationConverter fixedStationConverter;
    protected abstract FixedStation converterFixedStation(final  Product product, final List<DataSource> dataSources);

    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;


    public FixedStationApiService(Retrofit retrofit,
                                  SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.fixedStationConverter = new FixedStationConverter();
        this.schedulerProvider = schedulerProvider;

    }

    public LiveData<List<FixedStation>> getDataProducts(String platformType){
        final MutableLiveData<List<FixedStation>> fixedStationsAdapter = new MutableLiveData<>();
        getApiOperation.getProducts(platformType, TRUE, apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
                .doOnNext(getProductsResponse -> System.out.println("Ok count: +" + getProductsResponse.getCount()))
                .map(GetProductsResponse::getResults)
                .flatMap(this::getFixedStation)
                .observeOn(this.schedulerProvider.getSchedulerUi())
                .subscribe(fixedStationsAdapter::setValue);
        return fixedStationsAdapter;
    }

    private Observable<List<FixedStation>> getFixedStation(List<Product> products) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products){
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .map(getDataSourceResponse -> converterFixedStation(product, getDataSourceResponse.getResults())));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private List<FixedStation> getFixedStationList(Object[] objects) {
        return Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation ->  fixedStation != null && fixedStation.getLatitude() != null && fixedStation.getLongitude() != null)
                .collect(Collectors.toList());
    }
}
