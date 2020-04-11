package com.socib.service.product;

import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.SeaLevelStation;
import com.socib.service.product.converter.SeaLevelStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.List;

import retrofit2.Retrofit;

public class SeaLevelStationApiService extends FixedStationApiService{

    private SeaLevelStationConverter seaLevelStationConverter;

    public SeaLevelStationApiService(Retrofit retrofit, SchedulerProvider schedulerProvider) {
        super(retrofit, schedulerProvider);
        this.seaLevelStationConverter = new SeaLevelStationConverter();
    }

    @Override
    protected FixedStation converterFixedStation(Product product, List<DataSource> dataSources) {
        return seaLevelStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product,dataSources, FixedStation.class), SeaLevelStation.class);
    }
}
