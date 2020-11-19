package com.example.Shine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject object){
        //init hash map
        HashMap<String,String> dataList = new HashMap<>();

        try {
            //get name from object
            String name = object.getString("name");
            //get lat from object
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            //get long from object
            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            //put all values in hash map
            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return hash map
        return dataList;
    }
    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        //init hash map
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<jsonArray.length();i++){

            try {
                //init hash map
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //add data in hash map list
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //ret hash map list
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object){
        //init json array
        JSONArray jsonArray = null;

        try {
            //get result array
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return array
        return parseJsonArray(jsonArray);
    }
}
