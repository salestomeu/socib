package com.socib.service.fixedStation.converter;

import com.google.gson.Gson;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.util.UtilTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixedStationConverterTest {
    private static final String PRODUCT_DATA_FILE_NAME = "product.json";
    private static final String DATA_SOURCES_FILE_NAME = "dataSources.json";
    private static final String DATA_SOURCE_FILE_NAME = "dataSource.json";

    private FixedStationConverter fixedStationConverter;
    private UtilTest utilTest;
    private Gson gson;

    @Before
    public void setUp() {
        fixedStationConverter = new FixedStationConverter();
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_convert_product_and_data_source_to_fixed_station() {

        Product product = gson.fromJson(utilTest.getMockResponse(PRODUCT_DATA_FILE_NAME), Product.class);
        DataSource dataSource = gson.fromJson(utilTest.getMockResponse(DATA_SOURCE_FILE_NAME), DataSource.class);
        GetDataSourceResponse getDataSourceResponse = gson.fromJson(utilTest.getMockResponse(DATA_SOURCES_FILE_NAME), GetDataSourceResponse.class);

        FixedStation fixedStation = fixedStationConverter
                .toDomainModel(product,
                        getDataSourceResponse.getResults(),
                        StationType.COASTALSTATION);

        Assert.assertEquals(product.getId(), fixedStation.getId());
        Assert.assertEquals(product.getName(), fixedStation.getName());
        Assert.assertEquals(dataSource.getEnd_datetime(), fixedStation.getLastUpdateDate());
    }

}
