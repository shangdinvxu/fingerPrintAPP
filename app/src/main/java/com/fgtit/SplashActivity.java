package com.fgtit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.fgtit.fingerprintapp.R;
import com.fgtit.http.FirBean;
import com.fgtit.http.PosApi;
import com.fgtit.http.RetrofitClient;
import com.fgtit.http.RetrofitFirClient;
import com.fgtit.utils.SysConstant;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean anInt = SPUtils.getInstance().getBoolean(SysConstant.IS_FIRST, true);
//        Intent intent = new Intent(this, DownloadActivity.class);
//        startActivity(intent);
        if (anInt){
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        initAutoUpdateApp();
    }

    private void initAutoUpdateApp() {
//        UpdateKey.API_TOKEN = SysConstant.APITOKEN;
//        UpdateKey.APP_ID = SysConstant.APPID;
//        //下载方式:
////        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;
//        UpdateFunGO.init(this);
        PosApi posApi = RetrofitFirClient.getInstance(this).create(PosApi.class);
        Call<FirBean> firVersion = posApi.getFirVersion(SysConstant.APITOKEN);
        firVersion.enqueue(new Callback<FirBean>() {
            @Override
            public void onResponse(Call<FirBean> call, final Response<FirBean> response) {
                String version = response.body().getVersion();
                Integer integer = Integer.valueOf(version);
                if (getAppVersionCode(SplashActivity.this)<integer){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("下载更新")
                            .setMessage(response.body().getChangelog())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do  delete thing
                                    Intent intent=new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri CONTENT_URI_BROWSERS = Uri.parse(response.body().getUpdate_url());
                                    intent.setData(CONTENT_URI_BROWSERS);
                                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<FirBean> call, Throwable t) {

            }
        });
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }
}
