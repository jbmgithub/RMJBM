package com.rm.rmjbm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Button;
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
import com.rm.rmjbm.adaptor.SpMatTransferPostingAdapter;
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MaterialTransferPostingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvMovementTypeH, tvFromLocationH, tvToLocationH, tvBarcodeH, tvMaterialH, tvMaterial, tvMaterialDescH, tvMaterialDesc, tvUomH, tvUom, tvBatchH, tvBatch, tvFromLocationViewH, tvFromLocationView, tvTotalScannedQtyH, tvTotalScannedQty, tvNoPalletScanH, tvNoPalletScan, tvStdPartQtyH, tvStdPartQty;
    private Spinner spMovementType, spFromLocation, spToLocation;
    private EditText etBarcode;
    private Button btnClear, btnSubmit;
    private LinearLayout llDataView;
    private ProgressDialog pd;
    private SessionManagement session;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private String strUserName, strIMEI, strDevId, strMacID, strPlant;
    private SpMatTransferPostingAdapter arrayAdapter;

    public MaterialTransferPostingFragment() {
        // Required empty public constructor
    }

    public static MaterialTransferPostingFragment newInstance(String param1, String param2) {
        MaterialTransferPostingFragment fragment = new MaterialTransferPostingFragment();
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

        View v = inflater.inflate(R.layout.fragment_material_transfer_posting, container, false);
        getWidgetRef(v);
        setWidgetEvent();
        init();
//        if (Utils.isOnline(getContext())) {
//            callDocumentLov();
//        } else {
//            Utils.showNetworkAlert(getContext());
//        }
        return v;
    }

    private void init() {
        initSession();
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
//        setSpinnerAdapter(spMovementType, documentLov);
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> list) {
        arrayAdapter = new SpMatTransferPostingAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void setTextFont() {
        tvMovementTypeH.setTypeface(robotoBold);
        tvFromLocationH.setTypeface(robotoBold);
        tvToLocationH.setTypeface(robotoBold);
        tvBarcodeH.setTypeface(robotoBold);
        tvMaterialH.setTypeface(robotoBold);
        tvMaterial.setTypeface(robotoRegular);
        tvMaterialDescH.setTypeface(robotoBold);
        tvMaterialDesc.setTypeface(robotoRegular);
        tvUomH.setTypeface(robotoBold);
        tvUom.setTypeface(robotoRegular);
        tvBatchH.setTypeface(robotoBold);
        tvBatch.setTypeface(robotoRegular);
        tvFromLocationViewH.setTypeface(robotoBold);
        tvFromLocationView.setTypeface(robotoRegular);
        tvTotalScannedQtyH.setTypeface(robotoBold);
        tvTotalScannedQty.setTypeface(robotoRegular);
        tvNoPalletScanH.setTypeface(robotoBold);
        tvNoPalletScan.setTypeface(robotoRegular);
        tvStdPartQtyH.setTypeface(robotoBold);
        tvStdPartQty.setTypeface(robotoRegular);
    }

    private void setFontStyle() {
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Italic.ttf");
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

    private void setWidgetEvent() {
        spMovementType.setOnItemSelectedListener(this);
        spFromLocation.setOnItemSelectedListener(this);
        spToLocation.setOnItemSelectedListener(this);
        etBarcode.addTextChangedListener(new GenericTextWatcher(etBarcode));
        btnClear.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void getWidgetRef(View v) {
        llDataView = v.findViewById(R.id.llFMTPDataView);
        tvMovementTypeH = v.findViewById(R.id.tvFMTPMovemontTypeH);
        spMovementType = v.findViewById(R.id.spFMTPMovemontType);
        tvFromLocationH = v.findViewById(R.id.tvFMTPFromLocationH);
        spFromLocation = v.findViewById(R.id.spFMTPFromLocation);
        tvToLocationH = v.findViewById(R.id.tvFMTPToLocationH);
        spToLocation = v.findViewById(R.id.spFMTPToLocation);
        tvBarcodeH = v.findViewById(R.id.tvFMTPBarcodeH);
        etBarcode = v.findViewById(R.id.etFMTPBarcode);
        tvMaterialH = v.findViewById(R.id.tvFMTPMaterialH);
        tvMaterial = v.findViewById(R.id.tvFMTPMaterial);
        tvMaterialDescH = v.findViewById(R.id.tvFMTPMaterialDescH);
        tvMaterialDesc = v.findViewById(R.id.tvFMTPMaterialDesc);
        tvUomH = v.findViewById(R.id.tvFMTPUomH);
        tvUom = v.findViewById(R.id.tvFMTPUom);
        tvBatchH = v.findViewById(R.id.tvFMTPBatchH);
        tvBatch = v.findViewById(R.id.tvFMTPBatch);
        tvFromLocationViewH = v.findViewById(R.id.tvFMTPFromLocationViewH);
        tvFromLocationView = v.findViewById(R.id.tvFMTPFromLocation);
        tvTotalScannedQtyH = v.findViewById(R.id.tvFMTPTotalScannedQtyH);
        tvTotalScannedQty = v.findViewById(R.id.tvFMTPTotalScannedQty);
        tvNoPalletScanH = v.findViewById(R.id.tvFMTPNoPalletScanH);
        tvNoPalletScan = v.findViewById(R.id.tvFMTPNoPalletScan);
        tvStdPartQtyH = v.findViewById(R.id.tvFMTPStdPartQtyH);
        tvStdPartQty = v.findViewById(R.id.tvFMTPStdPartQty);
        btnClear = v.findViewById(R.id.btnFMTPClear);
        btnSubmit = v.findViewById(R.id.btnFMTPSubmit);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }

    private boolean isValidBarCode() {
        if (etBarcode.getText().toString().length() != 0)
            return true;
        else
            Utils.showCustomToast("Barcode should not be empty", getContext());

        return false;
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
            switch (view.getId()) {
                case R.id.etFMTPBarcode:
                    if (text.isEmpty() || text.length() == 0 || text.equals(null)) {
                    } else {
                        if (isValidBarCode()) {
                            if (Utils.isOnline(getContext())) {
//                                callScanData();
                            } else {
                                Utils.showNetworkAlert(getContext());
                                etBarcode.setText("");
                            }
                        }
                    }
                    break;
            }
        }
    }
}