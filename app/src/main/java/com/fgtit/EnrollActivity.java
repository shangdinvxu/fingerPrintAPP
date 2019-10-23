package com.fgtit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fgtit.app.Fingerprint;
import com.fgtit.app.UserItem;
import com.fgtit.app.UsersList;
import com.fgtit.fingerprintapp.R;
import com.fgtit.fpcore.FPMatch;
import com.fgtit.http.PosApi;
import com.fgtit.http.RetrofitClient;
import com.fgtit.multilevelTreeList.Node;
import com.fgtit.multilevelTreeList.OnTreeNodeClickListener;
import com.fgtit.multilevelTreeList.SimpleTreeAdapter;
import com.fgtit.utils.DialogDelete;
import com.fgtit.utils.DialogEnroll;
import com.fgtit.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollActivity extends Activity {

    private static final String TGP = "robotime";

    @BindView(R.id.list_bom)
    ListView listBom;
    @BindView(R.id.edit_search)
    AutoCompleteTextView editSearch;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title_string)
    TextView tvTitleString;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.tv_cancel_search)
    TextView tvCancelSearch;

    private byte[] jpgbytes = null;

    private ImageView fpImage;
    private TextView tvFpStatus;
    private Dialog fpDialog;
    private int iPlaceCount = 0;
    private byte[] fpTemp1 = new byte[256];
    private byte[] fpTemp2 = new byte[256];
    //NFC
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;

    private Dialog cardDialog = null;

    private int iEnrolIndex = 0;//区分3个 指纹  卡
    private int employee_id;

    UserItem userItem = new UserItem();
    public String CardSN = "";

    private SimpleTreeAdapter mAdapter;
    private DialogEnroll dialogEnroll;
    private ArrayAdapter<String> stringArrayAdapter;
    private SearchEditAdapter searchAdapter;
    private ProgressDialog initprogressDialog;
    private Handler mProgressDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initprogressDialog.show();
                    break;
                case 1:
                    initprogressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        ButterKnife.bind(this);

        initData();
        KeyboardUtils.hideSoftInput(editSearch);
//        Card
        InitReadCard();
    }


    @Override
    public void onPause() {
        super.onPause();

//        this.setResult(0);
//        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void InitReadCard() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "Device does not support NFC!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "Enable the NFC function in the system settings!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
    }


    /**
     * 获取员工信息列表
     */
    private void initData() {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("need_total", true);
        hashMap.put("uid", 1);
        initprogressDialog = new ProgressDialog(this);
        initprogressDialog.setMessage("加载员工信息");
        mProgressDialog.sendEmptyMessage(0);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<EmployeeBean> allDepartmentTree = posApi.getAllDepartmentTreeSimple(hashMap);
        allDepartmentTree.enqueue(new Callback<EmployeeBean>() {
            @Override
            public void onResponse(Call<EmployeeBean> call, Response<EmployeeBean> response) {
                if (response.body() == null) {
                    mProgressDialog.sendEmptyMessage(1);
                    return;
                }
                if (response.body().getError() != null) {
                    mProgressDialog.sendEmptyMessage(1);
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null) {
                    EmployeeBean.ResultBean result = response.body().getResult();
                    List<EmployeeBean.ResultBean.ResDataBean> res_data = result.getRes_data();
                    List<Node> mlist = new ArrayList<>();
                    for (int i = 0; i < res_data.size(); i++) {
                        mlist.add(new Node(res_data.get(i).getId(), res_data.get(i).getPId(),
                                res_data.get(i).getName(), res_data.get(i).getRes_id(), res_data.get(i).getRes_model()
                                , res_data.get(i).getFg1(), res_data.get(i).getFg2(), res_data.get(i).getFg3(), res_data.get(i).getWorker_code(),
                                res_data.get(i).getWx_open_id(), res_data.get(i).getEmployee_avatar()));
                    }
                    mAdapter = new SimpleTreeAdapter(listBom, EnrollActivity.this,
                            mlist, 2, R.mipmap.zhankai, R.mipmap.shousuo);
                    listBom.setAdapter(mAdapter);

//                    editSearch.setFocusable(false);
                    searchAdapter = new SearchEditAdapter(mlist, EnrollActivity.this);
                    editSearch.setThreshold(1);
                    editSearch.setAdapter(searchAdapter);
                    initSearchItemClick();

                    mProgressDialog.sendEmptyMessage(1);
//                    createUserDb(mlist);
                    clickListner();
                }
            }

            @Override
            public void onFailure(Call<EmployeeBean> call, Throwable t) {
                mProgressDialog.sendEmptyMessage(1);
            }
        });
    }


    /**
     * 搜索出来的内容item点击事件
     */
    private void initSearchItemClick() {
        editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Node> nodes = searchAdapter.getmList();
                KeyboardUtils.hideSoftInput(editSearch);
                getEmployeeDetail(nodes.get(position));
            }
        });
    }

    @OnClick(R.id.img_back)
    void clickBack() {
        finish();
    }

    /**
     * 点击搜索
     */
    @OnClick(R.id.img_search)
    void clickSearchImg() {
        imgBack.setVisibility(View.GONE);
        tvTitleString.setVisibility(View.GONE);
        imgSearch.setVisibility(View.GONE);
        tvCancelSearch.setVisibility(View.VISIBLE);
        editSearch.setVisibility(View.VISIBLE);
        editSearch.setFocusable(true);
    }

    /**
     * 点击取消
     */
    @OnClick(R.id.tv_cancel_search)
    void clickCancelSearch() {
        imgBack.setVisibility(View.VISIBLE);
        tvTitleString.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.VISIBLE);
        tvCancelSearch.setVisibility(View.GONE);
        editSearch.setVisibility(View.GONE);
        KeyboardUtils.hideSoftInput(editSearch);
    }

    /**
     * 列表点击事件，弹出dialog，准备录指纹
     */
    private void clickListner() {
        mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.getRes_model().equals("hr.employee")) {
                    getEmployeeDetail(node);
//                    showFingerDialog(node);
                }
            }
        });
    }

    /**
     * 获取点击的员工的详细信息
     * <p>
     * 为了保证是最新信息
     */
    private void getEmployeeDetail(final Node node) {
        final ProgressDialog progressDialog = new ProgressDialog(EnrollActivity.this);
        progressDialog.show();

//加载nfc
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);


        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", node.getRes_id());
        PosApi posApi = RetrofitClient.getInstance(EnrollActivity.this).create(PosApi.class);
        Call<EmployeeDetailBean> fg_employee_detail = posApi.get_fg_employee_detail(hashMap);
        fg_employee_detail.enqueue(new Callback<EmployeeDetailBean>() {
            @Override
            public void onResponse(Call<EmployeeDetailBean> call, Response<EmployeeDetailBean> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_data() != null) {
                    EmployeeDetailBean.ResultBean.ResDataBean res_data = response.body().getResult().getRes_data();
                    employee_id = node.getRes_id();
                    showFingerDialog(res_data);
                }
            }

            @Override
            public void onFailure(Call<EmployeeDetailBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 显示录指纹的dialog
     */
    private void showFingerDialog(final EmployeeDetailBean.ResultBean.ResDataBean res_data) {
        dialogEnroll = new DialogEnroll(EnrollActivity.this, R.style.AlertDialogStyle);
        dialogEnroll.show();

        boolean is_show_clear_button = false;
        //初始化dialog
        dialogEnroll.setUserName(res_data.getName());
        if (StringUtils.isEmpty(res_data.getWx_open_id())) {
            dialogEnroll.setTvWx("未录入");
        } else {
            is_show_clear_button = true;
            dialogEnroll.setTvWx("已录入");
        }
        if (StringUtils.isEmpty(res_data.getRt_employee_fingerprint1())) {
            dialogEnroll.setTvEnroll1("未录入");
        } else {
            is_show_clear_button = true;
            dialogEnroll.setTvEnroll1("已录入");
        }
        if (StringUtils.isEmpty(res_data.getRt_employee_fingerprint2())) {
            dialogEnroll.setTvenroll2("未录入");
        } else {
            is_show_clear_button = true;
            dialogEnroll.setTvenroll2("已录入");
        }
        if (StringUtils.isEmpty(res_data.getRt_employee_fingerprint3())) {
            dialogEnroll.setTvenroll3("未录入");
        } else {
            is_show_clear_button = true;
            dialogEnroll.setTvenroll3("已录入");
        }
        if (StringUtils.isEmpty(res_data.getWorker_code())) {
            dialogEnroll.setTvNFC("未录入");
        } else {
            is_show_clear_button = true;
            dialogEnroll.setTvNFC(res_data.getWorker_code());
        }
        if (is_show_clear_button) {
            dialogEnroll.setVisibleClearMessage(true);
        } else {
            dialogEnroll.setVisibleClearMessage(false);
        }

        dialogEnroll.setClickClearMessage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogDelete dialogDelete = new DialogDelete(EnrollActivity.this, R.style.AlertDialogStyle);
                dialogDelete.show();
                dialogDelete.setClickEnroll1(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearMessage(res_data.getEmployee_id());
                        dialogDelete.dismiss();
                    }
                });
            }
        });

        dialogEnroll.setClickEnroll1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FPDialog(1);
            }
        });

        dialogEnroll.setClickNFC(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NFCDialog(4);
            }
        });

        dialogEnroll.setClickEnroll2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FPDialog(2);
            }
        });

        dialogEnroll.setClickEnroll3(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FPDialog(3);
            }
        });

        dialogEnroll.setClickWX(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmap = generateBitmap(String.valueOf(employee_id), 480, 480);
                dialogEnroll.setStateImageByBitmap(mBitmap);
            }
        });

        dialogEnroll.setClickClose(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nfcAdapter != null)
                    nfcAdapter.disableForegroundDispatch(EnrollActivity.this);
            }
        });

        dialogEnroll.setClickBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fingerprint.getInstance().Cancel();
                Fingerprint.getInstance().setHandler(null);
                setDialogChushi();
            }
        });
    }

    /**
     * 清空用户信息
     */
    private void clearMessage(final int employee_id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("");
        progressDialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", employee_id);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<ClearMessageBean> objectCall = posApi.clean_employee_fg(hashMap);
        objectCall.enqueue(new Callback<ClearMessageBean>() {
            @Override
            public void onResponse(Call<ClearMessageBean> call, Response<ClearMessageBean> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    dialogEnroll.setTvWx("未录入");
                    dialogEnroll.setTvEnroll1("未录入");
                    dialogEnroll.setTvenroll2("未录入");
                    dialogEnroll.setTvenroll3("未录入");
                    dialogEnroll.setTvNFC("未录入");
                    dialogEnroll.setVisibleClearMessage(false);
//                    UserItem userItem = new UserItem();
//                    userItem.employeeId = employee_id;
//                    userItem.enlcon1[0] = 0;
//                    userItem.enlcon2[0] = 0;
//                    userItem.enlcon3[0] = 0;
//                    userItem.fp1 = new byte[512];
//                    userItem.fp2 = new byte[512];
//                    userItem.fp3 = new byte[512];
//                    UsersList.getInstance().UpdateUserFp1(userItem);
//                    UsersList.getInstance().UpdateUserFp2(userItem);
//                    UsersList.getInstance().UpdateUserFp3(userItem);
                    UsersList.getInstance().DeleteUser(employee_id);
                    UsersList.getInstance().LoadAll();
                }
            }

            @Override
            public void onFailure(Call<ClearMessageBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 生成二维码
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化dialog
     */
    private void FPDialog(int i) {
        dialogEnroll.setVisibleBack(true);
        dialogEnroll.setVisibleClearMessage(false);
        dialogEnroll.setStateImageByDrawable(R.mipmap.zhiwen);
        dialogEnroll.setStateMessage("请长按指纹");
        iEnrolIndex = i;

        iPlaceCount = 0;
        Fingerprint.getInstance().SetUpImage(true);
        Fingerprint.getInstance().setHandler(fingerprintHandler);
        Fingerprint.getInstance().Process();
    }

    /**
     *
     */
    private void NFCDialog(int i) {
        dialogEnroll.setVisibleBack(true);
        dialogEnroll.setVisibleClearMessage(false);
        dialogEnroll.setStateMessage("请将nfc卡片靠近机器");
        iEnrolIndex = i;
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
    }


    @SuppressLint("HandlerLeak")
    private final Handler mDialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialogEnroll == null) {
                dialogEnroll = new DialogEnroll(EnrollActivity.this, R.style.AlertDialogStyle);
                dialogEnroll.show();
            }
            switch (msg.what) {
                case 1:
                    dialogEnroll.setStateMessage("请长按指纹");
                    dialogEnroll.setCloseVisible(false);
                    break;
                case 2:
                    dialogEnroll.setStateMessage("获取图像..");
                    break;
                case 3:
                    Bitmap image = BitmapFactory.decodeByteArray((byte[]) msg.obj, 0, ((byte[]) msg.obj).length);
                    dialogEnroll.setStateImageByBitmap(image);
                    dialogEnroll.setStateMessage("开始处理..");
                    break;
                case 4:
                    dialogEnroll.setStateMessage("正在识别..");
                    break;
                case 5:
                    try {
                        upLoadImage();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    dialogEnroll.setStateMessage("录入失败!");
                    dialogEnroll.setStateImageByDrawable(R.mipmap.fail);
                    dialogEnroll.setCloseVisible(true);
                    dialogEnroll.setBtnFailLinearVisible(true);
                    dialogEnroll.setClickButtonBack(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDialogChushi();
                        }
                    });
                    dialogEnroll.setClickButtonRetest(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDialogChushi();
                            FPDialog(iEnrolIndex);
                        }
                    });
                    break;
                case 7:
                    switch (iEnrolIndex) {
                        case 1:
                            if (UsersList.getInstance().UserIsExists(employee_id)) {
                                UsersList.getInstance().UpdateUserFp1(userItem);
                            } else {
                                UsersList.getInstance().AppendUser(userItem);
                            }
                            break;
                        case 2:
                            if (UsersList.getInstance().UserIsExists(employee_id)) {
                                UsersList.getInstance().UpdateUserFp2(userItem);
                            } else {
                                UsersList.getInstance().AppendUser(userItem);
                            }
                            break;
                        case 3:
                            if (UsersList.getInstance().UserIsExists(employee_id)) {
                                UsersList.getInstance().UpdateUserFp3(userItem);
                            } else {
                                UsersList.getInstance().AppendUser(userItem);
                            }
                            break;
                        case 4:
                            if (UsersList.getInstance().UserIsExists(employee_id)) {
                                UsersList.getInstance().UpdateUserNFC(userItem);
                            } else {
                                UsersList.getInstance().AppendUser(userItem);
                            }
                            UsersList.getInstance().LoadAll();
                            break;
                    }
                    dialogEnroll.setBtnFinish(true);
                    dialogEnroll.setCloseVisible(true);
                    dialogEnroll.setClickBtnFinish(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (iEnrolIndex) {
                                case 1:
                                    dialogEnroll.setTvEnroll1("已录入");
                                    break;
                                case 2:
                                    dialogEnroll.setTvenroll2("已录入");
                                    break;
                                case 3:
                                    dialogEnroll.setTvenroll3("已录入");
                                    break;
                            }
                            setDialogChushi();
                        }
                    });
                    break;
            }
        }
    };

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    /**
     * byte转字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        return r;
    }

    /**
     * 上传用户指纹到服务器
     */
    private void upLoadImage() throws UnsupportedEncodingException {
        final ProgressDialog dialog = new ProgressDialog(EnrollActivity.this);
        dialog.setMessage("");
        dialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", employee_id);
        String fingerString = "";
        switch (iEnrolIndex) {
            case 1:
                fingerString = bytes2HexString(userItem.fp1);
                break;
            case 2:
                fingerString = bytes2HexString(userItem.fp2);
                break;
            case 3:
                fingerString = bytes2HexString(userItem.fp3);
                break;
        }
        hashMap.put("employee_fg", fingerString);
        hashMap.put("fg_type", iEnrolIndex);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<FingerSuccessBean> objectCall = posApi.update_employee_fg(hashMap);
        objectCall.enqueue(new Callback<FingerSuccessBean>() {
            @Override
            public void onResponse(Call<FingerSuccessBean> call, Response<FingerSuccessBean> response) {
                dialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    mDialogHandler.sendEmptyMessage(6);
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    if (dialogEnroll != null) {
                        dialogEnroll.setStateImageByDrawable(R.mipmap.chenggong);
                        dialogEnroll.setStateMessage("录入成功!");
                    }
                    if (mDialogHandler != null)
                        mDialogHandler.sendEmptyMessage(7);
                }
            }

            @Override
            public void onFailure(Call<FingerSuccessBean> call, Throwable t) {
                dialog.dismiss();
                ToastUtil.showToast(EnrollActivity.this,"请求失败");
//                mDialogHandler.sendEmptyMessage(6);
            }
        });
    }

    private void setDialogChushi() {
        dialogEnroll.setVisibleBack(false);
        dialogEnroll.setVisibleClearMessage(true);
        dialogEnroll.setAllLinearVisible(true);
        dialogEnroll.setBtnFinish(false);
        dialogEnroll.setStateImageVisible(false);
        dialogEnroll.setStateImageVisible(false);
        dialogEnroll.setStateMessageVisible(false);
        dialogEnroll.setBtnFailLinearVisible(false);
        dialogEnroll.setCloseVisible(true);
    }

    @SuppressLint("HandlerLeak")
    private final Handler fingerprintHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Fingerprint.STATE_PLACE:
                    mDialogHandler.sendEmptyMessage(1);
                    break;
                case Fingerprint.STATE_LIFT:
                    break;
                case Fingerprint.STATE_GETIMAGE:
                    mDialogHandler.sendEmptyMessage(2);
                    break;
                case Fingerprint.STATE_UPIMAGE: {
                    mDialogHandler.obtainMessage(3, (byte[]) msg.obj).sendToTarget();
                }
                break;
                case Fingerprint.STATE_GENDATA: {
                    mDialogHandler.sendEmptyMessage(4);
                }
                break;
                case Fingerprint.STATE_UPDATA: {
                    iPlaceCount++;
                    if (iPlaceCount >= 2) {
                        System.arraycopy((byte[]) msg.obj, 0, fpTemp2, 0, 256);
                        userItem.employeeId = employee_id;
                        switch (iEnrolIndex) {
                            case 1:
                                Log.d("EnrollActivity", "FPMatch.getInstance().MatchTemplate(fpTemp1,fpTemp2):" + FPMatch.getInstance().MatchTemplate(fpTemp1, fpTemp2));
                                if (FPMatch.getInstance().MatchTemplate(fpTemp1, fpTemp2) > 40) {
                                    userItem.enlcon1[0] = 1;
                                    System.arraycopy(fpTemp1, 0, userItem.fp1, 0, 256);
                                    System.arraycopy(fpTemp2, 0, userItem.fp1, 256, 256);
                                    mDialogHandler.sendEmptyMessage(5);
                                } else {
                                    mDialogHandler.sendEmptyMessage(6);
                                }
                                break;
                            case 2:
                                if (FPMatch.getInstance().MatchTemplate(fpTemp1, fpTemp2) > 40) {
                                    userItem.enlcon2[0] = 1;
                                    System.arraycopy(fpTemp1, 0, userItem.fp2, 0, 256);
                                    System.arraycopy(fpTemp2, 0, userItem.fp2, 256, 256);
                                    mDialogHandler.sendEmptyMessage(5);
                                } else {
                                    mDialogHandler.sendEmptyMessage(6);
                                }
                                break;
                            case 3:
                                if (FPMatch.getInstance().MatchTemplate(fpTemp1, fpTemp2) > 40) {
                                    userItem.enlcon3[0] = 1;
                                    System.arraycopy(fpTemp1, 0, userItem.fp3, 0, 256);
                                    System.arraycopy(fpTemp2, 0, userItem.fp3, 256, 256);
                                    mDialogHandler.sendEmptyMessage(5);
                                } else {
                                    mDialogHandler.sendEmptyMessage(6);
                                }
                                break;
                        }
                        Fingerprint.getInstance().Cancel();
                    } else {
                        System.arraycopy((byte[]) msg.obj, 0, fpTemp1, 0, 256);
                        Fingerprint.getInstance().Process();
                    }
                }
                break;
                case Fingerprint.STATE_FAIL:
                    mDialogHandler.sendEmptyMessage(6);
                    Fingerprint.getInstance().Process();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1: {
                Bundle bl = data.getExtras();
                String barcode = bl.getString("barcode");
                //editText9.setText(barcode);
            }
            break;
            case 2:
                break;
            case 3: {
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //NFC

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        iEnrolIndex = 4;
        byte[] sn = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//        if (UsersList.getInstance().FindUserItemByCard(sn) == null) {
        upLoadNFC(sn);
//        } else {
//            ToastUtils.setBgColor(Color.WHITE);
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setMsgColor(Color.RED);
//            ToastUtils.setMsgTextSize(20);
//            ToastUtils.showShort("重复的NFC卡号");
////            Toast.makeText(EnrollActivity.this, "重复的卡", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 绑定用户NFC
     */
    private void upLoadNFC(final byte[] sn) {
        final String nfcString =/*Integer.toString(count)+":"+*/
                Integer.toHexString(sn[0] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[1] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[2] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[3] & 0xFF).toUpperCase();

        final ProgressDialog dialog = new ProgressDialog(EnrollActivity.this);
        dialog.setMessage("");
        dialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", employee_id);
        hashMap.put("worker_code", nfcString);
        hashMap.put("fg_type", 4);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<FingerSuccessBean> objectCall = posApi.update_employee_fg(hashMap);
        objectCall.enqueue(new Callback<FingerSuccessBean>() {
            @Override
            public void onResponse(Call<FingerSuccessBean> call, Response<FingerSuccessBean> response) {
                dialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    mDialogHandler.sendEmptyMessage(6);
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    if (dialogEnroll != null) {
                        if (nfcAdapter != null) {
                            nfcAdapter.disableForegroundDispatch(EnrollActivity.this);
                        }
                        setDialogChushi();
                        dialogEnroll.setTvNFC(nfcString);
                        dialogEnroll.setStateImageByDrawable(R.mipmap.chenggong);
                        dialogEnroll.setStateMessage("录入成功!");
                        userItem.enlcon1[0] = 1;
                        userItem.employeeId = employee_id;
                        UserItem userItem = UsersList.getInstance().FindUserItemByCard(sn);
                        if (userItem != null) {
                            userItem.enlcon1 = new byte[5];
                            UsersList.getInstance().UpdateUserNFC(userItem);
                        }
                        System.arraycopy(sn, 0, EnrollActivity.this.userItem.enlcon1, 1, 4);
                        UsersList.getInstance().UpdateUserNFC(EnrollActivity.this.userItem);
                        mDialogHandler.sendEmptyMessage(7);
                    }
                }
            }

            @Override
            public void onFailure(Call<FingerSuccessBean> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            Fingerprint.getInstance().Close();
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
