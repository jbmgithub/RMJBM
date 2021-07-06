package com.rm.rmjbm.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rm.rmjbm.R;

public class AppVersionFragment extends Fragment {
    private TextView tvAppVersion, tvJBM, tvPoweredBy, tvRawMAterial, tvRmJBM;
    private Typeface robotoRegular, robotoBold, robotoItalic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_version, container, false);
        getWidgetRef(v);
        setFontStyle();
        setTextFont();
        return v;
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setTextFont() {
        tvAppVersion.setTypeface(robotoRegular);
        tvJBM.setTypeface(robotoBold);
        tvPoweredBy.setTypeface(robotoRegular);
        tvRawMAterial.setTypeface(robotoBold);
        tvRmJBM.setTypeface(robotoBold);
    }

    private void getWidgetRef(View v) {
        tvAppVersion = v.findViewById(R.id.tvFAVAppVersion);
        tvJBM = v.findViewById(R.id.tvFAVJBM);
        tvPoweredBy = v.findViewById(R.id.tvFAVPoweredby);
        tvRawMAterial = v.findViewById(R.id.tvFAVRawMaterial);
        tvRmJBM = v.findViewById(R.id.tvFAVRmJbm);
    }


}