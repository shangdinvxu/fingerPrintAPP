package com.fgtit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.fgtit.fingerprintapp.R;
import com.fgtit.utils.SysConstant;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;

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
        UpdateKey.API_TOKEN = SysConstant.APITOKEN;
        UpdateKey.APP_ID = SysConstant.APPID;
        //下载方式:
        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);
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
