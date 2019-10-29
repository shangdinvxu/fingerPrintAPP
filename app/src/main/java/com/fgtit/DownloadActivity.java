package com.fgtit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fgtit.app.ActivityList;
import com.fgtit.app.UserItem;
import com.fgtit.app.UsersList;
import com.fgtit.fingerprintapp.R;
import com.fgtit.http.PosApi;
import com.fgtit.http.RetrofitClient;
import com.fgtit.multilevelTreeList.Node;
import com.fgtit.utils.SysConstant;
import com.fgtit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fgtit.EnrollActivity.hexString2Bytes;

public class DownloadActivity extends AppCompatActivity {

    @BindView(R.id.diy_loading)
    ZYDownloading diyLoading;

    private int progress=0;
    private boolean downOk = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        ActivityList.getInstance().setMainContext(this);
        ActivityList.getInstance().CreateDir();//创建该有的文件夹
        UsersList.getInstance().LoadAll();

        ActivityList.getInstance().addActivity(this);

        diyLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!diyLoading.isDownloading()) {
                    ToastUtils.showLong("开始请求");
                    initData();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    diyLoading.setProgress(progress);
                    if (progress < 50) {
                        progress += 1;
                        Log.e("process","process = "+progress);
                        Message message = new Message();
                        message.what=1;
                        handler.sendMessageDelayed(message, 50);
                    }else {
                        if (downOk){
                            Message message = new Message();
                            message.what=2;
                            handler.sendMessageDelayed(message, 20);
                        }
                    }
                    break;
                case 2:
                    diyLoading.setProgress(progress);
                    if (progress < 100) {
                        progress += 1;
                        Log.e("process2","process = "+progress);
                        Message message = new Message();
                        message.what=2;
                        handler.sendMessageDelayed(message, 50);
                    }else {
                        toMain();
                    }
                    break;
            }
//            diyLoading.setProgress(progress);
//            if (progress < 100) {
//                progress += 1;
//                handler.sendMessageDelayed(Message.obtain(), 100);
//            }
            super.handleMessage(msg);
        }
    };

    /**
     * 获取员工信息列表
     */
    private void initData() {
        progress = 0;
        diyLoading.startDownload();
        handler.sendEmptyMessage(1);
        handler.sendMessageDelayed(Message.obtain(), 1000);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("need_total", true);
        hashMap.put("uid", 1);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<EmployeeBean> allDepartmentTree = posApi.getAllDepartmentTree(hashMap);
        allDepartmentTree.enqueue(new Callback<EmployeeBean>() {
            @Override
            public void onResponse(Call<EmployeeBean> call, Response<EmployeeBean> response) {
                ToastUtil.showToast(DownloadActivity.this,"请求已经接收到了");
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null) {
                    ToastUtil.showToast(DownloadActivity.this,"请求正确");
                    EmployeeBean.ResultBean result = response.body().getResult();
                    List<EmployeeBean.ResultBean.ResDataBean> res_data = result.getRes_data();
                    List<Node> mlist = new ArrayList<>();
                    for (int i = 0; i < res_data.size(); i++) {
                        mlist.add(new Node(res_data.get(i).getId(), res_data.get(i).getPId(),
                                res_data.get(i).getName(), res_data.get(i).getRes_id(), res_data.get(i).getRes_model()
                                , res_data.get(i).getFg1(), res_data.get(i).getFg2(), res_data.get(i).getFg3(),res_data.get(i).getWorker_code(),
                                res_data.get(i).getWx_open_id(), res_data.get(i).getEmployee_avatar()));
                    }
                    downOk = true;
                    createUserDb(mlist);
                }
            }

            @Override
            public void onFailure(Call<EmployeeBean> call, Throwable t) {
                ToastUtil.showToast(DownloadActivity.this,"请求错误");
            }
        });
    }

    /**
     * 根据员工列表
     * 创建用户表
     */
    private void createUserDb(final List<Node> mlist) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                long start = SystemClock.currentThreadTimeMillis();
                for (int i = 0; i < mlist.size(); i++) {
                    if (mlist.get(i).getRes_model().equals("hr.employee")) {
                        UserItem userItem = new UserItem();
                        userItem.employeeId = mlist.get(i).getRes_id();
                        userItem.employee_avatar = mlist.get(i).getEmployee_avatar();
                        if (!StringUtils.isEmpty(mlist.get(i).getFg1())) {
                            userItem.enlcon1[0] = 1;
                            userItem.fp1 = hexString2Bytes(mlist.get(i).getFg1());
                        }
                        if (!StringUtils.isEmpty(mlist.get(i).getFg2())) {
                            userItem.enlcon2[0] = 1;
                            userItem.fp2 = hexString2Bytes(mlist.get(i).getFg2());
                        }
                        if (!StringUtils.isEmpty(mlist.get(i).getFg3())) {
                            userItem.enlcon3[0] = 1;
                            userItem.fp3 = hexString2Bytes(mlist.get(i).getFg3());
                        }
                        if (!StringUtils.isEmpty(mlist.get(i).getWx_open_id())) {
                            userItem.wx = mlist.get(i).getWx_open_id();
                        }
                        userItem.username = mlist.get(i).getName();
                        userItem.userid = mlist.get(i).getRes_id();
                        if (UsersList.getInstance().UserIsExists(userItem.employeeId)) {
                            UsersList.getInstance().UpdateUserAvatar(userItem);
                            if (!StringUtils.isEmpty(mlist.get(i).getFg1())) {
                                UsersList.getInstance().UpdateUserFp1(userItem);
                            }
                            if (!StringUtils.isEmpty(mlist.get(i).getFg2())) {
                                UsersList.getInstance().UpdateUserFp2(userItem);
                            }
                            if (!StringUtils.isEmpty(mlist.get(i).getFg3())) {
                                UsersList.getInstance().UpdateUserFp3(userItem);
                            }
                            if(!StringUtils.isEmpty(mlist.get(i).getWorker_code())){
                                byte[] bytes = hexString2Bytes(mlist.get(i).getWorker_code());
                                if(bytes!=null){
                                    System.arraycopy(bytes,0,userItem.enlcon1,1,4);
                                    UsersList.getInstance().UpdateUserNFC(userItem);
                                }
                            }
                        } else {
                            if(!StringUtils.isEmpty(mlist.get(i).getWorker_code())){
                                byte[] bytes = hexString2Bytes(mlist.get(i).getWorker_code());
                                if(bytes!=null) {
                                    System.arraycopy(bytes, 0, userItem.enlcon1, 1, 4);
                                }
                            }
                            UsersList.getInstance().AppendUser(userItem);
                        }
                    } else {
                    }
                }
//                handler.sendEmptyMessage(2);
                Log.e("robotime", "用时多少： " + (SystemClock.currentThreadTimeMillis() - start));
                ToastUtil.showToast(DownloadActivity.this,"用时多少： " + (SystemClock.currentThreadTimeMillis() - start));
                toMain();
            }
        });
    }

    private void toMain(){
        SPUtils.getInstance().put(SysConstant.IS_FIRST, false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
