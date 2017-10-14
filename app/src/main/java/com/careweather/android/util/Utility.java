package com.careweather.android.util;

import com.careweather.android.gson.DailyWeather;
import com.careweather.android.gson.NowWeather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by forev on 2017/9/30.
 */

public class Utility {

    public static NowWeather handleNowWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String weatherString = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherString, NowWeather.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static DailyWeather handleDailyWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String weatherString = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherString, DailyWeather.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
