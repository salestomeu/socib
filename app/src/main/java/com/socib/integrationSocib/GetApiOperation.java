package com.socib.integrationSocib;



import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.integrationSocib.model.GetProductsResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetApiOperation {
    @GET("data-products/")
    Observable<GetProductsResponse> getProducts(@Query("platform_type") String platformType,
                                                @Query("is_active") String isActive,
                                                @Header("api_key") String apiKey);

    @GET("data-sources/")
    Observable<GetDataSourceResponse> getDataSource(@Query("data_product") String dataProduct,
                                                    @Query("is_active") String isActive,
                                                    @Header("api_key") String apiKey);
}
