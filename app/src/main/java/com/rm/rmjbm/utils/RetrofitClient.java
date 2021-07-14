package com.rm.rmjbm.utils;


import com.rm.rmjbm.Interface.MyApi;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static RetrofitClient retrofitClient;

//    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
//            Request originalRequest = chain.request();
//
//            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
//                    Credentials.basic(URLs.TAG_USER, URLs.TAG_PASSWORD));
//
//            Request newRequest = builder.build();
//            return chain.proceed(newRequest);
//        }
//    }).build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
                .baseUrl(URLs.URL_BASEURL_R)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null)
            retrofitClient = new RetrofitClient();
        return retrofitClient;
    }

    public MyApi getApi() {
        return retrofit.create(MyApi.class);
    }

}
