package com.socib.integrationSocib;


import com.socib.integrationSocib.model.GetDataResponse;
import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.integrationSocib.model.GetProductsResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetApiOperation {

    String DATA_PRODUCTS_PATH = "data-products/";
    String DATA_SOURCES_PATH = "data-sources/";
    String DATA_PATH = "data-sources/{id}/data/";

    @GET(DATA_PRODUCTS_PATH)
    Observable<GetProductsResponse> getProducts(@Query("platform_type") String platformType,
                                                @Query("is_active") String isActive,
                                                @Header("api_key") String apiKey);

    @GET(DATA_SOURCES_PATH)
    Observable<GetDataSourceResponse> getDataSource(@Query("data_product") String dataProduct,
                                                    @Query("is_active") String isActive,
                                                    @Query("initial_datetime") String initialDateTime,
                                                    @Header("api_key") String apiKey);

    @GET(DATA_PATH)
    Observable<List<GetDataResponse>> getData(@Path("id") String id,
                                              @Query("processing_level") String processingLevel,
                                              @Query("max_qc_value") Integer maxQcValue,
                                              @Query("latest") String latest,
                                              @Header("api_key") String apiKey);

    @GET(DATA_SOURCES_PATH)
    Observable<GetDataSourceResponse> getMobileStation(@Query("platform_type") String platformType,
                                                       @Query("is_active") String isActive,
                                                       @Header("api_key") String apiKey);

}
