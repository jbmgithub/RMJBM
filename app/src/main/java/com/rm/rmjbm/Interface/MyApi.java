package com.rm.rmjbm.Interface;

import com.rm.rmjbm.model.documentlov.DocumentLov;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApi {

    //    @Headers({"Content-Type:application/xml;charset=UTF-8"})
    @POST("RM_BC_PHY_INV_DOC")
    Call<DocumentLov> getDocumentLov(@Body RequestBody body);
//    Call<DocumentLov> getDocumentLov(@Body RequestBody body, @Header("Authorization") String auth);



}
