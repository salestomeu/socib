package com.socib.service.fixedStation;

import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.WeatherStation;
import com.socib.service.fixedStation.converter.WeatherStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.List;

import retrofit2.Retrofit;

public class WeatherStationApiService extends FixedStationApiService {
    private WeatherStationConverter weatherStationConverter;

    public WeatherStationApiService(Retrofit retrofit, SchedulerProvider schedulerProvider) {
        super(retrofit, schedulerProvider);
        this.weatherStationConverter = new WeatherStationConverter();
    }

   /* @Override
    protected FixedStation converterFixedStation(Product product, DataSource dataSource, List<Data> getDataResponse) {
        return weatherStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSource, getDataResponse, FixedStation.class), WeatherStation.class);
    }*/

    @Override
    protected FixedStation converterFixedStation(Product product, List<DataSource> dataSources) {
        return weatherStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSources, FixedStation.class), WeatherStation.class);
    }
}
