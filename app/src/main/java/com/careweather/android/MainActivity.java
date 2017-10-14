package com.careweather.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLoadCounty = (Button)findViewById(R.id.btnLoadCounty);
        btnLoadCounty.setOnClickListener(this);

        Button btnShowWeather = (Button)findViewById(R.id.btnShowWeather);
        btnShowWeather.setOnClickListener(this);

        EditText editText = (EditText)findViewById(R.id.txt1);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoadCounty:
                Intent countyIntent = new Intent(MainActivity.this, CountyActivity.class);
                startActivity(countyIntent);
                break;
            case R.id.btnShowWeather:
                Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(weatherIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(System.currentTimeMillis() - exitTime  > 2000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else{
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
