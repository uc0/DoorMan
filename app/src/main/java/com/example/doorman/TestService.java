package com.example.doorman;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TestService {
    @GET("items")
    Call<Object> getTest();

    @POST
    Call<Model__CheckAlready> postOverlapCheck(@Body Model__CheckAlready modelCheckAlready);
}
