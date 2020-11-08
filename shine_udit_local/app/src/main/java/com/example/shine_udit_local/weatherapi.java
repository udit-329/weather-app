package com.example.shine_udit_local;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<dataGet> getweather(@Query("q") String cityname,
                             @Query("appid") String apikey);
}
