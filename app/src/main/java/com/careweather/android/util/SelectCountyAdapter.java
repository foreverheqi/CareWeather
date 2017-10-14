package com.careweather.android.util;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.careweather.android.R;
import com.careweather.android.db.SelectCounty;

import java.util.List;

/**
 * Created by forev on 2017/10/4.
 */

public class SelectCountyAdapter extends RecyclerView.Adapter<SelectCountyAdapter.ViewHolder> {
    private List<SelectCounty> mSelectCountyList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        Button btnRemoveCounty;
        TextView county_name;
        TextView now_temperature;
        ImageView now_weather_image;
        public ViewHolder(View view){
            super(view);
            btnRemoveCounty = (Button)view.findViewById(R.id.btnRemoveCounty);
            county_name = (TextView)view.findViewById(R.id.county_name);
            now_temperature = (TextView)view.findViewById(R.id.now_temperature);
            now_weather_image = (ImageView)view.findViewById(R.id.now_weather_image);
        }
    }

    public SelectCountyAdapter(List<SelectCounty> list){
        mSelectCountyList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectr_county_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.btnRemoveCounty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setTitle("确认删除吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = holder.getAdapterPosition();
                                mSelectCountyList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, mSelectCountyList.size() - position);
                                //Toast.makeText(v.getContext(), "you clicked" + position, Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectCounty selectCounty = mSelectCountyList.get(position);
        holder.county_name.setText(selectCounty.getCountyName());
    }

    @Override
    public int getItemCount() {
        return mSelectCountyList.size();
    }

//    public List<SelectCounty> getItems(){
//        return mSelectCountyList;
//    }
}
