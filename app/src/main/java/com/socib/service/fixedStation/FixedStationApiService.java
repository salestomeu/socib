package com.socib.service.fixedStation;

import android.util.Log;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.converter.FixedStationConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class FixedStationApiService {
    private static final String TRUE = "true";

    private FixedStationConverter fixedStationConverter;
    private final GetApiOperation getApiOperation;
    private final String apiKey;


    public FixedStationApiService(GetApiOperation getApiOperation, String apiKey) {
        this.getApiOperation = getApiOperation;
        this.apiKey = apiKey;
        this.fixedStationConverter = new FixedStationConverter();

    }

    public Observable<List<FixedStation>> getFixedStations(StationType stationType) {
        return getApiOperation.getProducts(stationType.stationType(), TRUE, apiKey)
                .doOnError(error -> Log.e("Error get fixedStation from product:",stationType.stationType(),error))
                .map(GetProductsResponse::getResults)
                .flatMap(products -> this.getFixedStationFromProduct(products, stationType));
    }

    private Observable<List<FixedStation>> getFixedStationFromProduct(final List<Product> products, final StationType stationType) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .doOnError(error -> Log.e("Error get dataSource from product:",product.getId(),error))
                    .map(getDataSourceResponse -> converterFixedStationFromDataSource(product, getDataSourceResponse.getResults(), stationType)));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private List<FixedStation> getFixedStationList(final Object[] objects) {
        return Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation -> fixedStation!= null && fixedStation.isValid())
                .collect(Collectors.toList());
    }

    private FixedStation converterFixedStationFromDataSource(final Product product, final List<DataSource> dataSources, StationType stationType){
        return fixedStationConverter.toDomainModel(product, dataSources, stationType);
    }
}
