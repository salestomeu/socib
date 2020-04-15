package com.socib.service.fixedStation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetProductsResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.service.fixedStation.converter.FixedStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;

public abstract class FixedStationApiService {
    private static final String TRUE = "true";
    private static final String PROCESSING_LEVEL = "L1";
    private static final Integer MAX_QC_VALUE = 2;
    private static final String apiKey = "cF0znGPFcamKiA2p7ze5lKmVHRQlrksI";

    protected FixedStationConverter fixedStationConverter;

    protected abstract FixedStation converterFixedStation(final Product product, final DataSource dataSource, final List<Data> getDataResponse);

    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;


    public FixedStationApiService(Retrofit retrofit,
                                  SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.fixedStationConverter = new FixedStationConverter();
        this.schedulerProvider = schedulerProvider;

    }

    public LiveData<List<FixedStation>> getFixedStationsLiveData(String platformType) {
        final MutableLiveData<List<FixedStation>> fixedStationsAdapter = new MutableLiveData<>();
        getApiOperation.getProducts(platformType, TRUE, apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
                .map(GetProductsResponse::getResults)
                .flatMap(this::getFixedStation)
                .observeOn(this.schedulerProvider.getSchedulerUi())
                .subscribe(fixedStationsAdapter::setValue);
        return fixedStationsAdapter;
    }

    private Observable<List<FixedStation>> getFixedStation(final List<Product> products) {
        List<Observable<List<FixedStation>>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
             fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .flatMap(getDataSourceResponse -> getVariableData(product, getDataSourceResponse.getResults())));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private Observable<List<FixedStation>>  getVariableData(final Product product, final List<DataSource> dataSources) {
        List<DataSource> dataSourcesWithInstrument = dataSources
                .stream()
                .filter(dataSource -> dataSource.getInstrument() != null)
                .collect(Collectors.toList());

        List<Observable<FixedStation>> result = dataSourcesWithInstrument
                .stream()
                .map(dataSource -> getApiOperation.getData(dataSource.getId(), PROCESSING_LEVEL, MAX_QC_VALUE, TRUE, apiKey)
                        .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                        .onErrorReturnItem(Collections.emptyList())
                        .map(getDataResponse -> converterFixedStation(product, dataSource, getDataResponse)))
                .collect(Collectors.toList());
        return Observable.zip(result, this::getFixedStationList);
    }

    private List<FixedStation> getFixedStationList(final Object[] objects) {
        List<FixedStation> result = Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation -> fixedStation != null && fixedStation.getLatitude() != null && fixedStation.getLongitude() != null)
                .collect(Collectors.toList());
        return  result;
    }
}
