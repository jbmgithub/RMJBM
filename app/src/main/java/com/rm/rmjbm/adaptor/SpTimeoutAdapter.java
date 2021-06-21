package com.rm.rmjbm.adaptor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rm.rmjbm.R;

import java.util.ArrayList;

public class SpTimeoutAdapter extends ArrayAdapter<String> {
    private ArrayList<String> list;
    private Context context;
    private Typeface robotoRegular, robotoBold;


    public SpTimeoutAdapter(Context context, int resourceId, ArrayList<String> list) {
        super(context, resourceId, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
//        return list.size() > 0 ? list.size() - 1 : list.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_sp_list, parent, false);
        TextView tvTimeout = v.findViewById(R.id.tvRSLTitle);
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
        tvTimeout.setTypeface(robotoRegular);
        tvTimeout.setText(list.get(position));
        return v;
    }
}
