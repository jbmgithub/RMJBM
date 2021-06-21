package com.rm.rmjbm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rm.rmjbm.R;
import com.rm.rmjbm.VolleySetting.AppController;
import com.rm.rmjbm.activity.LoginActivity;
import com.rm.rmjbm.adaptor.SpLovAdapter;
import com.rm.rmjbm.adaptor.StockDataAdapter;
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.model.StockDataModel;
import com.rm.rmjbm.utils.ConstantsUtils;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.URLs;
import com.rm.rmjbm.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AppVersionFragment extends Fragment {
    private TextView tvAppVersion, tvJbmGroup;
    private Typeface robotoRegular, robotoBold, robotoItalic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_version, container, false);
        getWidgetRef(v);
        setFontStyle();
        return v;
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setTextFont() {
        tvAppVersion.setTypeface(robotoRegular);
        tvJbmGroup.setTypeface(robotoRegular);

    }

    private void getWidgetRef(View v) {
        tvAppVersion = v.findViewById(R.id.tvFAPAppVersion);
        tvJbmGroup = v.findViewById(R.id.tvFAPJbmGroup);
    }


}