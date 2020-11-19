package com.example.Shine;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<dataGet> getweather(@Query("q") String cityname,
                             @Query("appid") String apikey);
}
//public interface weatherapi {
//    @GET("weather")
//    Call<dataGet> getweather(@Query("lat") String lat,
//                             @Query("lng") String lng,
//                             @Query("appid") String apikey);
//}
