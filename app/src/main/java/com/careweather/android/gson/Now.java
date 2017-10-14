package com.careweather.android.gson;

/**
 * Created by forev on 2017/9/30.
 */

public class Now {
    //天气现象文字
    public String text;

    //天气现象代码
    public String code;

    //温度，单位为c摄氏度或f华氏度
    public String temperature;

    //体感温度，单位为c摄氏度或f华氏度
    public String feels_like;

    //气压，单位为mb百帕或in英寸
    public String pressure;

    //相对湿度，0~100，单位为百分比
    public String humidity;

    //能见度，单位为km公里或mi英里
    public String visibility;

    //风向文字
    public String wind_direction;

    //风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西
    public String wind_direction_degree;

    //风速
    public String wind_speed;

    //风力等级
    public String wind_scale;

    //云量
    public String clouds;

    //露点温度
    public String dew_point;
}
