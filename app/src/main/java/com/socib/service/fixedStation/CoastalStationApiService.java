package com.socib.service.fixedStation;

import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.CoastalStation;
import com.socib.model.FixedStation;
import com.socib.service.fixedStation.converter.CoastalStationConverter;
import com.socib.service.provider.SchedulerProvider;

import java.util.List;

import retrofit2.Retrofit;

public class CoastalStationApiService extends FixedStationApiService {
    private CoastalStationConverter coastalStationConverter;

    public CoastalStationApiService(Retrofit retrofit, SchedulerProvider schedulerProvider) {
        super(retrofit, schedulerProvider);
        this.coastalStationConverter = new CoastalStationConverter();
    }

   /* @Override
    protected FixedStation converterFixedStation(Product product, DataSource dataSource, List<Data> getDataResponse) {
        return coastalStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSource, getDataResponse, FixedStation.class), CoastalStation.class);
    }
*/
    @Override
    protected FixedStation converterFixedStation(Product product, List<DataSource> dataSources) {
        return coastalStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSources, FixedStation.class), CoastalStation.class);
    }
}
