package com.rm.rmjbm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rm.rmjbm.R;
import com.rm.rmjbm.activity.LoginActivity;
import com.rm.rmjbm.adaptor.SpDocumentLovAdapter;
import com.rm.rmjbm.model.documentlov.DocumentLov;
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.model.StockDataModel;
import com.rm.rmjbm.utils.RetrofitClient;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhysicalInventoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvDocumentListH, tvBarcodeH, tvCountH, tvCount, tvMaterialCodeH, tvShortTextH, tvBatchH, tvQuantityH, tvMaterialCode, tvShortText, tvBatch, tvQuantity;
    private EditText etBarcode;
    private LinearLayout llDataView;
    private Spinner spDocumentList;
    private String strUserName, strIMEI, strDevId, strMacID, strPlant, strPhyInvNo, strBarcode, strUname, strQtys, strMatDoc;
    private SessionManagement session;
    private ArrayList<LovModel> lovList;
    private SpDocumentLovAdapter arrayAdapter;
    private ProgressDialog pd;
    private List<StockDataModel> displayList;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private List<String> documentLov;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!documentLov.isEmpty())
                    initSpinnerAdapter();
                pd.dismiss();
            } else if (msg.what == 1) {
            } else if (msg.what == 2) {
            }
            return false;
        }
    });

    public PhysicalInventoryFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static PhysicalInventoryFragment newInstance(String param1, String param2) {
        PhysicalInventoryFragment fragment = new PhysicalInventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_physical_inventory, container, false);
        getWidgetRef(v);
        setWidgetEvent();
        init();
        if (Utils.isOnline(getContext())) {
            callDocumentLov();
        } else {
            Utils.showNetworkAlert(getContext());
        }
        return v;
    }

    private void callDocumentLov() {
        pd.setTitle("Loading...");
        pd.show();

        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_RM_BC_PHY_INV_DOC_SND xmlns:ns0=\"http://RM_BC_PHY_INV_DOC\">\n" +
                "   <UNAME>" + "TEST2" + "</UNAME>" +
                "   <WERKS>" + "4551" + "</WERKS>" +
                "</ns0:MT_RM_BC_PHY_INV_DOC_SND>\n\n";
        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<DocumentLov> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDocumentLov(requestBody);

        call.enqueue(new Callback<DocumentLov>() {
            @Override
            public void onResponse(Call<DocumentLov> call, Response<DocumentLov> response) {
                documentLov = new ArrayList<>();
                documentLov = response.body().getMtRmBcPhyInvDocRec().getPhyInvNos().getItem();
                System.out.println("body:: " + documentLov.size());
                handler.sendEmptyMessage(0);


            }

            @Override
            public void onFailure(Call<DocumentLov> call, Throwable t) {

            }
        });


    }

    private void setWidgetEvent() {
        spDocumentList.setOnItemSelectedListener(this);
        etBarcode.addTextChangedListener(new GenericTextWatcher(etBarcode));

    }

    private void init() {
        initSession();
        documentLov = new ArrayList<>();
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

    private void getWidgetRef(View v) {
        tvDocumentListH = v.findViewById(R.id.tvFPIDocumentListH);
        tvBarcodeH = v.findViewById(R.id.tvFPIBarcodeH);
        etBarcode = v.findViewById(R.id.etFPIBarcode);
        spDocumentList = v.findViewById(R.id.spFPIDocumentList);

        tvCountH = v.findViewById(R.id.tvFPICountH);
        tvCount = v.findViewById(R.id.tvFPICount);
        tvMaterialCodeH = v.findViewById(R.id.tvFPIMaterialCodeH);
        tvMaterialCode = v.findViewById(R.id.tvFPIMaterialCode);
        tvShortTextH = v.findViewById(R.id.tvFPIShortTextH);
        tvShortText = v.findViewById(R.id.tvFPIShortText);
        tvBatchH = v.findViewById(R.id.tvFPIBatchH);
        tvBatch = v.findViewById(R.id.tvFPIBatch);
        tvQuantityH = v.findViewById(R.id.tvFPIQuantityH);
        tvQuantity = v.findViewById(R.id.tvFPIQuantity);
        llDataView = v.findViewById(R.id.llFPIDataView);
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setTextFont() {
        tvDocumentListH.setTypeface(robotoBold);
        tvBarcodeH.setTypeface(robotoBold);
        tvCountH.setTypeface(robotoBold);
        tvCount.setTypeface(robotoBold);
        tvMaterialCodeH.setTypeface(robotoBold);
        tvMaterialCode.setTypeface(robotoRegular);
        tvShortTextH.setTypeface(robotoBold);
        tvShortText.setTypeface(robotoRegular);
        tvBatchH.setTypeface(robotoBold);
        tvBatch.setTypeface(robotoRegular);
        tvQuantityH.setTypeface(robotoBold);
        tvQuantity.setTypeface(robotoRegular);
    }

    private void initSpinnerAdapter() {
        setSpinnerAdapter(spDocumentList, documentLov);
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> list) {
        arrayAdapter = new SpDocumentLovAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                case R.id.etFPIBarcode:
                    if (text.isEmpty() || text.length() == 0 || text.equals(null)) {
                    } else {
                        String[] separated = text.split(Pattern.quote("/"));
                        System.out.println("LeL:: " + separated.length);
                        if ((separated.length == 3)) {
                            if (isValidBarCode()) {
                                if (Utils.isOnline(getContext())) {
//                                    callDisplayList();
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
                strPhyInvNo = separated[0].trim();
                strBarcode = separated[1].trim();
                strUname = separated[2].trim();
                return true;
            } else {
                Utils.showCustomToast("Barcode should not be empty", getContext());
            }
            return false;
        }
    }

}