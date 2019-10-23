package com.fgtit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgtit.fingerprintapp.R;
import com.fgtit.http.PosApi;
import com.fgtit.http.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel.Xu on 2019-10-22.
 */
public class CanteenTotalInfoActivity extends Activity {
    PosApi posApi;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CanteenTotalAdapter canteenTotalAdapter;
    List<CanteenTotalBean.ResultBean.ResDataBean> data = new ArrayList<>();
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title_string)
    TextView tvTitleString;
    @BindView(R.id.yesterday)
    Button yesterday;
    @BindView(R.id.today)
    Button today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_info);
        ButterKnife.bind(this);
        posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        canteenTotalAdapter = new CanteenTotalAdapter(R.layout.item_canteen_total, data, this);
        recyclerView.setAdapter(canteenTotalAdapter);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString2 = formatter2.format(new Date());
        initData(dateString2);
    }

    private void initData(String dateString) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("now_date", dateString);
        Call<CanteenTotalBean> user_is_canteen_manager = posApi.get_user_is_canteen_manager_pos(map);
        user_is_canteen_manager.enqueue(new Callback<CanteenTotalBean>() {
            @Override
            public void onResponse(Call<CanteenTotalBean> call, Response<CanteenTotalBean> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getResult() != null) {
                    CanteenTotalBean.ResultBean result = response.body().getResult();
                    List<CanteenTotalBean.ResultBean.ResDataBean> res_data = result.getRes_data();
                    data.clear();
                    data.addAll(res_data);
                    canteenTotalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CanteenTotalBean> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.yesterday, R.id.today})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yesterday:
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.add(calendar.DATE,-1);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString= formatter.format(calendar.getTime());
                initData(dateString);
                break;
            case R.id.today:
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String dateString2 = formatter2.format(new Date());
                initData(dateString2);
                break;
        }
    }
}
