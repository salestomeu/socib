package com.socib.service.fixedStation;

import android.util.Log;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.converter.FixedStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class FixedStationApiService {
    public static final String TRUE = "true";
    public static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";

    private FixedStationConverter fixedStationConverter;
    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;


    public FixedStationApiService(Retrofit retrofit,
                                  SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.fixedStationConverter = new FixedStationConverter();
        this.schedulerProvider = schedulerProvider;

    }

    public Observable<List<FixedStation>> getFixedStations(StationType stationType) {
        Log.i("FixedStationApiService.getFixedStationsLiveData: ", stationType.stationType());
        return getApiOperation.getProducts(stationType.stationType(), TRUE, apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
                .doOnError(error -> Log.e("Error get datasource from product:",stationType.stationType(),error))
                .map(GetProductsResponse::getResults)
                .flatMap(products -> this.getFixedStationFromProduct(products, stationType))
                .observeOn(this.schedulerProvider.getSchedulerUi());
    }

    private Observable<List<FixedStation>> getFixedStationFromProduct(final List<Product> products, final StationType stationType) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .doOnError(error -> Log.e("Error get datasource from product:",product.getId(),error))
                    .map(getDataSourceResponse -> converterFixedStationFromDataSource(product, getDataSourceResponse.getResults(), stationType)));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private List<FixedStation> getFixedStationList(final Object[] objects) {
        List<FixedStation> result = Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation -> fixedStation!= null && fixedStation.isValid())
                .collect(Collectors.toList());
        Log.i("FixedStationApiService.getFixedStationList:",String.valueOf(result.size()));
        return  result;
    }

    private FixedStation converterFixedStationFromDataSource(final Product product, final List<DataSource> dataSources, StationType stationType){
        return fixedStationConverter.toDomainModel(product, dataSources, stationType);
    }
}
