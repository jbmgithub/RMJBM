package com.rm.rmjbm.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.utils.ConstantsUtils;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.URLs;
import com.rm.rmjbm.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MaterialStagingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView tvIssuingLocH, tvReceivingLocH, tvBarcodeH, tvScannedQtyH, tvScannedQty, tvPlantH, tvPlant, tvMaterialNoH, tvMaterialNo, tvBatchNoH, tvBatchNo, tvShortTextH, tvShortText, tvNetQtyH, tvNetQty, tvAvailableStockH, tvAvailableStock, tvMissingQtyH, tvMissingQty, tvUomH, tvUom, tvReqDateH, tvReqDate, tvNumofPalletsH, tvNumofPallets;
    private LinearLayout llDataView, llButtonView;
    private Button btnClear, btnSubmit;
    private EditText etBarcode;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private Spinner spIssuingLoc, spReceivingLoc;
    private ArrayList<LovModel> lovList;
    private ArrayList<String> spLovList;
    private SpLovAdapter arrayAdapter;
    private SessionManagement session;
    private String strUserName, strIMEI, strDevId, strMacID, strPlant, strMatnr, strBatch, strPallet, strQtys, strMatDoc, strQty = "0", strIssuingLovCode, strIssuingLov, strReceivingLovCode, strReceivingLov;
    private AlertDialog.Builder builder;
    private ProgressDialog pd;
    private String strMessage;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                etBarcode.setText("");
                if (Utils.isOnline(getContext())) {
                    callMatMfScan();
                } else {
                    Utils.showNetworkAlert(getContext());
                }
            } else if (msg.what == 1) {
                setTextViewEmpty();

            } else if (msg.what == 2) {
            }
            return false;
        }
    });

    private void setTextViewEmpty() {
        llDataView.setVisibility(View.GONE);
        llButtonView.setVisibility(View.GONE);
        etBarcode.setText("");
        tvScannedQty.setText("");
        tvPlant.setText("");
        tvMaterialNo.setText("");
        tvBatchNo.setText("");
        tvShortText.setText("");
        tvNetQty.setText("");
        tvAvailableStock.setText("");
        tvMissingQty.setText("");
        tvUom.setText("");
        tvReqDate.setText("");
        tvNumofPallets.setText("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_material_satging, container, false);
        getWidgetRef(v);
        setWidgetEvent();
        init();
        return v;
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

    private void setTextFont() {
        tvIssuingLocH.setTypeface(robotoBold);
        tvReceivingLocH.setTypeface(robotoBold);
        tvBarcodeH.setTypeface(robotoBold);
        tvScannedQtyH.setTypeface(robotoBold);
        tvPlantH.setTypeface(robotoBold);
        tvMaterialNoH.setTypeface(robotoBold);
        tvBatchNoH.setTypeface(robotoBold);
        tvShortTextH.setTypeface(robotoBold);
        tvNetQtyH.setTypeface(robotoBold);
        tvAvailableStockH.setTypeface(robotoBold);
        tvMissingQtyH.setTypeface(robotoBold);
        tvUomH.setTypeface(robotoBold);
        tvReqDateH.setTypeface(robotoBold);
        tvNumofPalletsH.setTypeface(robotoBold);
        btnClear.setTypeface(robotoBold);
        btnSubmit.setTypeface(robotoBold);
    }

    private void init() {
        initSession();
        spLovList = new ArrayList<>();
        for (int i = 0; i < lovList.size(); i++)
            spLovList.add(lovList.get(i).getLGORT() + ": " + lovList.get(i).getLGOBE());
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

    private void initSpinnerAdapter() {
        setSpinnerAdapter(spIssuingLoc, spLovList);
        setSpinnerAdapter(spReceivingLoc, spLovList);
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setSpinnerAdapter(Spinner spinner, ArrayList<String> arrayList) {
        arrayAdapter = new SpLovAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void setWidgetEvent() {
        btnClear.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        spIssuingLoc.setOnItemSelectedListener(this);
        spReceivingLoc.setOnItemSelectedListener(this);
        etBarcode.addTextChangedListener(new GenericTextWatcher(etBarcode));
    }

    private void getWidgetRef(View v) {
        spIssuingLoc = v.findViewById(R.id.spFMSIssuingLoc);
        spReceivingLoc = v.findViewById(R.id.spFMSReceivingLoc);
        tvIssuingLocH = v.findViewById(R.id.tvFMSIssuingLoc);
        tvReceivingLocH = v.findViewById(R.id.tvFMSReceivingLoc);
        tvBarcodeH = v.findViewById(R.id.tvFMSBarcode);
        etBarcode = v.findViewById(R.id.etFMSBarcode);
        tvScannedQtyH = v.findViewById(R.id.tvFMSScannedQtyH);
        tvScannedQty = v.findViewById(R.id.tvFMSScannedQty);
        tvPlantH = v.findViewById(R.id.tvFMSPlantH);
        tvPlant = v.findViewById(R.id.tvFMSPlant);
        tvMaterialNoH = v.findViewById(R.id.tvFMSMaterialNoH);
        tvMaterialNo = v.findViewById(R.id.tvFMSMaterialNo);
        tvBatchNoH = v.findViewById(R.id.tvFMSBatchNoH);
        tvBatchNo = v.findViewById(R.id.tvFMSBatchNo);
        tvShortTextH = v.findViewById(R.id.tvFMSShortTextH);
        tvShortText = v.findViewById(R.id.tvFMSShortText);
        tvNetQtyH = v.findViewById(R.id.tvFMSNetQtyH);
        tvNetQty = v.findViewById(R.id.tvFMSNetQty);
        tvAvailableStockH = v.findViewById(R.id.tvFMSAvailableStockH);
        tvAvailableStock = v.findViewById(R.id.tvFMSAvailableStock);
        tvMissingQtyH = v.findViewById(R.id.tvFMSMissingQtyH);
        tvMissingQty = v.findViewById(R.id.tvFMSMissingQty);
        tvUomH = v.findViewById(R.id.tvFMSUomH);
        tvUom = v.findViewById(R.id.tvFMSUom);
        tvReqDateH = v.findViewById(R.id.tvFMSReqDateH);
        tvReqDate = v.findViewById(R.id.tvFMSReqDate);
        tvNumofPalletsH = v.findViewById(R.id.tvFMSNumOfPalletsH);
        tvNumofPallets = v.findViewById(R.id.tvFMSNumOfPallets);
        llDataView = v.findViewById(R.id.llFMSDataView);
        llButtonView = v.findViewById(R.id.llFMSButtonView);
        btnClear = v.findViewById(R.id.btnFMSClear);
        btnSubmit = v.findViewById(R.id.btnFMSSubmit);


    }

    private void ShowAlertDialog(final String strAction) {
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setMessage("Are you sure to delete the data?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Utils.isOnline(getContext())) {
                    callClearSubmit(strAction);
                } else {
                    Utils.showNetworkAlert(getContext());
                }
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void callClearSubmit(final String strAction) {
        pd.setTitle("Loading...");
        pd.show();
        String url = URLs.URL_BASEURL + URLs.URL_SUBMIT_CLEAR;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println("JSON::display" + jsonObject);
                                if (jsonObject != null) {
                                    JSONObject jsonObj = jsonObject.getJSONObject("MT_RM_BC_MAT_311_SUBMITCLEAR_REC");
                                    if (jsonObj.getString("STATUS").equalsIgnoreCase("S"))
                                        handler.sendEmptyMessage(1);
                                    if (jsonObj.getString("STATUS").equalsIgnoreCase("E") | jsonObj.getString("STATUS").equalsIgnoreCase("")) {
                                        llDataView.setVisibility(View.GONE);
                                        llButtonView.setVisibility(View.GONE);
                                    }
                                    Utils.showCustomToast(jsonObj.getString("MESSAGE"), getContext());
                                    pd.dismiss();
                                }
                            } catch (JSONException e) {
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
                        "<ns0:MT_RM_BC_MAT_311_SUBMITCLEAR_SND xmlns:ns0=\"http://RM_BC_MAT_311_SUBMITCLEAR\">\n" +
                        "   <MATNR>" + strMatnr + "</MATNR>" +
                        "   <WERKS>" + strPlant + "</WERKS>" +
//                        "   <I_LGORT>" + "RM00" + "</I_LGORT>" +
//                        "   <R_LGORT>" + "WP00" + "</R_LGORT>" +
                        "   <I_LGORT>" + strIssuingLovCode + "</I_LGORT>" +
                        "   <R_LGORT>" + strReceivingLovCode + "</R_LGORT>" +
                        "   <UNAME>" + strUserName + "</UNAME>" +
                        "   <ACTION>" + strAction + "</ACTION>" +
                        "</ns0:MT_RM_BC_MAT_311_SUBMITCLEAR_SND>\n\n";

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
//            Snackbar.make(coordinatorLayout, "Barcode should not be empty", Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == btnClear) {
            ShowAlertDialog("CLEAR");
        } else if (v == btnSubmit) {
            ShowAlertDialog("POST");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spFMSIssuingLoc:
                String[] issuingSeparated = spIssuingLoc.getSelectedItem().toString().trim().split(Pattern.quote(": "));
                strIssuingLovCode = issuingSeparated[0].trim();
                strIssuingLov = issuingSeparated[1].trim();
                break;
            case R.id.spFMSReceivingLoc:
                String[] receivingSeparated = spReceivingLoc.getSelectedItem().toString().trim().split(Pattern.quote(": "));
                strReceivingLovCode = receivingSeparated[0].trim();
                strReceivingLov = receivingSeparated[1].trim();
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
                case R.id.etFMSBarcode:
                    if (text.isEmpty() || text.length() == 0 || text.equals(null)) {
                    } else {
                        String[] separated = text.split(Pattern.quote("/"));
                        System.out.println("LeL:: " + separated.length);
                        if ((separated.length == 5)) {
                            if (isValidBarCode()) {
                                if (Utils.isOnline(getContext())) {
                                    callMatBatchScan();
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
    }

    private void callMatBatchScan() {
        //Barcode
        // 3006438/0000013245/1/50/5000037639
        pd.setTitle("Loading...");
        pd.show();
        String url = URLs.URL_BASEURL + URLs.URL_MATBATCH_SCAN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println("JSON::display" + jsonObject);
                                if (jsonObject != null) {
                                    JSONObject jsonObj = jsonObject.getJSONObject("MT_RM_BC_MATBATCH_SCAN_REC");
                                    strQty = jsonObj.getString("QTY");
                                    pd.dismiss();
                                    strMessage = jsonObj.getString("MESSAGE");
                                    if (jsonObj.getString("STATUS").equalsIgnoreCase("S")) {
                                        Utils.showCustomToast(strMessage, getContext());
                                    } else {
                                        ShowResponseAlertDialog(strMessage);
                                    }
                                    handler.sendEmptyMessage(0);
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
                        "<ns0:MT_RM_BC_MATBATCH_SCAN_SND xmlns:ns0=\"http://RM_BC_MATBATCH_SCAN\">\n" +
                        "   <MATNR>" + strMatnr + "</MATNR>" +
                        "   <WERKS>" + strPlant + "</WERKS>" +
                        "   <I_LGORT>" + strIssuingLovCode + "</I_LGORT>" +
                        "   <R_LGORT>" + strReceivingLovCode + "</R_LGORT>" +
                        "   <CHARG>" + strBatch + "</CHARG>" +
                        "   <BARCODE>" + etBarcode.getText().toString().trim() + "</BARCODE>" +
                        "   <UNAME>" + strUserName + "</UNAME>" +
                        "</ns0:MT_RM_BC_MATBATCH_SCAN_SND>\n\n";

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

    private void callMatMfScan() {
        pd.setTitle("Loading...");
        pd.show();

        String url = URLs.URL_BASEURL + URLs.URL_MAT_MF60_SCAN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println("JSON::display" + jsonObject);
                                if (jsonObject != null) {
                                    JSONObject jsonObj = jsonObject.getJSONObject("MT_RM_BC_MAT_MF60_SCAN_REC");
                                    if (jsonObj.getString("STATUS").equalsIgnoreCase("S")) {
                                        llDataView.setVisibility(View.VISIBLE);
                                        llButtonView.setVisibility(View.VISIBLE);
                                        tvScannedQty.setText(jsonObj.getString("SCAN_QTY"));
                                        tvPlant.setText(jsonObj.getString("WERKS"));
                                        tvMaterialNo.setText(Utils.trimLeadingZeros(jsonObj.getString("MATNR_NO")));
                                        tvBatchNo.setText(Utils.trimLeadingZeros(strBatch));
                                        tvShortText.setText(jsonObj.getString("MAKTX"));
                                        tvNetQty.setText(jsonObj.getString("NBDMG"));
                                        tvAvailableStock.setText(jsonObj.getString("AVAIL_TOT"));
                                        tvMissingQty.setText(jsonObj.getString("MISSQ"));
                                        tvUom.setText(jsonObj.getString("ERFME"));
                                        tvReqDate.setText(jsonObj.getString("BDTER"));
                                        tvNumofPallets.setText(jsonObj.getString("NO_OF_PALLETS"));

                                    } else {

                                        if (!strMessage.equalsIgnoreCase("Batch Stock Not Found")) {
                                            llDataView.setVisibility(View.GONE);
                                            llButtonView.setVisibility(View.GONE);
                                            ShowResponseAlertDialog(jsonObj.getString("MESSAGE"));
                                        }
                                    }
//                                    Utils.showCustomToast(jsonObj.getString("MESSAGE"), getContext());
//                                    handler.sendEmptyMessage(0);
                                    pd.dismiss();
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
                        "<ns0:MT_RM_BC_MAT_MF60_SCAN_SND xmlns:ns0=\"http://RM_BC_MAT_MF60_SCAN\">\n" +
                        "   <MATNR>" + strMatnr + "</MATNR>" +
                        "   <WERKS>" + strPlant + "</WERKS>" +
//                        "   <I_LGORT>" + "RM00" + "</I_LGORT>" +
//                        "   <R_LGORT>" + "WP00" + "</R_LGORT>" +
                        "   <I_LGORT>" + strIssuingLovCode + "</I_LGORT>" +
                        "   <R_LGORT>" + strReceivingLovCode + "</R_LGORT>" +
                        "</ns0:MT_RM_BC_MAT_MF60_SCAN_SND>\n\n";

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
}
