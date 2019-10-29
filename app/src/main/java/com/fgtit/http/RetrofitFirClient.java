package com.fgtit.http;

import android.content.Context;

import com.fgtit.utils.SysConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFirClient {
    private  static Retrofit retrofit;

    private RetrofitFirClient(Context context) {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(new OKHttpFactory(context).getOkHttpClient())

//                .baseUrl("http://192.168.2.111:8069/linkloving_app_api/")
                .baseUrl("http://api.fir.im/apps/latest/")
                //gson转化器
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit getInstance(Context context) {
        new RetrofitFirClient(context);
        return retrofit;
    }
}
