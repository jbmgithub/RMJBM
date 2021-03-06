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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rm.rmjbm.R;
import com.rm.rmjbm.activity.LoginActivity;
import com.rm.rmjbm.adaptor.SpMatTransferPostingAdapter;
import com.rm.rmjbm.model.MTPSubmit.MTMTPSubmitRec;
import com.rm.rmjbm.model.movementLov.Item;
import com.rm.rmjbm.model.movementLov.MovementbaseLov;
import com.rm.rmjbm.model.movementLov.Mvtype;
import com.rm.rmjbm.model.mtpList.Getdata;
import com.rm.rmjbm.model.mtpList.MtpList;
import com.rm.rmjbm.utils.ConstantsUtils;
import com.rm.rmjbm.utils.RetrofitClient;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialTransferPostingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvNoScanListMsg, tvAuthMessage, tvMovementTypeH, tvFromLocationH, tvToLocationH, tvBarcodeH, tvMaterialH, tvMaterial, tvMaterialDescH, tvMaterialDesc, tvUomH, tvUom, tvBatchH, tvBatch, tvFromLocationViewH, tvFromLocationView, tvTotalScannedQtyH, tvTotalScannedQty, tvNoPalletScanH, tvNoPalletScan, tvStdPartQtyH, tvStdPartQty;
    private Spinner spMovementType, spFromLocation, spToLocation;
    private EditText etBarcode;
    private Button btnCancel, btnSubmit;
    private LinearLayout llDataView, llDetails;
    private ProgressDialog pd;
    private SessionManagement session;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private String strUserName, strIMEI, strDevId, strMacID, strPlant;
    private SpMatTransferPostingAdapter arrayAdapter;
    private MovementbaseLov movementLov;
    private MtpList mtpList;
    private List<String> movementList;
    private List<String> fromLocList;
    private List<String> toLocList;
    private Mvtype myType;
    private List<String> barcodeList;
    private Getdata myGetData;
    private float scannedQtyP = 0.0F;
    private int count = 0;
    private int totalPalletScanCount = 0;
    private String strMaterial = "";
    private String strBatch = "";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                pd.dismiss();
                llDetails.setVisibility(View.VISIBLE);
                tvAuthMessage.setVisibility(View.GONE);
                if (myType != null) {
                    System.out.println("size:::: " + myType.getItem().size());
                    for (Item item : myType.getItem()) {
                        movementList.add(String.valueOf(item.getBwart()));
                    }
                    movementList = removeDuplicateItem(movementList);
                    setSpinnerAdapter(spMovementType, movementList);
                }

            } else if (msg.what == 1) {
                pd.dismiss();
                llDetails.setVisibility(View.GONE);
                tvAuthMessage.setVisibility(View.VISIBLE);
                tvAuthMessage.setText(movementLov.getMtMtpDdlistRec().getMessage());
            } else if (msg.what == 2) {
                pd.dismiss();
                llDataView.setVisibility(View.VISIBLE);
                tvNoScanListMsg.setVisibility(View.GONE);
                System.out.println("str::: " + strMaterial);
                if (strMaterial.equalsIgnoreCase("")) {
                    strMaterial = Utils.removerZeroFromPrefix(myGetData.getMatnr());
                }
                if (strBatch.equalsIgnoreCase("")) {
                    strBatch = Utils.removerZeroFromPrefix(myGetData.getCharg());
                }

                tvMaterial.setText(Utils.removerZeroFromPrefix(myGetData.getMatnr()));
                tvMaterialDesc.setText(myGetData.getMaktx());
                tvUom.setText(myGetData.getMeins());
                tvBatch.setText(Utils.removerZeroFromPrefix(myGetData.getCharg()));
                tvFromLocationView.setText(spFromLocation.getSelectedItem().toString());

                barcodeList.add(etBarcode.getText().toString());
//                barcodeList = removeDuplicateItemFromList(barcodeList);
//                totalPalletScanCount = 0;
                System.out.println("totalPalletScanCount::S " + barcodeList.size());
                count = 0;
                for (int i = 0; i < barcodeList.size(); i++)
                    if (etBarcode.getText().toString().equalsIgnoreCase(barcodeList.get(i))) {
                        count++;
                    }
                if (count == 1)
                    scannedQtyP = scannedQtyP + Float.parseFloat(myGetData.getScanq());
                else
                    Utils.showMyAlert(getContext(), "Error", "Sticker already scanned...");
                tvTotalScannedQty.setText(String.valueOf(scannedQtyP));
                totalPalletScanCount = removeDuplicateItemFromList(barcodeList).size();
                tvNoPalletScan.setText(String.valueOf(totalPalletScanCount));
                tvStdPartQty.setText(myGetData.getLabst().toString());
                etBarcode.setText("");
            } else if (msg.what == 3) {
                pd.dismiss();
                etBarcode.setText("");
                llDataView.setVisibility(View.GONE);
                tvNoScanListMsg.setVisibility(View.VISIBLE);
                tvNoScanListMsg.setText(mtpList.getMTMTPGetlistRec().getMessage());
            }
            return false;
        }
    });

    private List<String> removeDuplicateItemFromList(List<String> list) {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(barcodeList);
        barcodeList.clear();
        barcodeList.addAll(hashSet);
        Collections.sort(barcodeList);
        return list;

    }

    private List<String> removeDuplicateItem(List<String> list) {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        Collections.sort(list);
        list.add(0, ConstantsUtils.PLEASE_SELECT);
        return list;
    }


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
        if (Utils.isOnline(getContext())) {
            callMovementLov();
        } else {
            Utils.showNetworkAlert(getContext());
        }
        return v;
    }

    private void callMovementLov() {

        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_MTP_DDLIST_SND xmlns:ns0=\"http://Material_Transfer_Posting_DDList\">\n" +
                "   <plant>" + strPlant + "</plant>" +
                "   <userid>" + strUserName.toUpperCase() + "</userid>" +
                "</ns0:MT_MTP_DDLIST_SND>\n\n";

        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<MovementbaseLov> call = RetrofitClient
                .getInstance()
                .getApi()
                .getMovementLov(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<MovementbaseLov>() {

            @Override
            public void onResponse(Call<MovementbaseLov> call, Response<MovementbaseLov> response) {
                if (!response.isSuccessful()) {
                    llDetails.setVisibility(View.GONE);
                    tvAuthMessage.setVisibility(View.VISIBLE);
                    tvAuthMessage.setText(response.errorBody().toString());
                    pd.dismiss();
                    return;
                }
                movementLov = response.body();
//                Gson gson = new GsonBuilder().create();

                System.out.println("msg:: " + response.body().getMtMtpDdlistRec().getSuccess());
                if (movementLov.getMtMtpDdlistRec().getSuccess().equalsIgnoreCase("S")) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(String.valueOf(movementLov.getMtMtpDdlistRec().getMvtype()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (json.has("item")) {
                        JSONObject dataObject = json.optJSONObject("item");
                        if (dataObject != null) {
                            //Do things with object.
                            myType = new Mvtype();
                            List<Item> listItem = new ArrayList<>();
                            Item item = new Item();
                            try {
                                item.setLgortT(dataObject.get("lgort_t").toString());
                                item.setLgortF(dataObject.get("lgort_f").toString());
                                item.setWerks(dataObject.getInt("werks"));
                                item.setBwart(dataObject.getInt("bwart"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listItem.add(item);
                            myType.setItem(listItem);
                        } else {
                            //Do things with array
                            JSONArray jsonArray = json.optJSONArray("item");
                            myType = new Mvtype();
                            List<Item> listItem = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Item item = new Item();
                                try {
                                    item.setLgortT(jsonArray.getJSONObject(i).getString("lgort_t"));
                                    item.setLgortF(jsonArray.getJSONObject(i).getString("lgort_f"));
                                    item.setWerks(jsonArray.getJSONObject(i).getInt("werks"));
                                    item.setBwart(jsonArray.getJSONObject(i).getInt("bwart"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                listItem.add(item);
                            }
                            myType.setItem(listItem);

/*                            TypeToken<Mvtype> responseTypeToken = new TypeToken<Mvtype>() {
                            };
                            myType = gson.fromJson(gson.toJson(movementLov.getMtMtpDdlistRec().getMvtype()), responseTypeToken.getType());*/
                        }
                    }
                    handler.sendEmptyMessage(0);

                } else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<MovementbaseLov> call, Throwable t) {
                llDetails.setVisibility(View.GONE);
                tvAuthMessage.setVisibility(View.VISIBLE);
                tvAuthMessage.setText(t.toString());
                pd.dismiss();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });

    }

    private void init() {
        initSession();
        setFontStyle();
        setTextFont();
        barcodeList = new ArrayList<>();
        movementList = new ArrayList<>();
        fromLocList = new ArrayList<>();
        toLocList = new ArrayList<>();
        setSpinnerAdapter(spMovementType, movementList);
        setSpinnerAdapter(spFromLocation, fromLocList);
        setSpinnerAdapter(spToLocation, toLocList);

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

        etBarcode.setFocusable(true);
//        etBarcode.setClickable(false);
        etBarcode.setLongClickable(false);
        etBarcode.setTextIsSelectable(false);
        etBarcode.setInputType(InputType.TYPE_NULL);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            etBarcode.setRawInputType(InputType.TYPE_CLASS_TEXT);
            etBarcode.setTextIsSelectable(true);
        }
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> list) {
        arrayAdapter = new SpMatTransferPostingAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void setTextFont() {
        tvNoScanListMsg.setTypeface(robotoRegular);
        tvAuthMessage.setTypeface(robotoRegular);
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
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void getWidgetRef(View v) {

        llDetails = v.findViewById(R.id.llFMTPDetails);
        llDataView = v.findViewById(R.id.llFMTPDataView);
        tvNoScanListMsg = v.findViewById(R.id.tvFMTPMsgNoScanList);

        tvAuthMessage = v.findViewById(R.id.tvFMTPMessage);
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
        btnCancel = v.findViewById(R.id.btnFMTPCancel);
        btnSubmit = v.findViewById(R.id.btnFMTPSubmit);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spFMTPMovemontType:
                fromLocList.clear();
                for (Item item : myType.getItem())
                    if (spMovementType.getSelectedItem().toString().equalsIgnoreCase(item.getBwart().toString()))
                        fromLocList.add(item.getLgortF());
                fromLocList = removeDuplicateItem(fromLocList);
                setSpinnerAdapter(spFromLocation, fromLocList);
                clearDataView();
                barcodeList.clear();
                scannedQtyP = 0;
                count = 0;
                totalPalletScanCount = 0;
                strMaterial = "";
                strBatch = "";
                break;
            case R.id.spFMTPFromLocation:
                toLocList.clear();
                for (Item item : myType.getItem())
                    if (spMovementType.getSelectedItem().toString().equalsIgnoreCase(item.getBwart().toString())
                            && spFromLocation.getSelectedItem().toString().equalsIgnoreCase(item.getLgortF().toString()))
                        toLocList.add(item.getLgortT());
                toLocList = removeDuplicateItem(toLocList);
                setSpinnerAdapter(spToLocation, toLocList);
                clearDataView();
                barcodeList.clear();
                scannedQtyP = 0;
                count = 0;
                totalPalletScanCount = 0;
                strMaterial = "";
                strBatch = "";

                break;
            case R.id.spFMTPToLocation:
                clearDataView();
                barcodeList.clear();
                scannedQtyP = 0;
                count = 0;
                totalPalletScanCount = 0;
                strMaterial = "";
                strBatch = "";

//                clearAll();
                break;

        }
    }

    private void clearDataView() {
        tvMaterial.setText("");
        tvMaterialDesc.setText("");
        tvUom.setText("");
        tvBatch.setText("");
        tvFromLocationView.setText("");
        tvTotalScannedQty.setText("");
        tvNoPalletScan.setText("");
        tvStdPartQty.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            clearAll();
//            setSpinnerAdapter(spMovementType, movementList);
//            clearDataView();
//            barcodeList.clear();
        }
        if (v == btnSubmit) {
            barcodeList = removeDuplicateItemFromList(barcodeList);
            if ((barcodeList.size() > 0) || !(barcodeList.isEmpty())) {
                if (Utils.isOnline(getContext())) {
                    callSubmitData();
                } else {
                    Utils.showNetworkAlert(getContext());
                }
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.please_scan_barcode), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void callSubmitData() {
        pd.setTitle("Loading...");
        pd.show();
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_MTP_Submit_snd xmlns:ns0=\"http://MTP_Submit\">\n" +
                "<scan_barc>\n";

        for (String barCode : barcodeList) {
            String itemConcat = "<item>\n" +
                    "  <barcode>" + barCode + "</barcode>\n" +
                    "</item>\n";
            requestBodyText = requestBodyText + itemConcat;
        }

        String finalConct = "</scan_barc>\n" +
                "<bwart>" + spMovementType.getSelectedItem().toString() + "</bwart>\n" +
                "<lgort_f>" + spFromLocation.getSelectedItem().toString() + "</lgort_f>\n" +
                "<lgort_t>" + spToLocation.getSelectedItem().toString() + "</lgort_t>\n" +
                "<matnr>" + tvMaterial.getText().toString().trim() + "</matnr>\n" +
                "<scanq>" + tvTotalScannedQty.getText().toString().trim() + "</scanq>\n" +
                "<uname>" + strUserName.toUpperCase() + "</uname>\n" +
                "<werks>" + strPlant + "</werks>\n" +
                "</ns0:MT_MTP_Submit_snd>";

        requestBodyText = requestBodyText + finalConct;
        System.out.println("List::: " + requestBodyText);

        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));
        Call<MTMTPSubmitRec> call = RetrofitClient
                .getInstance()
                .getApi()
                .getMTPSubmit(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<MTMTPSubmitRec>() {
            @Override
            public void onResponse(Call<MTMTPSubmitRec> call, Response<MTMTPSubmitRec> response) {
                if (!response.isSuccessful()) {
                    pd.dismiss();
                    return;
                }
                pd.dismiss();

                if (response.body().getMTMTPSubmitRec().getSuccess().equalsIgnoreCase("E")) {
                    Utils.showMyAlert(getContext(), "Error", response.body().getMTMTPSubmitRec().getMessage());
                } else {
                    Utils.showMyAlert(getContext(), "", response.body().getMTMTPSubmitRec().getMessage());
                }
//                Toast.makeText(getContext(), response.body().getMTMTPSubmitRec().getMessage(), Toast.LENGTH_LONG).show();
                clearAll();
            }

            @Override
            public void onFailure(Call<MTMTPSubmitRec> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });
    }

    private void clearAll() {
        clearDataView();

        setSpinnerAdapter(spMovementType, movementList);
        barcodeList.clear();
        scannedQtyP = 0;
        count = 0;
        totalPalletScanCount = 0;
        strMaterial = "";
        strBatch = "";

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
//            String text = "3006438/0000013245/1/50/5000037639";
            switch (view.getId()) {
                case R.id.etFMTPBarcode:
                    if (text.isEmpty() || text.length() == 0 || text.equals(null)) {
                    } else {
                        if (isValidBarCode()) {
                            if (isValidMovementType()) {
                                if (isValidFromLocation()) {
                                    if (isValidToLocation()) {
                                        if (Utils.isOnline(getContext())) {
                                            if (strMaterial.isEmpty() && strBatch.isEmpty()) {
                                                callScanData();
                                            } else {
                                                String[] separated = text.split(Pattern.quote("/"));
                                                String strNewMaterial = separated[0];
                                                String strNewBatch = separated[1];
                                                if (strNewBatch.equalsIgnoreCase(strBatch) && strNewMaterial.equalsIgnoreCase(strMaterial)) {
                                                    callScanData();
                                                } else if (strNewMaterial.equalsIgnoreCase(strMaterial) && !strNewBatch.equalsIgnoreCase(strBatch)) {
                                                    etBarcode.setText("");
                                                    Utils.showMyAlert(getContext(), "Error", "Different Batch:" + strNewBatch + " is not allow...");
//                                                    Toast.makeText(getContext(), "Different Batch:" + strNewBatch + " is not allow...", Toast.LENGTH_LONG).show();
                                                } else {
                                                    etBarcode.setText("");
                                                    Utils.showMyAlert(getContext(), "Error", "Different Material:" + strNewMaterial + " & Batch:" + strNewBatch + " are not allow...");
//                                                    Toast.makeText(getContext(), "Different Material:" + strNewMaterial + " & Batch:" + strNewBatch + " are not allow...", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Utils.showNetworkAlert(getContext());
                                            etBarcode.setText("");
                                        }
                                    } else {
                                        etBarcode.setText("");
                                    }
                                } else {
                                    etBarcode.setText("");
                                }
                            } else {
                                etBarcode.setText("");
                            }
                        } else {
                            etBarcode.setText("");
                        }
                    }
                    break;
            }
        }
    }

    private void callScanData() {
        pd.setTitle("Loading...");
        pd.show();
//        System.out.println("print::: " + etBarcode.getText().toString().trim() + " :: " + strUserName + " :: " + strDocumentNo);
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns0:MT_MTP_Getlist_snd xmlns:ns0=\"http://MTP_Getlist\">\n" +
                "   <plant>" + strPlant + "</plant>" +
                "   <movtyp>" + spMovementType.getSelectedItem().toString() + "</movtyp>" +
                "   <slocf>" + spFromLocation.getSelectedItem().toString() + "</slocf>" +
                "   <sloct>" + spToLocation.getSelectedItem().toString() + "</sloct>" +
                "   <barc>" + etBarcode.getText().toString().trim() + "</barc>" +
                "</ns0:MT_MTP_Getlist_snd>";
        RequestBody requestBody = RequestBody.create(requestBodyText, MediaType.parse("text/xml"));

        Call<MtpList> call = RetrofitClient
                .getInstance()
                .getApi()
                .getMTPScanData(requestBody, Utils.getAuthToken());

        call.enqueue(new Callback<MtpList>() {
            @Override
            public void onResponse(Call<MtpList> call, Response<MtpList> response) {
                if (!response.isSuccessful()) {
                    llDataView.setVisibility(View.GONE);
                    tvNoScanListMsg.setVisibility(View.VISIBLE);
                    tvNoScanListMsg.setText(response.errorBody().toString());
                    pd.dismiss();
                    etBarcode.setText("");
                    return;
                }

                mtpList = response.body();
                Gson gson = new GsonBuilder().create();

                System.out.println("msg:: " + mtpList.getMTMTPGetlistRec().getSuccess());
                if (mtpList.getMTMTPGetlistRec().getSuccess().equalsIgnoreCase("S")) {
                    TypeToken<Getdata> responseTypeToken = new TypeToken<Getdata>() {
                    };
                    myGetData = gson.fromJson(gson.toJson(mtpList.getMTMTPGetlistRec().getGetdata()), responseTypeToken.getType());
                    handler.sendEmptyMessage(2);
                } else handler.sendEmptyMessage(3);
            }

            @Override
            public void onFailure(Call<MtpList> call, Throwable t) {
                llDataView.setVisibility(View.GONE);
                tvNoScanListMsg.setVisibility(View.VISIBLE);
                tvNoScanListMsg.setText(t.toString());
                etBarcode.setText("");
                pd.dismiss();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });
    }

    private boolean isValidMovementType() {
        if (!spMovementType.getSelectedItem().toString().equalsIgnoreCase(ConstantsUtils.PLEASE_SELECT))
            return true;
        else
            Utils.showCustomToast("Please select MOVEMENT TYPE", getContext());
        return false;
    }

    private boolean isValidFromLocation() {
        if (!spFromLocation.getSelectedItem().toString().equalsIgnoreCase(ConstantsUtils.PLEASE_SELECT))
            return true;
        else
            Utils.showCustomToast("Please select FROM LOCATION", getContext());
        return false;
    }

    private boolean isValidToLocation() {
        if (!spToLocation.getSelectedItem().toString().equalsIgnoreCase(ConstantsUtils.PLEASE_SELECT))
            return true;
        else
            Utils.showCustomToast("Please select TO LOCATION", getContext());
        return false;
    }
}