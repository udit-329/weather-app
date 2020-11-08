package com.example.shine_udit_local;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
EditText et;
TextView tv;
String url="api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
String apikey="03b431e0388ab2f5ce8b19f3aa73272c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et=findViewById(R.id.et);
        tv=findViewById(R.id.tv);

    }

    public void getweather(View v) {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherapi myapi=retrofit.create(weatherapi.class);
        Call<dataGet> example=myapi.getweather(et.getText().toString().trim(),apikey);
        example.enqueue(new Callback<dataGet>() {
            @Override
            public void onResponse(Call<dataGet> call, Response<dataGet> response) {
                dataGet mydata=response.body();
                Main main=mydata.getMain();
                Double temp=main.getTemp();
                Integer temperature=(int)(temp-273.15);
                tv.setText(String.valueOf(temperature)+"C");
            }

            @Override
            public void onFailure(Call<dataGet> call, Throwable t) {
                
            }
        }
    }
}
