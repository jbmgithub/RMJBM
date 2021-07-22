package com.rm.rmjbm.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rm.rmjbm.R;
import com.rm.rmjbm.utils.Utils;

import java.util.List;

public class SpMatTransferPostingAdapter extends ArrayAdapter<String> {
    private List<String> list;
    private Context context;

    public SpMatTransferPostingAdapter(Context context, int resourceId, List<String> list) {
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
        TextView tvSpinnerText = v.findViewById(R.id.tvRSLTitle);
        tvSpinnerText.setText(list.get(position));
        return v;
    }
}