package com.careweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.careweather.android.db.SelectCounty;
import com.careweather.android.entity.County;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class CountyActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private SearchView searchView;
    private ListView listView;
    private List<County> allCountyList;
    private List<County> findList;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> findAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county);
        searchView = (SearchView) findViewById(R.id.searchEdit);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                County county = findList.get(position);
                SelectCounty selectCounty =  new SelectCounty();
                selectCounty.setCountyId(county.getCountyId());
                selectCounty.setCountyCode(county.getCountyCode());
                selectCounty.setCountyName(county.getCountyName());
                selectCounty.setStatus("1");
                Intent intent = new Intent();
                intent.putExtra("selectCounty", selectCounty);
                setResult(RESULT_OK, intent);
                finish();

                //Toast.makeText(CountyActivity.this, county.getId() + county.getCountyName(), Toast.LENGTH_SHORT).show();
            }
        });
        findList = new ArrayList<County>();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query)
            {

                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(CountyActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < allCountyList.size(); i++)
                    {
                        County county = allCountyList.get(i);
                        if(county.getCountyName().equals(query))
                        {
                            findList.add(county);
                            break;
                        }
                    }
                    if(findList.size() == 0)
                    {
                        Toast.makeText(CountyActivity.this, "查找的城市不在列表中", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(CountyActivity.this, "查找成功", Toast.LENGTH_SHORT).show();
                        refreshCountyListView(findList);
                    }
                }
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText)
            {
                if(TextUtils.isEmpty(newText))
                {
                    listView.setAdapter(adapter);
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < allCountyList.size(); i++)
                    {
                         County county = allCountyList.get(i);
                        if(county.getCountyName().contains(newText))
                        {
                            findList.add(county);
                        }
                    }
                    refreshCountyListView(findList);
                }
                return true;
            }
        });

        allCountyList = DataSupport.findAll(County.class);
        findList.addAll(allCountyList);
        if(allCountyList.size() == 0) {
            new ExcelDataLoader().execute("thinkpage_cities.xls");
        }
        else{
            refreshCountyListView(allCountyList);
        }
    }

    private void refreshCountyListView(List<County> countyList){
        List<String> countyNameList = new ArrayList<>();
        for(County county : countyList){
            countyNameList.add(county.getCountyName());
        }
        findAdapter = new ArrayAdapter<String>(CountyActivity.this, android.R.layout.simple_list_item_1, countyNameList);
        //findAdapter.notifyDataSetChanged();
        listView.setAdapter(findAdapter);
    }

    private final static String TAG = "Get Xls Data";

    private ArrayList<County> getXlsData(String xlsName, int index) {
        ArrayList<County> countyList = new ArrayList<County>();
        AssetManager assetManager = getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 1; i < sheetRows; i++) {
                County county = new County();
                county.setCountyId(sheet.getCell(0, i).getContents());
                county.setCountyName(sheet.getCell(1, i).getContents());
                county.setCountyCode(sheet.getCell(2, i).getContents());
                county.setCountry(sheet.getCell(1, i).getContents());
                county.setCountryCode(sheet.getCell(2, i).getContents());
                county.setProvinceName(sheet.getCell(1, i).getContents());
                county.setProvinceCode(sheet.getCell(2, i).getContents());
                county.setCityName(sheet.getCell(1, i).getContents());
                county.setCityCode(sheet.getCell(2, i).getContents());
                countyList.add(county);
            }

            workbook.close();

        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return countyList;
    }

    private class ExcelDataLoader extends AsyncTask<String, Void, ArrayList<County>> {

        @Override
        protected void onPreExecute() {
            //progressDialog.setMessage("加载中,请稍后......");
            //progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();
        }

        @Override
        protected ArrayList<County> doInBackground(String... params) {
            Log.e(TAG, "File name:" + params[0]);
            return getXlsData(params[0], 1);
        }

        @Override
        protected void onPostExecute(ArrayList<County> countyList) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }

            if(countyList != null && countyList.size()>0){
                SQLiteDatabase db = Connector.getDatabase();
                DataSupport.saveAll(countyList);
                //存在数据
                //sortByName(countryModels);
                //setupData(countryModels);
                //Toast.makeText(CountyActivity.this, "Count" + countyList.size(), Toast.LENGTH_SHORT).show();
                findList = countyList;
                allCountyList = countyList;
                refreshCountyListView(countyList);
//                List<String> countyNameList = new ArrayList<>();
//                for(County county : countyList){
//                    countyNameList.add(county.getCountyName());
//                }
//                adapter = new ArrayAdapter<String>(CountyActivity.this, android.R.layout.simple_list_item_1, countyNameList);
//                listView.setAdapter(adapter);
            }else {
                //加载失败
                Toast.makeText(CountyActivity.this,"读取Excel失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
