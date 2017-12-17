package org.svuonline.anaskoara.webid.loginregistration.server;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("webidsrerver/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
