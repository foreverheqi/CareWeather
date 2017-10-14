package com.careweather.android.gson;

/**
 * Created by forev on 2017/9/30.
 */

public class Daily {
    //日期
    public String date;
    //白天天气现象文字
    public String text_day;

    //白天天气现象代码
    public String  code_day;

    //晚间天气现象文字
    public String  text_night;

    //晚间天气现象代码
    public String code_night;

    //当天最高温度
    public String high;

    //当天最低温度
    public String low;

    //降水概率，范围0~100，单位百分比
    public String precip;

    //风向文字
    public String wind_direction;

    //风向角度，范围0~360
    public String wind_direction_degree;

    //风速，单位km/h（当unit=c时）、mph（当unit=f时）
    public String wind_speed;

    //风力等级
    public String wind_scale;
}
