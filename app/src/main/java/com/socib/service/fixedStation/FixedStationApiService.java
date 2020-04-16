package com.socib.service.fixedStation;

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
    private static final String TRUE = "true";
    private static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";

    private FixedStationConverter fixedStationConverter;

    //protected abstract FixedStation converterFixedStation(final Product product, final DataSource dataSource, final List<Data> getDataResponse);

    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;


    public FixedStationApiService(Retrofit retrofit,
                                  SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.fixedStationConverter = new FixedStationConverter();
        this.schedulerProvider = schedulerProvider;

    }

    public Observable<List<FixedStation>> getFixedStationsLiveData(StationType stationType) {
        return getApiOperation.getProducts(stationType.stationType(), TRUE, apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
                .map(GetProductsResponse::getResults)
                .flatMap(products -> this.getFixedStation(products, stationType))
                .observeOn(this.schedulerProvider.getSchedulerUi());
    }

    private Observable<List<FixedStation>> getFixedStation(final List<Product> products, final StationType stationType) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .doOnError(error -> {
                        System.out.println("Error get datasource from product:"+product.getId());
                        System.out.println(error.getMessage());
                    })
                    .map(getDataSourceResponse -> converterFixedStation(product, getDataSourceResponse.getResults(), stationType)));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private List<FixedStation> getFixedStationList(final Object[] objects) {
        return Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation -> fixedStation != null && fixedStation.getLatitude() != null && fixedStation.getLongitude() != null && !fixedStation.getDataSourceId().isEmpty())
                .collect(Collectors.toList());
    }

    private FixedStation converterFixedStation(final Product product, final List<DataSource> dataSources, StationType stationType){
        return fixedStationConverter.toDomainModel(product, dataSources, stationType);
    }
}
