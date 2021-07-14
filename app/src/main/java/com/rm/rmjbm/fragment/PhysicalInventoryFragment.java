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

import androidx.fragment.app.Fragment;

import com.rm.rmjbm.R;
import com.rm.rmjbm.activity.LoginActivity;
import com.rm.rmjbm.adaptor.SpDocumentLovAdapter;
import com.rm.rmjbm.model.StockDataModel;
import com.rm.rmjbm.model.documentlov.DocumentLov;
import com.rm.rmjbm.model.scanPhyInventoryData.PhyInventoryData;
import com.rm.rmjbm.model.totalCount.TotalCount;
import com.rm.rmjbm.utils.RetrofitClient;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.Utils;

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
    private String strUserName, strIMEI, strDevId, strMacID, strPlant, strMaterialNo, strBatchNo, strTagNo, strQtys, strMatDoc, strDocumentNo;
    private SessionManagement session;
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
                // pd.dismiss();
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
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_RM_BC_PHY_INV_DOC_SND xmlns:ns0=\"http://RM_BC_PHY_INV_DOC\">\n" +
                "   <UNAME>" + strUserName.toUpperCase() + "</UNAME>" +
                "   <WERKS>" + strPlant + "</WERKS>" +
                "</ns0:MT_RM_BC_PHY_INV_DOC_SND>\n\n";

        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<DocumentLov> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDocumentLov(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<DocumentLov>() {
            @Override
            public void onResponse(Call<DocumentLov> call, Response<DocumentLov> response) {
                documentLov = new ArrayList<>();

                System.out.println("Null:: ");
                if (response.isSuccessful()) {
                    documentLov = response.body().getMtRmBcPhyInvDocRec().getPhyInvNos().getItem();
                    handler.sendEmptyMessage(0);
                } else {
                    System.out.println("Null::Else" + response.errorBody().toString());
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show(); // this will tell you why your api doesnt work most of time
                }
            }

            @Override
            public void onFailure(Call<DocumentLov> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
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
        }
    }

    private void getWidgetRef(View v) {
        llDataView = v.findViewById(R.id.llFPIDataView);
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
        switch (parent.getId()) {
            case R.id.spFPIDocumentList:
                strDocumentNo = spDocumentList.getSelectedItem().toString().trim();
                etBarcode.setText("");
                if (Utils.isOnline(getContext())) {
                    callTotalCounts();
                } else {
                    Utils.showNetworkAlert(getContext());
                }
                break;
        }
    }

    private void callTotalCounts() {
        pd.setTitle("Loading...");
        pd.show();
        System.out.println("print:: " + strDocumentNo + " :: " + strUserName.toUpperCase() + " :: " + strPlant);
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_RM_BC_PHY_INV_DET_SND xmlns:ns0=\"http://RM_BC_PHY_INV_DET\">\n" +
                "   <PHY_INV_NO>" + strDocumentNo + "</PHY_INV_NO>" +
                "   <UNAME>" + strUserName.toUpperCase() + "</UNAME>" +
                "   <WERKS>" + strPlant + "</WERKS>" +
                "</ns0:MT_RM_BC_PHY_INV_DET_SND>";
        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<TotalCount> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTotalCount(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<TotalCount>() {
            @Override
            public void onResponse(Call<TotalCount> call, Response<TotalCount> response) {
                llDataView.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    tvCountH.setText(getResources().getString(R.string.total_count));
                    tvCount.setText(Integer.toString(response.body().getMtRmBcPhyInvDetRec().getCount()));
                } else {
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show(); // this will tell you why your api doesnt work most of time
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<TotalCount> call, Throwable t) {
                pd.dismiss();
                llDataView.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });

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
                        if ((separated.length == 5)) {
                            if (isValidBarCode()) {
                                if (Utils.isOnline(getContext())) {
                                    callScanData();
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

    private boolean isValidBarCode() {
        if (etBarcode.getText().toString().length() != 0) {
            String[] separated = etBarcode.getText().toString().trim().split(Pattern.quote("/"));
            strMaterialNo = separated[0].trim();//MTNR
            strBatchNo = separated[1].trim(); //Batch
            strTagNo = separated[2].trim(); //TAGNo
            strQtys = separated[3].trim(); //QTY
            strMatDoc = separated[4].trim(); //MetDoc
            return true;
        } else {
            Utils.showCustomToast("Barcode should not be empty", getContext());
        }
        return false;
    }

    private void callScanData() {
        pd.setTitle("Loading...");
        pd.show();
        System.out.println("print::: " + etBarcode.getText().toString().trim() + " :: " + strUserName + " :: " + strDocumentNo);
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_RM_BC_PHY_INV_SCAN_SND xmlns:ns0=\"http://RM_BC_PHY_INV_SCAN\">\n" +
                "   <BARCODE>" + etBarcode.getText().toString().trim() + "</BARCODE>" +
                "   <PHY_INV_NO>" + strDocumentNo + "</PHY_INV_NO>" +
                "   <UNAME>" + strUserName.toUpperCase() + "</UNAME>" +
                "</ns0:MT_RM_BC_PHY_INV_SCAN_SND>";
        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<PhyInventoryData> call = RetrofitClient
                .getInstance()
                .getApi()
                .getScanData(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<PhyInventoryData>() {
            @Override
            public void onResponse(Call<PhyInventoryData> call, Response<PhyInventoryData> response) {
                if (response.isSuccessful()) {
                    llDataView.setVisibility(View.VISIBLE);
                    tvCountH.setText(getResources().getString(R.string.count));
                    System.out.println("print:: " + response.body().getMtRmBcPhyInvScanRec().getCount().toString());
                    tvCount.setText(response.body().getMtRmBcPhyInvScanRec().getCount().toString());
                    tvMaterialCode.setText(response.body().getMtRmBcPhyInvScanRec().getMatnr().replaceFirst("^0+(?!$)", ""));
                    tvShortText.setText(response.body().getMtRmBcPhyInvScanRec().getMaktx());
                    tvBatch.setText(response.body().getMtRmBcPhyInvScanRec().getBatch().replaceFirst("^0+(?!$)", ""));
                    tvQuantity.setText(response.body().getMtRmBcPhyInvScanRec().getQty().trim());
                    Toast.makeText(getContext(), response.body().getMtRmBcPhyInvScanRec().getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    etBarcode.setText("");
                } else {
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show(); // this will tell you why your api doesnt work most of time
                }
            }

            @Override
            public void onFailure(Call<PhyInventoryData> call, Throwable t) {
                llDataView.setVisibility(View.VISIBLE);
                pd.dismiss();
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });
    }

}