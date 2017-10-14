package com.careweather.android.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by forev on 2017/9/29.
 */

public class County extends DataSupport{
    private int id;
    private String countyId;
    private String countyName;

    private String countyCode;

    private String country;

    private String countryCode;

    private String provinceName;

    private String provinceCode;

    private String cityName;

    private String cityCode;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCountyId(){
        return countyId;
    }

    public void setCountyId(String id){
        this.countyId = id;
    }

    public String getCountyName(){
        return  countyName;
    }

    public void setCountyName(String countyName){
        this. countyName = countyName;
    }

    public String getCountyCode(){
        return  countyCode;
    }

    public void setCountyCode(String countyCode){
        this. countyCode = countyCode;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getCountryCode(){
        return countryCode;
    }

    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }

    public String getProvinceCode(){
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode){
        this.provinceCode = provinceCode;
    }

    public String getCityName(){
        return cityName;
    }

    public void setCityName(String cityName){
        this.cityName = cityName;
    }

    public String getCityCode(){
        return cityCode;
    }

    public void setCityCode(String cityCode){
        this.cityCode = cityCode;
    }
}
