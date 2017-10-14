package com.careweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by forev on 2017/9/30.
 */

public class Location {

    @SerializedName("id")
    public String countyId;

    @SerializedName("name")
    public String countyName;

    public String country;

    public String timezone;

    public String timezone_offset;
}
