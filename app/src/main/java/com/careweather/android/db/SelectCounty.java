package com.careweather.android.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by forev on 2017/9/29.
 */

public class SelectCounty extends DataSupport  implements Serializable {
    private int id;

    private String status;

    private String countyId;

    private String countyName;

    private String countyCode;

    private Boolean isDefault;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getCountyId(){
        return countyId;
    }

    public void setCountyId(String countyId){
        this.countyId = countyId;
    }

    public String getCountyName(){
        return countyName;
    }

    public void setCountyName(String countyName){
        this.countyName = countyName;
    }

    public String getCountyCode(){
        return countyCode;
    }

    public void setCountyCode(String countyCode){
        this.countyCode = countyCode;
    }

    public boolean getIsDefault(){
        return isDefault;
    }
    public void setIsDefault(boolean isDefault){
        this.isDefault = isDefault;
    }
}
