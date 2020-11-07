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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView currentTemp, city, currentDescription, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTemp = (TextView)findViewById(R.id.currentTempField);
        currentDescription = (TextView)findViewById(R.id.currentDescriptionField);
        city = (TextView)findViewById(R.id.cityField);
        date = (TextView)findViewById(R.id.dateField);
        find_current_weather();
    }

    protected void find_current_weather(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=toronto&appid=542ce61fca8f88351ade3116575cd0e8&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    date.setText("hi");
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String city1 = response.getString("name");

                    currentTemp.setText(temp);
                    currentDescription.setText("ugh");
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
            //date.setText("no");
            }
        });
        //date.setText("ugh");
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}