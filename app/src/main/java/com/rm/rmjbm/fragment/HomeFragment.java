package com.rm.rmjbm.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rm.rmjbm.R;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView tvAryaSandeshTVTitle;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        getWidgetRef(v);
        setWidgetEvent();
        init();
        setTextFont();
        return v;
    }

    private void setTextFont() {
        tvAryaSandeshTVTitle.setTypeface(robotoBold);
    }

    private void init() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setWidgetEvent() {

    }

    private void getWidgetRef(View v) {
        tvAryaSandeshTVTitle = v.findViewById(R.id.tvFHAryaSandeshTVTitle);
    }

    @Override
    public void onClick(View v) {
    }
}
