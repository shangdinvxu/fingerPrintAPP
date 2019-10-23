package com.fgtit.http;

import android.content.Context;

import com.fgtit.utils.SysConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitLoginClient {
    private  static Retrofit retrofit;

    private RetrofitLoginClient(Context context) {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(new OKHttpFactory(context).getOkHttpClient())

//                .baseUrl("http://192.168.2.111:8069/linkloving_app_api/")
                .baseUrl(SysConstant.LINKLOVING_URL+"linkloving_app_api/")
                //gson转化器
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit getInstance(Context context) {
        new RetrofitLoginClient(context);
        return retrofit;
    }
}
