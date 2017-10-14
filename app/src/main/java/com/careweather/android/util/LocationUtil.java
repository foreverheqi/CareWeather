package com.careweather.android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by forev on 2017/10/12.
 */

public class LocationUtil {
    private String cityName;
    public String getCityName(){
        return cityName;
    }
    private Geocoder geocoder;  //此对象能通过经纬度来获取相应的城市等信息
    private  LocationManager locationManager;

    private  Context myContext;

    private static LocationUtil instance;
    //private static volatile Singleton instance; 遇到编译器问题时使用
    private LocationUtil() {}
    public static LocationUtil getInstance(){
        if (null == instance){
            synchronized(LocationUtil.class){
                if (null == instance)
                    instance = new LocationUtil();
            }

        }
        return instance;
    }

    private MyCallBack mCallBack;

    public interface MyCallBack{
        public abstract void work();
    }

    public void setCallback(MyCallBack mCallBack){
        this.mCallBack = mCallBack;
    }

    private void doWork(){
        if(mCallBack != null) {
            mCallBack.work();
        }
    }
    public void getCNByLocation(Context context){
        myContext = context;
        geocoder = new Geocoder(context);
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        String provider;// 位置提供器
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            //优先使用network
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // 没有可用的位置提供器
            Toast.makeText(context, "没有位置提供器可供使用", Toast.LENGTH_LONG).show();
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);    //低精度   高精度：ACCURACY_FINE
        criteria.setAltitudeRequired(false);       //不要求海拔
        criteria.setBearingRequired(false);       //不要求方位
        criteria.setCostAllowed(false);      //不允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗

        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null) {
            //String tempCityName =
            //Toast.makeText(myContext,  "getLastKnownLocation" , Toast.LENGTH_SHORT).show();
            updateWithNewLocation(location);
//            if((tempCityName!=null)&&(tempCityName.length()!=0)){
//                CityName = tempCityName;
//                RemoveLocationListener();
//            }
        }

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(provider, 30000, 50, locationListener);
        }
    }
    private LocationListener locationListener = new LocationListener() {
        String tempCityName;
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
            //Toast.makeText(myContext,  "onProviderDisabled" , Toast.LENGTH_SHORT).show();
            updateWithNewLocation(null);
//            if((tempCityName!=null)&&(tempCityName.length()!=0)){
//                CityName = tempCityName;
//                RemoveLocationListener();
//            }
        }
        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(myContext,  "onLocationChanged" , Toast.LENGTH_SHORT).show();
            //tempCityName = ;
             updateWithNewLocation(location);
            if((cityName!=null)&&(cityName.length()!=0)){
                RemoveLocationListener();
           }
        }
    };
    //更新location  return cityName
    private void updateWithNewLocation(Location location){
        //Toast.makeText(myContext,  "updateWithNewLocation" , Toast.LENGTH_SHORT).show();
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if(location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();
        }else{
            cityName = "无法获取地理信息";
        }
        try {
            addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addList != null && addList.size()>0){
            for(int i=0; i< addList.size(); i++){
                Address add = addList.get(i);
                mcityName += add.getLocality();
            }
        }
        if(mcityName.length()!=0){
            cityName = mcityName.substring(0, (mcityName.length()-1));
            doWork();
        }else{
            cityName = null;
            //doWork();
        }
    }

    public void RemoveLocationListener(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
        if(locationListener != null){
            locationListener = null;
        }
    }
}
