package com.socib.service.mobileStation;

import android.util.Log;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.converter.MobileStationConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class MobileStationApiService {
    public static final String TRUE = "true";
    public static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";

    private final GetApiOperation getApiOperation;
    private MobileStationConverter mobileStationConverter;

    public MobileStationApiService(GetApiOperation getApiOperation) {
        this.getApiOperation = getApiOperation;
        this.mobileStationConverter = new MobileStationConverter();
    }

    public Observable<List<MobileStation>> getMobileStations(StationType stationType) {
        return getApiOperation.getProducts(stationType.stationType(), TRUE, apiKey)
                .doOnError(error -> Log.e("Error get datasource from product:", stationType.stationType(), error))
                .map(GetProductsResponse::getResults)
                .flatMap(products -> this.getMobileStationFromProduct(products, stationType));
    }

    private Observable<List<MobileStation>> getMobileStationFromProduct(final List<Product> products, final StationType stationType) {
        List<Observable<MobileStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .doOnError(error -> Log.e("Error get datasource from product:", product.getId(), error))
                    .map(getDataSourceResponse -> converterFixedStationFromDataSource(product, getDataSourceResponse.getResults(), stationType)));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private List<MobileStation> getFixedStationList(final Object[] objects) {
        List<MobileStation> result = Arrays.stream(objects)
                .map(MobileStation.class::cast)
                .filter(fixedStation -> fixedStation != null && fixedStation.isValid())
                .collect(Collectors.toList());
        Log.i("FixedStationApiService.getFixedStationList:", String.valueOf(result.size()));
        return result;
    }

    private MobileStation converterFixedStationFromDataSource(final Product product, final List<DataSource> dataSources, StationType stationType) {
        return mobileStationConverter.toDomainModel(product, dataSources, stationType);
    }
}
