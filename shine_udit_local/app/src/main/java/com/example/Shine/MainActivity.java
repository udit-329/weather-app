package com.example.Shine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shine_udit_local.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;
    TextView bestTimeText;
    TextView textView2;
    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikey = "<API_KEY>";


    //initialize buttons
    Spinner spType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;

    double currentLat = 0, currentLong = 0;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variables
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        bestTimeText = findViewById(R.id.bestTimeText);
        textView2 = findViewById(R.id.textView2);

        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initialize places
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_map_key));
        PlacesClient placesClient = Places.createClient(this);

        //initialize array of place type
        String[] placeTypeList = {"park", "spa", "bakery", "supermarket"};
        //initialize array of place name
        String[] placeNameList = {"Parks", "Spa", "Bakery", "Supermarket"};

        //set adapter on spinner
        spType.setAdapter(new ArrayAdapter<>(MainActivity.this
                , android.R.layout.simple_spinner_dropdown_item, placeNameList));

        //initialize fused location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(MainActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            //call method
            getCurrentLocation();
        } else {
            //when permission denied
            //request permission
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get selected position of spinner
                int i = spType.getSelectedItemPosition();

                if(i == 0) {
                    textView2.setText("Go out for a jog!");
                }
                else if(i == 1){
                    textView2.setText("Get a relaxing massage!");
                }
                else if(i == 2){
                    textView2.setText("Eat your favourite cake!");
                }
                else if(i == 3){
                    textView2.setText("Shop for your daily needs!");
                }


                //init url
                String url_goog = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong + //location
                        "&radius=2000" +  //within 2km
                        "&types=" + placeTypeList[i] + //type
                        "&sensor=true" +
                        "&opennow=true" +
                        "&key=" + getResources().getString(R.string.google_map_key);

                //execute place task method to get json data
                new PlaceTask().execute(url_goog);
            }
        });
    }

    private void getCurrentLocation() {
        //initialize task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when success
                if (location != null) {
                    //location not null
                    //get current lat
                    currentLat = location.getLatitude();
                    //get current long
                    currentLong = location.getLongitude();
                    //sync map
                    //View v = null;
                    //getweather(v);



                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //when map is ready
                            map = googleMap;
                            //zoom current location
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong),13
                            ));


                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permission granted
                //call method
                getCurrentLocation();

            }
        }
    }

    public void getweather(View v) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherapi myapi = retrofit.create(weatherapi.class);
        Call<dataGet> example = myapi.getweather(et.getText().toString().trim(), apikey);
        //Call<dataGet> example=myapi.getweather(String.valueOf(currentLat),String.valueOf(currentLong),apikey);
        example.enqueue(new Callback<dataGet>() {
            @Override
            public void onResponse(Call<dataGet> call, Response<dataGet> response) {
                dataGet mydata = response.body();
                Main main = mydata.getMain();
                Double temp = main.getTemp();
                Integer temperature = (int) (temp - 273.15);
                tv.setText("The temperature in "+et.getText().toString().trim()+" is "+  String.valueOf(temperature) + "C");
                bestTimeText.setText("Best time to go out today is 3pm - 4pm. You will have clear skies and moderate temperatures of 15.5 C.");
            }

            @Override
            public void onFailure(Call<dataGet> call, Throwable t) {

            }
        });
    }

    //public void getCurrentLocation(View view) {
    //    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
    //        return;
    //    }
    //    Task<Location> task = fusedLocationProviderClient.getLastLocation();
    //    task.addOnSuccessListener(new OnSuccessListener<Location>(){

    //        @Override
    //        public void onSuccess(Location location) {
                //get current lat
    //            currentLat = location.getLatitude();
                //get current long
    //            currentLong = location.getLongitude();
    //        }
    //    });
        
        

    //}

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                //initialize data
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s) {
            //exe parser task
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //init url
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //connect connection
        connection.connect();
        //init istream
        InputStream stream = connection.getInputStream();
        //init buffer stream
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));
        //init string builder
        StringBuilder builder = new StringBuilder();
        //init string variable
        String line = "";
        //while loop
        while ((line = reader.readLine()) != null) {
            //append line
            builder.append(line);
        }
        //get append data
        String data = builder.toString();
        //close reader
        reader.close();

        return data;
    }


    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create json parser
            JsonParser jsonParser = new JsonParser();
            //init hash map list
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                //init json object
                object = new JSONObject(strings[0]);
                //parse json object
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //ret map list
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //clear map
            map.clear();

            for(int i = 0; i<hashMaps.size();i++){
                //init hash maps
                HashMap<String,String> hashMapList = hashMaps.get(i);
                //get latitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                //get long
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //get name
                String name = hashMapList.get("name");
                //concat lat and lng
                LatLng latLng = new LatLng(lat,lng);
                //init marker options
                MarkerOptions options = new MarkerOptions();
                //set position
                options.position(latLng);

                options.title(name);
                //add markers on map
                map.addMarker(options);
            }
        }
    }
}
