package com.rm.rmjbm.utils;


import androidx.appcompat.app.AppCompatActivity;

import com.rm.rmjbm.Interface.MyApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.*;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static RetrofitClient retrofitClient;

    OkHttpClient httpClient = new OkHttpClient.Builder()
            //here we can add Interceptor for dynamical adding headers
            .addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("test", "test").build();
                    return chain.proceed(request);
                }
            })
            //here we adding Interceptor for full level logging
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY))
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(httpClient)
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
