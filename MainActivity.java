package com.example.shine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    //private static String OPEN_WEATHER_MAP_URL = "api.openweathermap.org/data/2.5/weather?q=Toronto&appid=";
    //private static String OPEN_WEATHER_MAP_API = "<API-KEY";
    TextView currentTemp, city, currentDescription, date, bestWeatherTemp, bestWeatherDescription, bestTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTemp = (TextView)findViewById(R.id.currentTempField);
        currentDescription = (TextView)findViewById(R.id.currentDescriptionField);
        //city = (TextView)findViewById(R.id.cityField);
        date = (TextView)findViewById(R.id.dateField);
        //find_current_weather();

        bestWeatherTemp = (TextView)findViewById(R.id.bestTempField);
        bestWeatherDescription = (TextView)findViewById(R.id.bestDescriptionField);
        bestTime = (TextView)findViewById(R.id.bestTimeField);
        find_best_weather();
    }

    public void find_current_weather(){
        date.setText("tess");
        //URL url1 = new URL
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=542ce61fca8f88351ade3116575cd0e8&units=Metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        date.setText("hi");
                       try {
                           date.setText("hi");
                           JSONObject main_object = response.getJSONObject("main");
                           JSONArray array = response.getJSONArray("weather");
                           JSONObject object = array.getJSONObject(0);
                           String temp = String.valueOf(main_object.getDouble("temp"));
                           String description = object.getString("description");
                           String city = response.getString("name");

                           currentTemp.setText(temp);
                           currentDescription.setText(description);
                       }
                        catch(JSONException e){
                        date.setText("bye");
                        e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        date.setText("hello");
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }




    protected void find_best_weather(){
        double[] scores = new double[20]; //sized based on hour range index represent hour
        //toronto
        //double[] temps = {11, 12, 13, 14, 15, 14, 15.5, 15.5, 15, 14, 14, 12, 12}; //TODO populate with real temps
        //int[] descriptions = {800, 800, 800, 800, 800, 800, 800, 800, 800, 800, 800, 800, 800}; //TODO populate with real description id
        //calgary
        double[]temps = {-9, -9, -8, -7, -7, -6, -6, -5.5, -5.5, -5.5, -6, -6, -7};
        int[] descriptions = { 602, 602, 802, 802, 802, 802, 802, 804, 802, 801, 802, 801, 801};

        for(int i = 0; i < temps.length-1;i++){
            scores[i] = temps[i];
        }
        for(int i = 0; i < descriptions.length-1;i++){
            if (descriptions[i] == 800) { scores[i] += 13;}
            else if(descriptions[i] == 801 || descriptions[i] == 802){scores[i]+=12;}
            else if(descriptions[i]== 803 || descriptions[i] == 804 ){scores[i]+=11;}
            else if(descriptions[i] == 600 || descriptions[i] == 601){scores[i]+= 10;}
            else if(descriptions[i] == 701 || descriptions[i] == 741){scores[i]+=9;}
            else if(descriptions[i] == 500){scores[i] += 8;}
            else if(descriptions[i] == 501){scores[i]+= 7;}
            else if(descriptions[i] == 602 || descriptions[i] == 615){scores[i] += 6;}
            else if((descriptions[i]>= 611 && descriptions[i] <= 613) || (descriptions[i] >= 616 || descriptions[i] <=622)){scores[i] += 5;}
            else if(descriptions[i] >= 502 || descriptions[i] <= 531){scores[i] += 4;}
            else if(descriptions[i] == 761 || descriptions[i] == 751 || descriptions[i] == 731 || descriptions[i]==721){scores[i]+=3;}
            else if(descriptions[i] == 711 || descriptions[i] == 762 || descriptions[i]==771){scores[i] += 2;}
            else if((descriptions[i]>=200 && descriptions[i] <= 232) || descriptions[i] == 781 ){scores[i] += 1;}
        }
        double highestScore = scores[0];
        int highestScoreIndex = 0;
        for(int i = 0; i < scores.length-1; i++){
            if(scores[i] >= highestScore){
                highestScore = scores[i];
                highestScoreIndex = i;
            }
        }
        bestTime.setText(getTime(highestScoreIndex));
        bestWeatherTemp.setText(String.valueOf(temps[highestScoreIndex]));
        bestWeatherDescription.setText(getDescription(descriptions[highestScoreIndex]));
    }

    protected String getTime(int index){
        index = index+8;
        if(index < 12){
            return index + ":30 AM ";
        }
        else{
            index -= 12;
            return index + ":30 PM ";
        }
    }

    protected String getDescription(int id){
        if(id == 800){
            return "clear";
        }
        else if(id == 801 || id == 802){
            return "party cloudy";
        }
        else if(id == 803 || id == 804){
            return "cloudy";
        }
        else if(id == 701 || id == 741){
            return "fog";
        }
        else if(id == 711 || id == 762 || id == 761 || id == 751 || id == 731 || id == 721){
            return "low air quality";
        }
        else if(id == 771){
            return "squall";
        }
        else if(id == 781 ){
            return "tornado";
        }
        else if(id >= 200 && id <= 232){
            return "thunderstorm";
        }
        else if(id == 600){
            return "light snow";
        }
        else if(id == 601 || id == 602){
            return "snow";
        }
        else if(id >= 615 && id <= 622){
            return "rain and snow";
        }
        else if(id >= 611 && id <= 613){
            return "sleet";
        }
        else if(id == 500){
            return "light rain";
        }
        else if(id == 501){
            return "rain";
        }
        else if(id >= 502 && id <= 531 ){
            return "heavy rain";
        }
        else return null;
    }



}
