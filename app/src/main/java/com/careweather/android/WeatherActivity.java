package com.careweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.careweather.android.db.SelectCounty;
import com.careweather.android.util.LocationUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity  implements View.OnClickListener{
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;

    private ViewPager viewPager;
    MyAdpter myAdpter;
    private ArrayList<Fragment> list;
    private ImageView imageView;
    /**将小圆点的图片用数组表示*/
    private ImageView[] imageViews;
    private ViewGroup viewPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        LocationUtil.getInstance().getCNByLocation(WeatherActivity.this);
        LocationUtil.getInstance().setCallback(new LocationUtil.MyCallBack() {
            @Override
            public void work() {
                refreshWeather();
            }
        });
        //defaultCity = LocationUtil.CityName;
        //LayoutInflater inflater = getLayoutInflater();



        //创建imageviews数组，大小是要显示的图片的数量
        //imageViews = new ImageView[list.size()];

        //实例化小圆点的linearLayout和viewpager
        //viewPoints = (ViewGroup) findViewById(R.id.viewGroup);


//        //添加小圆点的图片
//        for(int i=0;i< list.size();i++){
//            imageView = new ImageView(WeatherActivity.this);
//            //设置小圆点imageview的参数
//            imageView.setLayoutParams(new ViewPager.LayoutParams());//创建一个宽高均为20 的布局
//            imageView.setPadding(20, 0, 20, 0);
//            //将小圆点layout添加到数组中
//            imageViews[i] = imageView;
//
//            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
//            if(i==0){
//                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focuse);
//            }else{
//                imageViews[i].setBackgroundResource(R.drawable.page_indicator);
//            }
//
//            //将imageviews添加到小圆点视图组
//            viewPoints.addView(imageViews[i]);
//        }

        //显示滑动图片的视图
        //setContentView(viewPics);


        //viewPager.addOnPageChangeListener(new GuidePageChangeListener());

//        Button btnAddCity = (Button)findViewById(R.id.btnAddCity);
//        btnAddCity.setOnClickListener(this);
//        Button btnManageCity = (Button)findViewById(R.id.btnManageCity);
//        btnManageCity.setOnClickListener(this);
//
//        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
//        titleCity = (TextView)findViewById(R.id.title_city);
//        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
//        degreeText = (TextView)findViewById(R.id.degree_text);
//        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
//        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
//        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//            @Override
//            public void onRefresh() {
//                requestWeather(weatherId);
//            }
//        });
//        requestWeather(weatherId);


    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.getInstance().RemoveLocationListener();
    }

    private void refreshWeather(){
        List<SelectCounty> selectCountyList = DataSupport.where( "status = ?", "1").find(SelectCounty.class);
        Bundle bundle;
        WeatherDetailFragment weatherDetailFragment;
        list = new ArrayList<>();
        if(selectCountyList.size() == 0) {
            weatherDetailFragment = new WeatherDetailFragment();
            bundle = new Bundle();
            String defaultCity = LocationUtil.getInstance().getCityName();
            if(defaultCity != null && !"".equals(defaultCity)){
                bundle.putString("countyId",defaultCity);
            }
            else {
                bundle.putString("countyId", "北京");
            }
            weatherDetailFragment.setArguments(bundle);
            list.add(weatherDetailFragment);
        }
        else {
            for (SelectCounty selectCounty : selectCountyList) {
                weatherDetailFragment = new WeatherDetailFragment();
                bundle = new Bundle();
                bundle.putString("countyId", selectCounty.getCountyName());
                weatherDetailFragment.setArguments(bundle);
                list.add(weatherDetailFragment);
            }
        }
        if(myAdpter == null) {
            myAdpter = new MyAdpter(getSupportFragmentManager());
        }
        myAdpter.setFragments(list);
        myAdpter.notifyDataSetChanged();
        viewPager = (ViewPager) findViewById(R.id.weather_details);
        viewPager.setAdapter(myAdpter);
    }

    public class MyAdpter extends FragmentPagerAdapter {

        FragmentManager fm;
        private List<Fragment> fgs = null;
        public MyAdpter(FragmentManager fm) {
            super(fm);
            //this.fgs = fragments;
            this.fm = fm;
        }

        public void setFragments(List<Fragment> fragments) {
            if(this.fgs != null){
                FragmentTransaction ft = fm.beginTransaction();
                for(Fragment f:this.fgs){
                    ft.remove(f);
                }
                ft.commit();
                ft=null;
                fm.executePendingTransactions();
            }
            this.fgs = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fgs.get(position);
        }

        @Override
        public int getCount() {
            return this.fgs.size();
        }
    }

//    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            // TODO Auto-generated method stub
//            for(int i=0;i<imageViews.length;i++){
//                imageViews[position].setBackgroundResource(R.drawable.page_indicator_focuse);
//                //不是当前选中的page，其小圆点设置为未选中的状态
//                if(position !=i){
//                    imageViews[i].setBackgroundResource(R.drawable.page_indicator);
//                }
//            }
//
//        }
//    }

//    public void requestWeather(final String weatherId){
//        String nowWeatherUrl = String.format("https://api.seniverse.com/v3/weather/now.json?key=a0jmz86mxb8qfixi&location=%s&language=zh-Hans&unit=c", weatherId);
//        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                swipeRefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseText = response.body().string();
//                final NowWeather nowWeather = Utility.handleNowWeatherResponse(responseText);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(nowWeather != null){
//                            showNowWeatherInfo(nowWeather);
//                        }
//                        else{
//                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
//                        }
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
//            }
//        });
//
//        String dailyWeatherUrl = String.format("https://api.seniverse.com/v3/weather/daily.json?key=a0jmz86mxb8qfixi&location=%s&language=zh-Hans&unit=c&start=-1&days=5", weatherId);
//        HttpUtil.sendOkHttpRequest(dailyWeatherUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //swipeRefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseText = response.body().string();
//                final DailyWeather dailyWeather = Utility.handleDailyWeatherResponse(responseText);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(dailyWeather != null){
//                            showDailyWeatherInfo(dailyWeather);
//                        }
//                        else{
//                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }
//
//    private void showNowWeatherInfo(NowWeather nowWeather){
//        try {
//            String cityName = nowWeather.location.countyName;
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");//小写的mm表示的是分钟
////            java.util.Date date = sdf.parse(nowWeather.last_update);
//            String updateTime = nowWeather.last_update.split("T")[1].substring(0,8);
//            String degree = nowWeather.now.temperature + "℃";
//            String weatherInfo = nowWeather.now.text;
//            titleCity.setText(cityName);
//            titleUpdateTime.setText(updateTime);
//            degreeText.setText(degree);
//            weatherInfoText.setText(weatherInfo);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void showDailyWeatherInfo(DailyWeather dailyWeather){
//        forecastLayout.removeAllViews();
//        for (Daily daily: dailyWeather.daily){
//            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
//            TextView dateText = (TextView)view.findViewById(R.id.date_text);
//            TextView infoText = (TextView)view.findViewById(R.id.info_text);
//            TextView maxText = (TextView)view.findViewById(R.id.max_text);
//            TextView minText = (TextView)view.findViewById(R.id.min_text);
//            dateText.setText(daily.date);
//            infoText.setText(daily.text_day);
//            maxText.setText(daily.high);
//            minText.setText(daily.low);
//            forecastLayout.addView(view);
//        }
//        weatherLayout.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCity:{
                Intent countyIntent = new Intent(WeatherActivity.this, CountyActivity.class);
                startActivityForResult (countyIntent, 1);
                break;
            }
            case R.id.btnManageCity:{
                Intent manageCity = new Intent(WeatherActivity.this,  ManageCountyActivity.class);
                startActivity(manageCity);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if(resultCode == RESULT_OK){
                    SelectCounty selectCounty = (SelectCounty)data.getSerializableExtra("selectCounty");
                    selectCounty.save();
//                    WeatherDetailFragment weatherDetailFragment = new WeatherDetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("countyId",selectCounty.getCountyName());
//                    weatherDetailFragment.setArguments(bundle);
//                    list.add(weatherDetailFragment);
//                    myAdpter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
