package org.svuonline.anaskoara.webid.loginregistration.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductInterface {

    @POST("webidsrerver/")
    Call<ProductResponse> operation(@Body ProductRequest request);

}
