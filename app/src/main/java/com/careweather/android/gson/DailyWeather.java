package com.careweather.android.gson;

import java.util.List;

/**
 * Created by forev on 2017/9/30.
 */

//逐日天气预报
public class DailyWeather {
    public Location location;

    public List<Daily> daily;

    //数据更新时间
    public String last_update;
}
