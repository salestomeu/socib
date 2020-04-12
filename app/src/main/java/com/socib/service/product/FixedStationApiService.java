package com.socib.service.product;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.Data;
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
import io.reactivex.Observer;
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

    public LiveData<List<FixedStation>> getDataProducts(String platformType) {
        final MutableLiveData<List<FixedStation>> fixedStationsAdapter = new MutableLiveData<>();
        getApiOperation.getProducts(platformType, TRUE, apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
              //  .doOnNext(getProductsResponse -> System.out.println("Ok count: +" + getProductsResponse.getCount()))
                .map(GetProductsResponse::getResults)
                .flatMap(this::getFixedStation)
                .observeOn(this.schedulerProvider.getSchedulerUi())
                .subscribe(fixedStationsAdapter::setValue);
        return fixedStationsAdapter;
    }

    private Observable<List<FixedStation>> getFixedStation(List<Product> products) {
        List<Observable<FixedStation>> fixedStationsList = new ArrayList<>();
        for (Product product : products) {
            fixedStationsList.add(getApiOperation.getDataSource(product.getId(), TRUE, apiKey)
                    .flatMap(getDataSourceResponse -> getVariableData(product, getDataSourceResponse.getResults())));
        }
        return Observable.zip(fixedStationsList, this::getFixedStationList);
    }

    private Observable<FixedStation> getVariableData(Product product, List<DataSource> dataSources) {
        try {
            return dataSources
                    .stream()
                    .filter(dataSource -> dataSource.getInstrument() != null)
                    .findFirst()
                    .map(dataSource -> {
                        Log.i("dataSource.getId:", dataSource.getId());
                        return getApiOperation.getData(dataSource.getId(), PROCESSING_LEVEL, MAX_QC_VALUE, TRUE, apiKey)
                                .doOnError(e -> {
                                    Log.i("Error get Data", e.getMessage());
                                    converterFixedStation(product, dataSource, null);
                                })
                                .map(getDataResponse -> converterFixedStation(product, dataSource, getDataResponse));
                    })
                    .orElse(new Observable<FixedStation>() {
                        @Override
                        protected void subscribeActual(Observer<? super FixedStation> observer) {
                            Log.i("Fallo: ", product.getId());
                            converterFixedStation(product, new DataSource(), null);
                        }
                    });
        } catch (Exception e) {
            return new Observable<FixedStation>() {
                @Override
                protected void subscribeActual(Observer<? super FixedStation> observer) {
                    Log.i("Fallo: ", product.getId());
                    converterFixedStation(product, new DataSource(), null);
                }
            };
        }
    }

    private List<FixedStation> getFixedStationList(Object[] objects) {
        List<FixedStation> result = Arrays.stream(objects)
                .map(FixedStation.class::cast)
                .filter(fixedStation -> fixedStation != null && fixedStation.getLatitude() != null && fixedStation.getLongitude() != null)
                .collect(Collectors.toList());
        Log.i("FixedSationApiService.getFixedSationList size", String.valueOf(result.size()));
        return  result;
    }

    public LiveData<List<Data>> getData(){
        MutableLiveData<List<Data>> listMutableLiveData = new MutableLiveData<>();
         getApiOperation.getData("10f09dc763", PROCESSING_LEVEL, MAX_QC_VALUE, TRUE, apiKey)
                 .subscribeOn(this.schedulerProvider.getSchedulerIo())
                 .doOnNext(data -> Log.i("data.size:", String.valueOf(data.size())))
                 .observeOn(this.schedulerProvider.getSchedulerUi())
                .subscribe(listMutableLiveData::setValue);

        return  listMutableLiveData;
    }

}
