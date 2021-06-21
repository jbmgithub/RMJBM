package com.rm.rmjbm.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.rm.rmjbm.R;
import com.rm.rmjbm.VolleySetting.AppController;
import com.rm.rmjbm.adaptor.SpTimeoutAdapter;
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.utils.ConstantsUtils;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.URLs;
import com.rm.rmjbm.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etServerIP, etSAPUser, etSAPPassword, etUserId, etPassword, etPlant;
    private TextView tvLoginToContinue, tvServerIP, tvSAPUser, tvSAPPassword, tvTimeout, tvUserId, tvPassword, tvPlant;
    private Button btnLogin, btnResetServerDetails;
    private LinearLayout llServerDetails, llUserDetails;
    private ArrayList<String> timeoutList;
    private ArrayList<LovModel> lovList;
    private Spinner spTimeout;
    private SpTimeoutAdapter timeoutAdapter;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private InputFilter[] filters;
    private ProgressDialog pd;
    private SessionManagement session;
    private int count = 0;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                pd.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWidgetReference();
        bindWidgetEvent();
        initData();
    }

    private void setTextFont() {
        tvLoginToContinue.setTypeface(robotoBold);
        tvServerIP.setTypeface(robotoRegular);
        tvSAPUser.setTypeface(robotoRegular);
        tvSAPPassword.setTypeface(robotoRegular);
        tvTimeout.setTypeface(robotoRegular);
        tvUserId.setTypeface(robotoRegular);
        tvPassword.setTypeface(robotoRegular);
        tvPlant.setTypeface(robotoRegular);
    }

    private void initFont() {
        robotoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setMySpinner() {
        timeoutList = new ArrayList<>();
//        customerCodeList.add(getString(R.string.select));
        timeoutList.add("5 Minutes");
        timeoutList.add("10 Minutes");
        timeoutList.add("15 Minutes");
        timeoutList.add("20 Minutes");
        timeoutList.add("25 Minutes");
        timeoutList.add("30 Minutes");
        setSpinnerAdapter(spTimeout, timeoutList);
    }

    private void setSpinnerAdapter(Spinner spinner, ArrayList<String> arrayList) {
        timeoutAdapter = new SpTimeoutAdapter(getApplicationContext(), R.layout.row_sp_list, arrayList);
        spinner.setAdapter(timeoutAdapter);
        System.out.println("print:: " + timeoutAdapter.getPosition("10 Minutes"));
        spinner.setSelection(timeoutAdapter.getPosition("10 Minutes"));
    }

    private void initData() {
        session = new SessionManagement(getApplicationContext());
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        setMySpinner();
        initFont();
        setTextFont();
        isValidIPAddress();
        etServerIP.setFilters(filters);
    }

    private void isValidIPAddress() {
        filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
    }

    private void bindWidgetEvent() {
        btnLogin.setOnClickListener(this);
        btnResetServerDetails.setOnClickListener(this);
    }

    private void setWidgetReference() {
        tvLoginToContinue = findViewById(R.id.tvALLoginToContinue);
        tvServerIP = findViewById(R.id.tvALServerIP);
        tvSAPUser = findViewById(R.id.tvALSAPUser);
        tvSAPPassword = findViewById(R.id.tvALSAPPassword);
        tvTimeout = findViewById(R.id.tvALTimeout);
        tvUserId = findViewById(R.id.tvALUserID);
        tvPassword = findViewById(R.id.tvALPassword);
        tvPlant = findViewById(R.id.tvALPlant);
        etServerIP = findViewById(R.id.etALServerIP);
        etSAPUser = findViewById(R.id.etALSapUser);
        etSAPPassword = findViewById(R.id.etALSapPassword);
        etUserId = findViewById(R.id.etALUserID);
        etPassword = findViewById(R.id.etALPassword);
        etPlant = findViewById(R.id.etALPlant);
        llServerDetails = findViewById(R.id.llALServerDetail);
        llUserDetails = findViewById(R.id.llALDetail);
        btnLogin = findViewById(R.id.btnALLogin);
        btnResetServerDetails = findViewById(R.id.btnALResetServerDetail);
        spTimeout = findViewById(R.id.spALTimeout);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            if (!isValidUserId(etUserId)) {
                return;
            } else if (!isValidUserPassword(etPassword)) {
                return;
            } else if (!isValidPlant(etPlant)) {
                return;
            } else if (!isValidServerIP(etServerIP)) {
                return;
            } else if (!isValidSAPUser(etSAPUser)) {
                return;
            } else if (!isValidSAPPassword(etSAPPassword)) {
                return;
            } else {
                if (Utils.isOnline(getApplicationContext()))
                    callLoginService();
                else
                    Utils.showNetworkAlert(this);
            }
        } else if (v == btnResetServerDetails) {
            if (count != 0) {
                llServerDetails.setVisibility(View.GONE);
                count = 0;
            } else {
                llServerDetails.setVisibility(View.VISIBLE);
                count++;
            }
        }
    }

    private boolean isValidUserId(EditText etUserId) {
        if (etUserId.getText().toString().length() != 0) {
            etUserId.setError(null);
            return true;
        } else {
            etUserId.requestFocus();
            etUserId.setError(getString(R.string.err_msg_user_id));
        }
        return false;
    }

    private boolean isValidServerIP(EditText etServerIP) {
        if (etServerIP.getText().toString().length() != 0) {
            etServerIP.setError(null);
            return true;
        } else {
            etServerIP.requestFocus();
            etServerIP.setError(getString(R.string.err_msg_server_ip));
        }
        return false;
    }

    private boolean isValidSAPUser(EditText etSAPUser) {
        if (etSAPUser.getText().toString().length() != 0) {
            etSAPUser.setError(null);
            return true;
        } else {
            etSAPUser.requestFocus();
            etSAPUser.setError(getString(R.string.err_msg_sap_user));
        }
        return false;
    }

    private boolean isValidSAPPassword(EditText etSAPPassword) {
        if (etSAPPassword.getText().toString().length() != 0) {
            etSAPPassword.setError(null);
            return true;
        } else {
            etSAPPassword.requestFocus();
            etSAPPassword.setError(getString(R.string.err_msg_sap_password));
        }
        return false;
    }

    private boolean isValidUserPassword(EditText etUserPassword) {
        if (etUserPassword.getText().toString().length() != 0) {
            etUserPassword.setError(null);
            return true;
        } else {
            etUserPassword.requestFocus();
            etUserPassword.setError(getString(R.string.err_msg_user_password));
        }
        return false;
    }

    private boolean isValidPlant(EditText etPlant) {
        if (etPlant.getText().toString().length() != 0) {
            etPlant.setError(null);
            return true;
        } else {
            etPlant.requestFocus();
            etPlant.setError(getString(R.string.err_msg_user_plant));
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pd != null && pd.isShowing())
            pd.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void callLoginService() {
        pd.setTitle("Loading...");
        pd.show();
        String url = URLs.URL_BASEURL + URLs.URL_LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.println("JSON::display" + jsonObject);
                                if (jsonObject != null) {
                                    JSONObject jsonObj = jsonObject.getJSONObject("MT_RM_BC_UValidate_REC");
                                    if (jsonObj.getString("SUCCESS").equalsIgnoreCase("1")) {
                                        lovList = new ArrayList<>();
                                        JSONObject storageLocObj = jsonObj.getJSONObject("STOR_LOC");
                                        if (storageLocObj.has("ITEM")) {
                                            JSONObject itemObject = storageLocObj.optJSONObject("ITEM");
                                            if (itemObject != null) {
                                                LovModel model = new LovModel();
                                                model.setLGOBE(itemObject.getString("LGOBE"));
                                                model.setLGORT(itemObject.getString("LGORT"));
                                                lovList.add(model);
                                            } else {
                                                JSONArray itemArray = storageLocObj.getJSONArray("ITEM");
                                                for (int i = 0; i < itemArray.length(); i++) {
                                                    JSONObject itemObj = itemArray.getJSONObject(i);
                                                    LovModel model = new LovModel();
                                                    model.setLGOBE(itemObj.getString("LGOBE"));
                                                    model.setLGORT(itemObj.getString("LGORT"));
                                                    lovList.add(model);
                                                }
                                            }
                                            Gson gson = new Gson();
                                            String jsonLovList = gson.toJson(lovList);
                                            session.createLoginSession(etUserId.getText().toString().trim(), etPassword.getText().toString().trim(),
                                                    etPlant.getText().toString().trim(), ConstantsUtils.TAG_UUID, ConstantsUtils.TAG_IMEI,
                                                    ConstantsUtils.TAG_MACID, jsonLovList);
                                        }
                                        handler.sendEmptyMessage(0);
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObj.getString("MESSAGE"), Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
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
                        Utils.showCustomToast(message, getApplicationContext());
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
                System.out.println("Post:: " + etUserId.getText().toString().trim() + " :: " + etPassword.getText().toString().trim() + " M:: " + ConstantsUtils.TAG_MACID + " U:: " + ConstantsUtils.TAG_UUID + " IP:: " + etServerIP.getText().toString() + " W " + etPlant.getText().toString());
                String postData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<ns0:MT_RM_BC_UValidate_SND xmlns:ns0=\"http://RM_BC_UValidate\">\n" +
                        "   <UNAME>" + etUserId.getText().toString().trim() + "</UNAME>" +
                        "   <PSWD>" + etPassword.getText().toString().trim() + "</PSWD>" +
                        "   <ACCESSIP>" + etServerIP.getText().toString().trim() + "</ACCESSIP>" +
                        "   <DEVID>" + ConstantsUtils.TAG_UUID + "</DEVID>" +
                        "   <IMEI>" + ConstantsUtils.TAG_IMEI + "</IMEI>" +
                        "   <MOBILENO>" + ConstantsUtils.TAG_MACID + "</MOBILENO>" +
                        "   <VALIDATE>" + "" + "</VALIDATE>" +
                        "   <WERKS>" + etPlant.getText().toString().trim() + "</WERKS>" +
                        "</ns0:MT_RM_BC_UValidate_SND>\n\n";

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

