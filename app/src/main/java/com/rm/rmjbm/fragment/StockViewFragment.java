package com.rm.rmjbm.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class StockViewFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextView tvStoreLocH, tvBarcodeH, tvMaterialNoH, tvMaterialNo, tvMaterialDescH, tvMaterialDesc, tvTotalStocksH, tvTotalStocks, tvStoreLocT, tvBatchNumberT, tvStockT;
    private EditText etBarcode;
    private Spinner spStoreLoc;
    private RecyclerView recyclerView;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private String strUserName, strIMEI, strDevId, strMacID, strPlant, strMatnr, strBatch, strPallet, strQtys, strMatDoc, strStoreLovCode, strStoreLov, strMaterialDesc;
    private SessionManagement session;
    private ArrayList<LovModel> lovList;
    private ArrayList<String> lovListFinal;
    private SpLovAdapter arrayAdapter;
    private ProgressDialog pd;
    private List<StockDataModel> displayList;
    private StockDataAdapter mAdapter;
    private LinearLayout llDataView, llListView;
    private AlertDialog.Builder builder;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                pd.dismiss();
                if (!displayList.isEmpty()) {
                    tvMaterialNo.setText(Utils.trimLeadingZeros(displayList.get(0).getMATNR()));
                    tvMaterialDesc.setText(strMaterialDesc);
                    float total = 0.0f;
                    for (StockDataModel model : displayList) {
                        total += Float.parseFloat(model.getCLABS());
                    }
                    tvTotalStocks.setText(String.format("%.0f", total));
                    setRecyclerViewAdapter();
                }
            } else if (msg.what == 1) {

            } else if (msg.what == 2) {
            }
            return false;
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock_view, container, false);
        getWidgetRef(v);
        setWidgetEvent();
        init();
        return v;
    }

    private void setRecyclerViewAdapter() {
        mAdapter = new StockDataAdapter(displayList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setTextFont() {
        tvStoreLocH.setTypeface(robotoBold);
        tvBarcodeH.setTypeface(robotoBold);
        tvMaterialNoH.setTypeface(robotoBold);
        tvMaterialNo.setTypeface(robotoRegular);
        tvMaterialDescH.setTypeface(robotoBold);
        tvMaterialDesc.setTypeface(robotoRegular);
        tvTotalStocksH.setTypeface(robotoBold);
        tvTotalStocks.setTypeface(robotoRegular);
        tvStoreLocT.setTypeface(robotoBold);
        tvBatchNumberT.setTypeface(robotoBold);
        tvStockT.setTypeface(robotoBold);
    }

    private void initSpinnerAdapter() {
        setSpinnerAdapter(spStoreLoc, lovListFinal);
    }

    private void setSpinnerAdapter(Spinner spinner, ArrayList<String> arrayList) {
        arrayAdapter = new SpLovAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void initSession() {
        session = new SessionManagement(getContext());
        if (!session.isLoggedIn()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            HashMap<String, String> user = session.getUserDetails();
            strUserName = user.get(SessionManagement.KEY_USERNAME);
            strDevId = user.get(SessionManagement.KEY_DEVICEID);
            strIMEI = user.get(SessionManagement.KEY_IMEI);
            strMacID = user.get(SessionManagement.KEY_MACID);
            strPlant = user.get(SessionManagement.KEY_USERPLANT);

            Gson gson = new Gson();
            String json = user.get(SessionManagement.KEY_LOV_LIST);
            if (json.isEmpty()) {
                Toast.makeText(getActivity(), "There is something error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<LovModel>>() {
                }.getType();
                lovList = gson.fromJson(json, type);
                System.out.println("Size::: " + lovList.size());
            }
        }
    }

    private void init() {
        initSession();
        lovListFinal = new ArrayList<>();
        displayList = new ArrayList<>();
        for (int i = 0; i < lovList.size(); i++)
            lovListFinal.add(lovList.get(i).getLGORT() + ": " + lovList.get(i).getLGOBE());
        setFontStyle();
        setTextFont();

        initSpinnerAdapter();

        pd = new ProgressDialog(getContext());
        pd.setCancelable(false);


        etBarcode.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        etBarcode.setLongClickable(false);
        etBarcode.setTextIsSelectable(false);
        etBarcode.setInputType(InputType.TYPE_NULL);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            etBarcode.setRawInputType(InputType.TYPE_CLASS_TEXT);
            etBarcode.setTextIsSelectable(true);
        }
    }

    private void setWidgetEvent() {
        spStoreLoc.setOnItemSelectedListener(this);
        etBarcode.addTextChangedListener(new GenericTextWatcher(etBarcode));
    }

    private void getWidgetRef(View v) {
        llDataView = v.findViewById(R.id.llFSVDataView);
        llListView = v.findViewById(R.id.llFSVListView);
        tvStoreLocH = v.findViewById(R.id.tvFSVStoreLocH);
        tvBarcodeH = v.findViewById(R.id.tvFSVBarcodeH);
        tvMaterialNoH = v.findViewById(R.id.tvFSVMaterialNoH);
        tvMaterialNo = v.findViewById(R.id.tvFSVMaterialNo);
        tvMaterialDescH = v.findViewById(R.id.tvFSVMaterialDescH);
        tvMaterialDesc = v.findViewById(R.id.tvFSVMaterialDesc);
        tvTotalStocksH = v.findViewById(R.id.tvFSVTotalStocksH);
        tvTotalStocks = v.findViewById(R.id.tvFSVTotalStocks);
        tvStoreLocT = v.findViewById(R.id.tvFSVStoreLocT);
        tvBatchNumberT = v.findViewById(R.id.tvFSVBatchNumberT);
        tvStockT = v.findViewById(R.id.tvFSVStockT);
        recyclerView = v.findViewById(R.id.rvFSVRecyclerView);
        etBarcode = v.findViewById(R.id.etFSVBarcode);
        spStoreLoc = v.findViewById(R.id.spFSVStoreLoc);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spFSVStoreLoc:
                String[] issuingSeparated = spStoreLoc.getSelectedItem().toString().trim().split(Pattern.quote(": "));
                strStoreLovCode = issuingSeparated[0].trim();
                strStoreLov = issuingSeparated[1].trim();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            System.out.println("Text::: " + charSequence);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
//            String text = "3006438/0000013245/1/50/5000037639";
            switch (view.getId()) {
                case R.id.etFSVBarcode:
                    if (text.isEmpty() || text.length() == 0 || text.equals(null)) {
                    } else {
                        String[] separated = text.split(Pattern.quote("/"));
                        System.out.println("LeL:: " + separated.length);
                        if ((separated.length == 5)) {
                            if (isValidBarCode()) {
                                if (Utils.isOnline(getContext())) {
                                    callDisplayList();
                                } else {
                                    Utils.showNetworkAlert(getContext());
                                    etBarcode.setText("");
                                }
                            }
                        } else {
                            Utils.showCustomToast("Please insert correct barcode...", getContext());
                            etBarcode.setText("");
                        }
                    }
                    break;
            }
        }

        private boolean isValidBarCode() {
            if (etBarcode.getText().toString().length() != 0) {
                String[] separated = etBarcode.getText().toString().trim().split(Pattern.quote("/"));
                strMatnr = separated[0].trim();
                strBatch = separated[1].trim();
                strPallet = separated[2].trim();
                strQtys = separated[3].trim();
                strMatDoc = separated[4].trim();
                return true;
            } else {
                Utils.showCustomToast("Barcode should not be empty", getContext());
            }
            return false;
        }
    }

    private void callDisplayList() {
        //Barcode
        // 3006438/0000013245/1/50/5000037639
        pd.setTitle("Loading...");
        pd.show();
        String url = URLs.URL_BASEURL + URLs.URL_DISPLAY_LIST;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println("JSON::display" + jsonObject);
                                if (jsonObject != null) {
                                    JSONObject jsonObj = jsonObject.getJSONObject("MT_RM_BC_MAT_DISPLAYLIST_REC");
                                    pd.dismiss();
                                    displayList.clear();
                                    if (jsonObj.getString("STATUS").equalsIgnoreCase("S")) {
                                        llDataView.setVisibility(View.VISIBLE);
                                        llListView.setVisibility(View.VISIBLE);
                                        strMaterialDesc = jsonObj.getString("MAKTX");

                                        JSONObject jsonLabelObj = jsonObj.getJSONObject("STOCK_DATA");
                                        if (jsonLabelObj.has("ITEM")) {
                                            JSONObject itemObject = jsonLabelObj.optJSONObject("ITEM");
                                            if (itemObject != null) {
                                                StockDataModel sdModel = new StockDataModel();
                                                sdModel.setMATNR(itemObject.get("MATNR").toString());
                                                sdModel.setWERKS(itemObject.get("WERKS").toString());
                                                sdModel.setLGORT(itemObject.get("LGORT").toString());
                                                sdModel.setCHARG(itemObject.get("CHARG").toString());
                                                sdModel.setCLABS(itemObject.get("CLABS").toString());
                                                sdModel.setCUMLM(itemObject.get("CUMLM").toString());
                                                sdModel.setCINSM(itemObject.get("CINSM").toString());
                                                displayList.add(sdModel);
                                            } else {
                                                JSONArray itemArray = jsonLabelObj.optJSONArray("ITEM");
                                                for (int i = 0; i < itemArray.length(); i++) {
                                                    JSONObject obj = itemArray.getJSONObject(i);
                                                    StockDataModel sdModel = new StockDataModel();
                                                    sdModel.setMATNR(obj.get("MATNR").toString());
                                                    sdModel.setWERKS(obj.get("WERKS").toString());
                                                    sdModel.setLGORT(obj.get("LGORT").toString());
                                                    sdModel.setCHARG(obj.get("CHARG").toString());
                                                    sdModel.setCLABS(obj.get("CLABS").toString());
                                                    sdModel.setCUMLM(obj.get("CUMLM").toString());
                                                    sdModel.setCINSM(obj.get("CINSM").toString());
                                                    displayList.add(sdModel);
                                                }
                                            }
                                        }
                                        handler.sendEmptyMessage(0);
                                    } else {
                                        llDataView.setVisibility(View.GONE);
                                        llListView.setVisibility(View.GONE);
                                        ShowResponseAlertDialog(jsonObj.getString("MESSAGE"));
//                                        Utils.showCustomToast(jsonObj.getString("MESSAGE"), getContext());
                                    }
                                    etBarcode.setText("");
                                    // ShowResponseAlertDialog(jsonObj.getString("MESSAGE"));
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        } else {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = ConstantsUtils.TAG_NETWORK_ERROR;
                        } else if (error instanceof ServerError) {
                            message = ConstantsUtils.TAG_SERVER_ERROR;
                        } else if (error instanceof AuthFailureError) {
                            message = ConstantsUtils.TAG_NETWORK_ERROR1;
                        } else if (error instanceof ParseError) {
                            message = ConstantsUtils.TAG_PARSE_ERROR;
                        } else if (error instanceof NoConnectionError) {
                            message = ConstantsUtils.TAG_NETWORK_ERROR2;
                        } else if (error instanceof TimeoutError) {
                            message = ConstantsUtils.TAG_TIMEOUT_ERROR;
                        }
                        pd.dismiss();
                        Utils.showCustomToast(message, getContext());
                    }
                }) {
            //Tried this because someone said to try it.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String credentials = URLs.TAG_USER + ":" + URLs.TAG_PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "apllication/xml;" + getParamsEncoding();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
//                System.out.println("Post:: " + etUserId.getText().toString().trim() + " :: " + etPassword.getText().toString().trim() + " M:: " + ConstantsUtils.TAG_MACID + " U:: " + ConstantsUtils.TAG_UUID + " IP:: " + etServerIP.getText().toString() + " W " + etPlant.getText().toString());
                String postData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<ns0:MT_RM_BC_MAT_DISPLAYLIST_SND xmlns:ns0=\"http://RM_BC_MAT_DISPLAYLIST\">\n" +
                        "   <MATNR>" + strMatnr + "</MATNR>" +
                        "   <WERKS>" + strPlant + "</WERKS>" +
                        "   <I_LGORT>" + strStoreLovCode + "</I_LGORT>" +
                        "</ns0:MT_RM_BC_MAT_DISPLAYLIST_SND>\n\n";

                System.out.println("Post:: " + postData);
                // TODO get your final output
                try {
                    return postData == null ? null :
                            postData.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    // TODO consider if some other action should be taken
                    return null;
                }
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                ConstantsUtils.TAG_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void ShowResponseAlertDialog(String msg) {
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}