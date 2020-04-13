package com.socib.service.fixedStation;

import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.SeaLevelStation;
import com.socib.service.fixedStation.converter.SeaLevelStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.List;

import retrofit2.Retrofit;

public class SeaLevelStationApiService extends FixedStationApiService {
    private SeaLevelStationConverter seaLevelStationConverter;

    public SeaLevelStationApiService(Retrofit retrofit, SchedulerProvider schedulerProvider) {
        super(retrofit, schedulerProvider);
        this.seaLevelStationConverter = new SeaLevelStationConverter();
    }

    @Override
    protected FixedStation converterFixedStation(Product product, DataSource dataSource, List<Data> getDataResponse) {
        return seaLevelStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSource, getDataResponse, FixedStation.class), SeaLevelStation.class);
    }
}
