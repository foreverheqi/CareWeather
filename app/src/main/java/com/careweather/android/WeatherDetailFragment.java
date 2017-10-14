package com.careweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.careweather.android.gson.Daily;
import com.careweather.android.gson.DailyWeather;
import com.careweather.android.gson.NowWeather;
import com.careweather.android.util.HttpUtil;
import com.careweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherDetailFragment extends Fragment implements View.OnClickListener {
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private ImageView weatherInfoImage;
    private LinearLayout forecastLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);

        final String weatherId;
        String countyId = getArguments().getString("countyId");
        //Intent intent = getIntent();
        //String extraWeatherId = intent.getStringExtra("WeatherId");

        if(countyId == null || "".equals(countyId)){
            weatherId = "ningbo";
        }
        else{
            weatherId = countyId;
        }

        Button btnAddCity = (Button)view.findViewById(R.id.btnAddCity);
        btnAddCity.setOnClickListener(this);
        Button btnManageCity = (Button)view.findViewById(R.id.btnManageCity);
        btnManageCity.setOnClickListener(this);

        weatherLayout = (ScrollView)view.findViewById(R.id.weather_layout);
        titleCity = (TextView)view.findViewById(R.id.title_city);
        titleCity.setText(weatherId);
        titleUpdateTime = (TextView)view.findViewById(R.id.title_update_time);
        degreeText = (TextView)view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView)view.findViewById(R.id.weather_info_text);
        weatherInfoImage = (ImageView)view.findViewById(R.id.weather_info_image);
        forecastLayout = (LinearLayout)view.findViewById(R.id.forecast_layout);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorBlack);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        requestWeather(weatherId);
        return view;
    }

    public void requestWeather(final String weatherId){
        String nowWeatherUrl = String.format("https://api.seniverse.com/v3/weather/now.json?key=a0jmz86mxb8qfixi&location=%s&language=zh-Hans&unit=c", weatherId);
        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final NowWeather nowWeather = Utility.handleNowWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nowWeather != null){
                            showNowWeatherInfo(nowWeather);
                        }
                        else{
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        String dailyWeatherUrl = String.format("https://api.seniverse.com/v3/weather/daily.json?key=a0jmz86mxb8qfixi&location=%s&language=zh-Hans&unit=c&start=-1&days=5", weatherId);
        HttpUtil.sendOkHttpRequest(dailyWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final DailyWeather dailyWeather = Utility.handleDailyWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(dailyWeather != null){
                            showDailyWeatherInfo(dailyWeather);
                        }
                        else{
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void showNowWeatherInfo(NowWeather nowWeather){
        try {
            String cityName = nowWeather.location.countyName;
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");//小写的mm表示的是分钟
//            java.util.Date date = sdf.parse(nowWeather.last_update);
            String updateTime = nowWeather.last_update.split("T")[1].substring(0,8);
            String degree = nowWeather.now.temperature + "℃";
            String weatherInfo = nowWeather.now.text;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            weatherInfoImage.setImageResource(getImageId(nowWeather.now.code));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDailyWeatherInfo(DailyWeather dailyWeather){
        forecastLayout.removeAllViews();
        for (Daily daily: dailyWeather.daily){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dateText.setText(daily.date);
            infoText.setText(daily.text_day);
            maxText.setText(daily.high);
            minText.setText(daily.low);
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCity:{
                Intent countyIntent = new Intent(getActivity(), CountyActivity.class);
                getActivity().startActivityForResult (countyIntent, 1);
                break;
            }
            case R.id.btnManageCity:{
                Intent manageCity = new Intent(getActivity(),  ManageCountyActivity.class);
                startActivity(manageCity);
                break;
            }
        }
    }

    private int getImageId(String code){
        switch (code){
            case "0" :return R.drawable.b_0;
            case "1" :return R.drawable.b_1;
            case "2" :return R.drawable.b_2;
            case "3" :return R.drawable.b_3;
            case "4" :return R.drawable.b_4;
            case "5" :return R.drawable.b_5;
            case "6" :return R.drawable.b_6;
            case "7" :return R.drawable.b_7;
            case "8" :return R.drawable.b_8;
            case "9" :return R.drawable.b_9;
            case "10" :return R.drawable.b_10;
            case "11" :return R.drawable.b_11;
            case "12" :return R.drawable.b_12;
            case "13" :return R.drawable.b_13;
            case "14" :return R.drawable.b_14;
            case "15" :return R.drawable.b_15;
            case "16" :return R.drawable.b_16;
            case "17" :return R.drawable.b_17;
            case "18" :return R.drawable.b_18;
            case "19" :return R.drawable.b_19;
            case "20" :return R.drawable.b_20;
            case "21" :return R.drawable.b_21;
            case "22" :return R.drawable.b_22;
            case "23" :return R.drawable.b_23;
            case "24" :return R.drawable.b_24;
            case "25" :return R.drawable.b_25;
            case "26" :return R.drawable.b_26;
            case "27" :return R.drawable.b_27;
            case "28" :return R.drawable.b_28;
            case "29" :return R.drawable.b_29;
            case "30" :return R.drawable.b_30;
            case "31" :return R.drawable.b_31;
            case "32" :return R.drawable.b_32;
            case "33" :return R.drawable.b_33;
            case "34" :return R.drawable.b_34;
            case "35" :return R.drawable.b_35;
            case "36" :return R.drawable.b_36;
            case "37" :return R.drawable.b_37;
            case "38" :return R.drawable.b_38;
            default: return R.drawable.b_99;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 1:{
//                if(resultCode == RESULT_OK){
//
//                }
//            }
//        }
//    }
}
