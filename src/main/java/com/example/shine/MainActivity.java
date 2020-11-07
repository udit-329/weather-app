package com.example.shine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView currentTemp, city, currentDescription, date, bestWeatherTemp, bestWeatherDescription, bestTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTemp = (TextView)findViewById(R.id.currentTempField);
        currentDescription = (TextView)findViewById(R.id.currentDescriptionField);
        city = (TextView)findViewById(R.id.cityField);
        date = (TextView)findViewById(R.id.dateField);
        find_current_weather();

        bestWeatherTemp = (TextView)findViewById(R.id.bestTempField);
        bestWeatherDescription = (TextView)findViewById(R.id.bestDescriptionField);
        bestTime = (TextView)findViewById(R.id.bestTimeField);
        find_best_weather();
    }
    protected void find_best_weather(){
        double[] scores = new double[13]; //sized based on hour range index represent hour
        double[] temps = {20, 18, 15, 25, 17}; //TODO populate with real temps
        int[] descriptions = {800, 600,602,803,801}; //TODO populate with real description id
        for(int i = 0; i < temps.length-1;i++){
            scores[i] = temps[i];
        }
        for(int i = 0; i < descriptions.length-1;i++){
            if (descriptions[i] == 800) { scores[i] += 13;}
            else if(descriptions[i] == 801 || descriptions[i] == 801){scores[i]+=12;}
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
        bestWeatherTemp.setText(String.valueOf(temps[highestScoreIndex]));
        bestWeatherDescription.setText(getDescription(descriptions[highestScoreIndex]));
        bestTime.setText(getTime(highestScoreIndex));
    }

    protected String getTime(int index){
        index = index+8;
        int hourLater = index+1;
        if(index < 11){
            return "between " + index + "AM and " + hourLater + "AM";
        }
        if(index == 11){
            return "between 11am and 12PM";
        }
        else{
            index -= 12;
            return "between " + index + "PM and " + hourLater + "PM";
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

    protected void find_current_weather(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=toronto&appid=542ce61fca8f88351ade3116575cd0e8&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                date.setText("hello");
                try {
                    date.setText("hi");
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String city1 = response.getString("name");

                    currentTemp.setText(temp);
                    currentDescription.setText(description);
                    city.setText(city1);

                   // Calendar calendar = new Calendar.getInstance());
                    //SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                   // String formatted_date = sdf.format(calendar.getTime());

                    //date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    int i = (int)temp_int;
                    currentTemp.setText(String.valueOf(i));
                }
                catch(JSONException e){
                    //date.setText("bye");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            date.setText("no");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}