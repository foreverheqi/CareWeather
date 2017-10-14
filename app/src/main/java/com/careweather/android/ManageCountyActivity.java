package com.careweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.careweather.android.db.SelectCounty;
import com.careweather.android.util.SelectCountyAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ManageCountyActivity extends AppCompatActivity implements View.OnClickListener{

    private List<SelectCounty> selectCountyList = new ArrayList<>();
    RecyclerView selectCountyView;
    SelectCountyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_county);

        Button btnAddCity = (Button)findViewById(R.id.btnManageAddCity);
        btnAddCity.setOnClickListener(this);
        Button btnManageCity = (Button)findViewById(R.id.btnSave);
        btnManageCity.setOnClickListener(this);

        selectCountyList = DataSupport.where( "status = ?", "1").find(SelectCounty.class);
        selectCountyView = (RecyclerView)findViewById(R.id.selectCountyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectCountyView.setLayoutManager(layoutManager);
        adapter = new SelectCountyAdapter(selectCountyList);
        selectCountyView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnManageAddCity:{
                Intent countyIntent = new Intent(ManageCountyActivity.this, CountyActivity.class);
                startActivityForResult(countyIntent, 2);
                break;
            }
            case R.id.btnSave:{
                Intent weatherIntent = new Intent(ManageCountyActivity.this,  WeatherActivity.class);
                startActivity(weatherIntent);
                finish();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:{
                if(resultCode == RESULT_OK){
                    SelectCounty selectCounty = (SelectCounty)data.getSerializableExtra("selectCounty");
                    selectCountyList.add(selectCounty);
                    int pos = selectCountyList.size()-1;
                    adapter.notifyItemInserted(pos);
                    adapter.notifyItemRangeChanged(pos, selectCountyList.size()-pos);//通知数据与界面重新绑定
                    break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
//        List<SelectCounty> result = adapter.getItems();
        SelectCounty selectCounty = new SelectCounty();
        selectCounty.setStatus("0");
        selectCounty.updateAll();
        DataSupport.saveAll(selectCountyList);
        super.onPause();
    }
}
