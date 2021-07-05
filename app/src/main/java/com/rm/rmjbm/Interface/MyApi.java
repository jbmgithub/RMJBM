package com.rm.rmjbm.Interface;

import com.rm.rmjbm.model.scanPhyInventoryData.PhyInventoryData;
import com.rm.rmjbm.model.documentlov.DocumentLov;
import com.rm.rmjbm.utils.URLs;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApi {

    //    @Headers({"Content-Type:application/xml;charset=UTF-8"})
    @POST(URLs.URL_PHY_INV_DOC_LOV)
    Call<DocumentLov> getDocumentLov(@Body RequestBody body);
//    Call<DocumentLov> getDocumentLov(@Body RequestBody body, @Header("Authorization") String auth);

    @POST(URLs.URL_PHY_INV_DISPLAY_LIST)
    Call<PhyInventoryData> getScanData(@Body RequestBody body);


}
