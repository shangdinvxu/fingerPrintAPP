package com.fgtit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fgtit.app.ActivityList;
import com.fgtit.app.Fingerprint;
import com.fgtit.app.LocatorServer;
import com.fgtit.app.LogItem;
import com.fgtit.app.LogsList;
import com.fgtit.app.SocketServer;
import com.fgtit.app.UserItem;
import com.fgtit.app.UsersList;
import com.fgtit.fingerprintapp.R;
import com.fgtit.fpcore.FPMatch;
import com.fgtit.http.NetworkInfoReceiver;
import com.fgtit.http.PosApi;
import com.fgtit.http.RetrofitClient;
import com.fgtit.http.RetrofitLoginClient;
import com.fgtit.multilevelTreeList.Node;
import com.fgtit.utils.DateTool;
import com.fgtit.utils.DialogFinger;
import com.fgtit.utils.SysConstant;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hugeterry.updatefun.UpdateFunGO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fgtit.EnrollActivity.hexString2Bytes;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.linear_start_work)
    LinearLayout linearStartWork;
    @BindView(R.id.linear_end_work)
    LinearLayout linearEndWork;
    @BindView(R.id.linear_get)
    LinearLayout linearGet;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.linear_enroll)
    LinearLayout linearEnroll;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_finger)
    TextView btnFinger;
    @BindView(R.id.btn_enroll)
    TextView btnEnroll;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.linear_eat)
    LinearLayout linearEat;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.tv_canteen_num)
    TextView tvCanteenNum;
    @BindView(R.id.name_version)
    TextView nameVersion;
    @BindView(R.id.imei)
    TextView tv_imei;
    @BindView(R.id.btn_login_by_finger)
    Button btnLoginByFinger;
    @BindView(R.id.btn_show_info)
    TextView btnShowInfo;

    private MediaPlayer mediaPlayer;

    private PowerManager.WakeLock wakeLock;

    private Timer startTimer;
    private TimerTask startTask;
    private Handler startHandler;

    private Handler handler = new Handler();

    private int mDay = -1;

    private Timer picTimer;
    private TimerTask picTask;
    private Handler picHandler;

    private DialogFinger dialogFinger = null;

    private int fingerType = 3;//打卡类型  0上班 1下班 2食堂打卡 3//默认开门 4登陆

    private Bitmap codeBitmap = null;

    private String imei = "";
    private boolean showImei = true;

    private String serveTime = "";
    private Timer codeTime;
    private Handler codeHandler;
    private TimerTask codeTask;


    //NFC
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;

    /**
     * 关闭dialog的消息队列
     * 需要的时候post进去
     * 需要打断的时候直接remove掉 目前是10秒
     */
    Runnable runnableCloseDialog = new Runnable() {
        @Override
        public void run() {
            fingerType = 3;
            if (dialogFinger != null) {
                dialogFinger.dismiss();
                dialogFinger = null;
            }
        }
    };
    private Handler closeDialogHandler = new Handler();
    private NetworkInfoReceiver netWorkStateReceiver;
    private Timer downTime;
    private TimerTask downTask;
    private Handler downHandler;

    private int num_canteen = 0;

    private boolean isShowCanteen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        imei = PhoneUtils.getIMEI();
        Log.e("robotime", "imei = " + imei);

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "myLock");
        wakeLock.acquire();
        nameVersion.setText("版本号：" + AppUtils.getAppVersionName());


        setTimeTaskStart();
        startGetServerTime();//循环获取服务器时间
        downTaskStart();//每一小时更新一次用户指纹信息
        getEatLocation(false);

        ActivityList.getInstance().setMainContext(this);
        ActivityList.getInstance().CreateDir();//创建该有的文件夹
        ActivityList.getInstance().LoadConfig();
        UsersList.getInstance().LoadAll();
        LogsList.getInstance().Init();

        if (FPMatch.getInstance().InitMatch() == 0) {
            Toast.makeText(getApplicationContext(), "Init Matcher Fail!", Toast.LENGTH_SHORT).show();
        }

        InitReadCard();

        //指纹模块设置  打开
        Fingerprint.getInstance().setContext(this);
        Fingerprint.getInstance().Open();

        List<LogItem> logItems = LogsList.getInstance().LoadAll();
        //过滤出此员工的食堂打卡记录
        if (logItems.size() > 0) {
            num_canteen = logItems.size();
            tvCanteenNum.setText("已打卡  " + num_canteen);
        }


        SocketServer.getInstance().setContext(this);
        SocketServer.getInstance().setHandler(handler);
        SocketServer.getInstance().Start();

        LocatorServer.getInstance().setContext(this);
        LocatorServer.getInstance().setHandler(handler);
        LocatorServer.getInstance().startUdpServer();


        ActivityList.getInstance().addActivity(this);


//        原onresume 里面的内容
        UsersList.getInstance().LoadAll();
        UpdateFunGO.onResume(this);
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

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }


    private void processIntent(Intent intent) {
        if (fingerType != 2 && fingerType != 4) {
            closeDialogHandler.removeCallbacks(runnableCloseDialog);
        }
        byte[] sn = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String cardstr =/*Integer.toString(count)+":"+*/
                Integer.toHexString(sn[0] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[1] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[2] & 0xFF).toUpperCase() +
                        Integer.toHexString(sn[3] & 0xFF).toUpperCase();
        UserItem ui = UsersList.getInstance().FindUserItemByCard(sn);
        if (ui != null) {
            Log.e("robotime", "name = " + ui.username);
            if (fingerType == 2) {
                toCheckCanteen(ui, cardstr);
                ActivityList.getInstance().OpenDoor();
            } else if (fingerType == 3) {
                if (dialogFinger != null)
                    dialogFinger.setTvMessage("欢迎");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialogFinger != null) {
                            dialogFinger.dismiss();
                            dialogFinger = null;
                        }
                    }
                }, 1000);
                ActivityList.getInstance().OpenDoor();
            } else if (fingerType == 4) {
                if (ui.usertype == 1) {
                    if (dialogFinger != null) {
                        dialogFinger.setTvMessage("登陆成功");
                        dialogFinger.setImageBitmap(codeBitmap);
                        dialogFinger.setCloseVisible(true);
                    }
                    dialogFinger.dismiss();
                    Intent intentActivity = new Intent(MainActivity.this, EnrollActivity.class);
                    startActivityForResult(intentActivity, 0);
                } else {
                    if (dialogFinger != null)
                        dialogFinger.setTvMessage("没有权限或未账号密码登陆过");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialogFinger != null) {
                                dialogFinger.setTvMessage("登陆成功");
                                dialogFinger.setImageBitmap(codeBitmap);
                                dialogFinger.setCloseVisible(true);
                            }
                            dialogFinger.dismiss();
                        }
                    }, 1000);
                }
            } else {
                uploadMessage(ui);
                if (fingerType == 0) {
                    ActivityList.getInstance().OpenDoor();
                }
            }
        } else {
            // TODO: 2019/3/21  未找到匹配的指纹
            if (dialogFinger != null) {
                dialogFinger.setTvMessage("未找到匹配的NFC", Color.RED);
            }
            mMediaPlayHandler.sendEmptyMessage(5);
            chushihuaDialog();
        }
        TimerStart();
    }


    @OnClick(R.id.name_version)
    void setNameVersion() {
        UpdateFunGO.manualStart(this);
    }

    @OnClick(R.id.btn_login_by_finger)
    void setBtnLoginByFinger() {
        fingerType = 4;
        startDialog();
    }

    @OnClick(R.id.btn_get)
    void setBtnGet() {
        if (isShowCanteen) {
            getEatLocation(false);
            updateCanteenData(false);//同步食堂打卡记录到服务器
        }
        initData();
    }

    @Override
    protected void onResume() {
//        if (netWorkStateReceiver == null) {
//            netWorkStateReceiver = new NetworkInfoReceiver();
//        }
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
    }


    @Override
    protected void onPause() {
//        unregisterReceiver(netWorkStateReceiver);
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    private void setTimeTaskStart() {
        if (picTimer != null)
            return;
        picTimer = new Timer();
        picHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                ShowDateTime();
                if (isShowCanteen) {
                    List<String> time = getTime();
                    for (int i = 0; i < time.size(); i++) {
                        int i1 = DateTool.timeComparator(time.get(i), DateTool.getNowDatePos());
                        if (i1 == 0) {
                            updateCanteenData(true);
                            break;
                        }
                    }
                }
                super.handleMessage(msg);
            }
        };
        picTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                picHandler.sendMessage(message);
            }
        };
        picTimer.schedule(picTask, 1000, 1000);
    }

    public void AutoCloseStop() {
        if (picTimer != null) {
            picTimer.cancel();
            picTimer = null;
            picTask.cancel();
            picTask = null;
        }
    }

    private void downTaskStart() {
        downTime = new Timer();
        downHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                initData();
                super.handleMessage(msg);
            }
        };
        downTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                downHandler.sendMessage(message);
            }
        };
        downTime.schedule(downTask, 1000 * 60 * 60 * 5, 1000 * 60 * 60 * 5);
    }

    private void ShowDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (mDay != day) {
            mDay = day;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            tvYear.setText(String.format("%d-%02d-%02d", year, month, day));
        }
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        tvTime.setText(String.format("%02d : %02d", hour, minute));
    }

    @OnClick(R.id.linear_start_work)
    void setLinearStartWork() {
        startDialog();
        fingerType = 0;
        dialogFinger.setTvType("上班");
        dialogFinger.setLinearImageResource(R.drawable.endwork);
        codeBitmap = generateBitmap("https://wechat.robotime.com/attendance?type=0&imei=" + imei + "&fg_time=" + serveTime, 480, 480);
        dialogFinger.setImageBitmap(codeBitmap);
    }

    @OnClick(R.id.linear_end_work)
    void setLinearEndWork() {
        startDialog();
        fingerType = 1;
        dialogFinger.setTvType("下班");
        dialogFinger.setLinearImageResource(R.drawable.endwork);
        codeBitmap = generateBitmap("https://wechat.robotime.com/attendance?type=1&imei=" + imei + "&fg_time=" + serveTime, 480, 480);
        dialogFinger.setImageBitmap(codeBitmap);
    }

    private void startDialog() {
        if (fingerType != 2 && fingerType != 4) {
            closeDialogHandler.postDelayed(runnableCloseDialog, 1000 * 10);
        }
        Fingerprint.getInstance().setHandler(fingerprintHandler);
        Fingerprint.getInstance().Process();
        if (dialogFinger == null) {
            dialogFinger = new DialogFinger(MainActivity.this, R.style.AlertDialogStyle);
            dialogFinger.show();
        } else {
            dialogFinger.show();
        }
        if (fingerType == 4) {
            dialogFinger.setTvMessage("请按指纹或扫码或NFC", Color.WHITE);
        } else {
            dialogFinger.setTvMessage("请按指纹或扫码或NFC", Color.WHITE);
        }
        dialogFinger.setClickClose(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runnableCloseDialog != null) {
                    closeDialogHandler.removeCallbacks(runnableCloseDialog);
                }
                fingerType = 3;
                if (dialogFinger != null) {
                    dialogFinger.dismiss();
                    dialogFinger = null;
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler mMediaPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AudioManager audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // 获取最大音乐音量
            int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            Log.e("robotime", "maxVolume = " + maxVolume);
            audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,AudioManager.FLAG_PLAY_SOUND);
//            audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
            switch (msg.what) {
                case 1:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sucessful);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 2:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fail);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 3:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.already);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 4:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.repeat);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 5:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.notfind);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 6:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.notup);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 7:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.noteattime);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 8:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.notsystem);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 9:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alreadyeat);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
                case 10:
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.welcomemeal);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                    break;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishMediaPlayer();
                }
            }, 3000);
        }
    };

    private void showDialogInfo(int type) {
        if (dialogFinger == null) {
            dialogFinger = new DialogFinger(MainActivity.this, R.style.AlertDialogStyle);
            dialogFinger.show();
            dialogFinger.setClickClose(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (runnableCloseDialog != null) {
                        closeDialogHandler.removeCallbacks(runnableCloseDialog);
                    }
                    fingerType = 3;
                    dialogFinger.dismiss();
                    dialogFinger = null;
                }
            });
        } else {
            dialogFinger.show();
        }
        switch (type) {
            case 1:
                dialogFinger.setTvMessage("正在识别..");
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void chushihuaDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialogFinger != null) {
                    dialogFinger.setTvMessage("请按指纹或扫码或NFC", Color.WHITE);
                    if (fingerType != 4) {
                        codeBitmap = generateBitmap("https://wechat.robotime.com/attendance?type=" + fingerType + "&imei=" + imei + "&fg_time=" + serveTime, 480, 480);
                        dialogFinger.setImageBitmap(codeBitmap);
                    } else {
                        dialogFinger.setImageBitmapVisible(false);
                    }
                    dialogFinger.setCloseVisible(true);
                }
                if (fingerType != 2 && fingerType != 4) {
                    closeDialogHandler.postDelayed(runnableCloseDialog, 1000 * 10);
                }
            }
        }, 2000);
    }

    /**
     * 打卡上传服务器
     */
    private void uploadMessage(final UserItem userItem) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", userItem.employeeId);
        hashMap.put("type", fingerType);
        hashMap.put("imei_code", imei);
        hashMap.put("fg_time", DateTool.getDateTime());
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<FingerTimeBean> objectCall = posApi.new_fg_attendance(hashMap);
        objectCall.enqueue(new Callback<FingerTimeBean>() {
            @Override
            public void onResponse(Call<FingerTimeBean> call, Response<FingerTimeBean> response) {
                progressDialog.dismiss();
                if (response.body().getError() != null) {
                    String message = response.body().getError().getData().getMessage();
                    if (dialogFinger != null)
                        dialogFinger.setTvMessage(message, Color.RED);
                    if (message.contains("已经")) {
                        mMediaPlayHandler.sendEmptyMessage(3);
                    } else if (message.contains("频繁")) {
                        mMediaPlayHandler.sendEmptyMessage(4);
                    } else if (message.contains("未打")) {
                        mMediaPlayHandler.sendEmptyMessage(6);
                    }
                    chushihuaDialog();
                    return;
                }
                if (response.body().getResult().getRes_code() == 1) {
                    mMediaPlayHandler.sendEmptyMessage(1);
                    if (dialogFinger != null) {
                        dialogFinger.setImageGlide(userItem.employee_avatar);
                        dialogFinger.setTvMessage(userItem.username + "   打卡成功");
                    }
                    chushihuaDialog();
                }
            }

            @Override
            public void onFailure(Call<FingerTimeBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                UsersList.getInstance().LoadAll();
                Fingerprint.getInstance().setHandler(fingerprintHandler);
                Fingerprint.getInstance().Process();
                break;
            case 1:
            case 2:
                Fingerprint.getInstance().setHandler(fingerprintHandler);
                Fingerprint.getInstance().Process();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.btn_login)
    void setBtnLogin() {
        if (showImei) {
            showImei = !showImei;
            tv_imei.setVisibility(View.VISIBLE);
            tv_imei.setText(imei);
        } else {
            showImei = !showImei;
            tv_imei.setVisibility(View.GONE);
        }
        String email = editName.getText().toString();
        String password = editPassword.getText().toString();
        if (StringUtils.isEmpty(email)) {
            ToastUtils.showShort("请填写用户名");
            return;
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("登录中");
        progressDialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("login", email);
        hashMap.put("password", password);
        hashMap.put("db", SysConstant.DB);
        PosApi posApi = RetrofitLoginClient.getInstance(MainActivity.this).create(PosApi.class);
        Call<LoginResponse> login = posApi.login_pos(hashMap);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError()
                            .getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    editPassword.setText("");
                    LoginResponse.ResultBean result = response.body().getResult();
                    List<LoginResponse.ResultBean.ResDataBean.GroupsBean> groups = result.getRes_data().getGroups();
                    List<String> stringList = new ArrayList<>();
                    for (int i = 0; i < groups.size(); i++) {
                        stringList.add(groups.get(i).getName());
                    }
                    if (stringList.size() > 0) {
                        if (stringList.contains("group_hr_manager") || stringList.contains("rt_hr_employee_manager")
                                || stringList.contains("employee_department_manager")) {
                            //找到此人本地数据库  将其设置为管理员
                            UserItem userByEmployeeId = UsersList.getInstance().findUserByEmployeeId(result.getRes_data().getEmployee_id());
                            userByEmployeeId.usertype = 1;
                            UsersList.getInstance().UpdateUserTYpe(userByEmployeeId);
                            Intent intent = new Intent(MainActivity.this, EnrollActivity.class);
                            startActivityForResult(intent, 0);
                        } else {
                            ToastUtils.showShort("没有人事权限");
                        }
                    }
                } else if (response.body().getResult() != null && response.body().getResult().getRes_code() == -1) {
                    ToastUtils.showShort(response.body().getResult().getRes_data().getError());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtils.hideSoftInput(editName);
        KeyboardUtils.hideSoftInput(editPassword);
        return super.dispatchTouchEvent(ev);
    }


    @SuppressLint("HandlerLeak")
    private final Handler fingerprintHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Fingerprint.STATE_PLACE:
                    break;
                case Fingerprint.STATE_LIFT:
                    break;
                case Fingerprint.STATE_GETIMAGE: {
//                    int valueGetImage = PSGetImage();
                    showDialogInfo(1);
                }
                break;
                case Fingerprint.STATE_UPIMAGE: {
                    if (fingerType != 2 && fingerType != 4) {
                        closeDialogHandler.removeCallbacks(runnableCloseDialog);
                    }
                    Bitmap image = BitmapFactory.decodeByteArray((byte[]) msg.obj, 0, ((byte[]) msg.obj).length);
                    if (dialogFinger != null) {
                        dialogFinger.setImageBitmapVisible(true);
                        dialogFinger.setImageBitmap(image);
                    }
                }
                break;
                case Fingerprint.STATE_GENDATA: {
//                    ToastUtils.showShort("在做什么操作");
                }
                break;
                case Fingerprint.STATE_UPDATA: {
                    UserItem ui = UsersList.getInstance().FindUserItemByFP((byte[]) msg.obj);
                    if (ui != null) {
                        Log.e("robotime", "name = " + ui.username);
                        if (fingerType == 2) {
                            toCheckCanteen(ui, "");
                            ActivityList.getInstance().OpenDoor();
                        } else if (fingerType == 3) {
                            if (dialogFinger != null)
                                dialogFinger.setTvMessage("欢迎");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (dialogFinger != null) {
                                        dialogFinger.dismiss();
                                        dialogFinger = null;
                                    }
                                }
                            }, 1000);
                            ActivityList.getInstance().OpenDoor();
                        } else if (fingerType == 4) {
                            if (ui.usertype == 1) {
                                if (dialogFinger != null) {
                                    dialogFinger.setTvMessage("登陆成功");
                                    dialogFinger.setImageBitmap(codeBitmap);
                                    dialogFinger.setCloseVisible(true);
                                }
                                dialogFinger.dismiss();
                                Intent intent = new Intent(MainActivity.this, EnrollActivity.class);
                                startActivityForResult(intent, 0);
                            } else {
                                if (dialogFinger != null)
                                    dialogFinger.setTvMessage("没有权限或未账号密码登陆过");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialogFinger != null) {
                                            dialogFinger.setTvMessage("登陆成功");
                                            dialogFinger.setImageBitmap(codeBitmap);
                                            dialogFinger.setCloseVisible(true);
                                        }
                                        dialogFinger.dismiss();
                                    }
                                }, 1000);
                            }
                        } else {
//                            LogsList.getInstance().Append(ui.userid, ui.username, 0, 0, fingerType, imei, 0, "");
                            uploadMessage(ui);
                            if (fingerType == 0) {
                                ActivityList.getInstance().OpenDoor();
                            }
                        }
                    } else {
                        // TODO: 2019/3/21  未找到匹配的指纹
                        if (dialogFinger != null) {
                            dialogFinger.setTvMessage("未找到匹配的指纹", Color.RED);
                        }
                        mMediaPlayHandler.sendEmptyMessage(5);
                        chushihuaDialog();
                    }
                    TimerStart();
                }
                break;
                case Fingerprint.STATE_FAIL:
                    dialogFinger.setTvMessage("未找到匹配的指纹", Color.RED);
                    mMediaPlayHandler.sendEmptyMessage(5);
                    chushihuaDialog();
                    Fingerprint.getInstance().Process();
                    break;
            }
        }
    };

    /**
     * 食堂打卡比较时间
     */
    private void toCheckCanteen(UserItem ui, String cardString) {
        int anInt = SPUtils.getInstance().getInt(SysConstant.CANTEEN_TIME_NUM, 0);
        if (anInt > 0) {
            List<LogItem> logItems = LogsList.getInstance().LoadAll();
            boolean is_in = false;
            for (int i = 0; i < anInt; i++) {
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), Activity.MODE_PRIVATE);
                Map<String, ?> all = sharedPreferences.getAll();
                String canteen_name = (String) all.get(SysConstant.NAME_KEY);
                String date = DateTool.getDate();
                String canteen_starttime = date + " " + all.get(SysConstant.START_TIME_KEY) + ":00";
                String canteen_endtime = date + " " + all.get(SysConstant.END_TIME_KEY) + ":00";
                int i1 = DateTool.timeComparator(canteen_starttime, DateTool.getNowDatePos());
                int i2 = DateTool.timeComparator(canteen_endtime, DateTool.getNowDatePos());
                if (i1 < 0 && i2 > 0) {//在用餐时间
                    is_in = true;
                    List<LogItem> selfList = new ArrayList<>();
                    //过滤出此员工的食堂打卡记录
                    if (logItems.size() > 0) {
                        for (int j = 0; j < logItems.size(); j++) {
                            if (logItems.get(j).fingertype == 2 && logItems.get(j).userid == ui.userid) {
                                selfList.add(logItems.get(j));
                            }
                        }
                        if (selfList.size() > 0) {
                            for (int j = 0; j < selfList.size(); j++) {
                                String currenttime = selfList.get(j).datetime;
                                Log.e("robotime", "currenttime = " + currenttime);
                                int i3 = DateTool.timeComparator(canteen_starttime, currenttime);
                                int i4 = DateTool.timeComparator(canteen_endtime, currenttime);
                                if (i3 < 0 && i4 > 0) {
                                    mMediaPlayHandler.sendEmptyMessage(9);
                                    if (dialogFinger != null) {
                                        dialogFinger.setTvMessage("已经打过用餐卡", Color.RED);
                                    }
                                    chushihuaDialog();
                                }
                            }
                        } else {
                            mMediaPlayHandler.sendEmptyMessage(10);
                            if (dialogFinger != null) {
                                if (!StringUtils.isEmpty(ui.employee_avatar))
                                    dialogFinger.setImageGlide(ui.employee_avatar);
                                if (!StringUtils.isEmpty(ui.username))
                                    dialogFinger.setTvMessage(ui.username + "   欢迎用餐");
                            }
                            chushihuaDialog();
                            num_canteen = num_canteen + 1;
                            tvCanteenNum.setText("已打卡  " + num_canteen);
                            LogsList.getInstance().Append(ui.userid, ui.username, 0, 0, 2, imei, 0, cardString, canteen_name);
                        }
                    } else {
                        mMediaPlayHandler.sendEmptyMessage(10);
                        if (dialogFinger != null) {
                            if (!StringUtils.isEmpty(ui.employee_avatar))
                                dialogFinger.setImageGlide(ui.employee_avatar);
                            if (!StringUtils.isEmpty(ui.username))
                                dialogFinger.setTvMessage(ui.username + "   欢迎用餐");
                        }
                        chushihuaDialog();
                        num_canteen = num_canteen + 1;
                        tvCanteenNum.setText("已打卡  " + num_canteen);
                        LogsList.getInstance().Append(ui.userid, ui.username, 0, 0, 2, imei, 0, cardString, canteen_name);
                    }
                }
            }
            if (!is_in) {
                mMediaPlayHandler.sendEmptyMessage(7);
                if (dialogFinger != null)
                    dialogFinger.setTvMessage("未到用餐时间", Color.RED);
                chushihuaDialog();
            }
        } else {
            if (dialogFinger != null)
                dialogFinger.setTvMessage("指纹机未录入系统", Color.RED);
            mMediaPlayHandler.sendEmptyMessage(8);
            chushihuaDialog();
        }
    }

    @SuppressLint("HandlerLeak")
    public void TimerStart() {
        if (startTimer == null) {
            startTimer = new Timer();
            startHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    TimeStop();
                    Fingerprint.getInstance().Process();
                }
            };
            startTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    startHandler.sendMessage(message);
                }
            };
            startTimer.schedule(startTask, 1000, 1000);
        }
    }

    public void TimeStop() {
        if (startTimer != null) {
            startTimer.cancel();
            startTimer = null;
            startTask.cancel();
            startTask = null;
        }
    }

    @OnClick(R.id.btn_enroll)
    void setBtnEnroll() {
        linearGet.setVisibility(View.GONE);
        linearEnroll.setVisibility(View.VISIBLE);

        btnEnroll.setTextColor(this.getResources().getColor(R.color.app_color_theme_6));
        btnFinger.setTextColor(Color.BLACK);
    }

    @OnClick(R.id.btn_finger)
    void setBtnFinger() {
        linearGet.setVisibility(View.VISIBLE);
        linearEnroll.setVisibility(View.GONE);

        btnFinger.setTextColor(this.getResources().getColor(R.color.app_color_theme_6));
        btnEnroll.setTextColor(Color.BLACK);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            wakeLock.release();
            Fingerprint.getInstance().Close();
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回收media  防止消耗系统资源!!!
     */
    private void finishMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Fingerprint.getInstance().Close();
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

    @OnClick(R.id.linear_eat)
    void setLinearEat() {
        String name = SPUtils.getInstance().getString(SysConstant.CANTEEN_NAME, "");
        if (StringUtils.isEmpty(name)) {
            getEatLocation(true);
        } else {
            getEatLocation(false);
            fingerType = 2;
            startDialog();
            dialogFinger.setTvType(name);
            dialogFinger.setLinearImageResource(R.drawable.yongcan);
            codeBitmap = generateBitmap("https://wechat.robotime.com/attendance?type=2&imei=" + imei + "&fg_time=" + serveTime, 480, 480);
            dialogFinger.setImageBitmap(codeBitmap);
        }
    }

    /**
     * 获取食堂名称
     */
    private void getEatLocation(final boolean is_dialog) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载食堂信息");
        progressDialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("imei_code", imei);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<EatLocationBean> canteen_by_imei = posApi.get_canteen_by_imei(hashMap);
        canteen_by_imei.enqueue(new Callback<EatLocationBean>() {
            @Override
            public void onResponse(Call<EatLocationBean> call, Response<EatLocationBean> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    isShowCanteen = false;
                    linearEat.setVisibility(View.GONE);
                    btnShowInfo.setVisibility(View.GONE);
                    ToastUtils.setBgColor(Color.WHITE);
                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    ToastUtils.setMsgColor(Color.RED);
                    ToastUtils.setMsgTextSize(20);
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_data() != null) {
                    isShowCanteen = true;
                    linearEat.setVisibility(View.VISIBLE);
                    btnShowInfo.setVisibility(View.VISIBLE);
                    String name = response.body().getResult().getRes_data().getName();
                    List<EatLocationBean.ResultBean.ResDataBean.TimeDataBean> time_data = response.body().getResult().getRes_data().getTime_data();
                    if (is_dialog) {
                        startDialog();
                        fingerType = 2;
                        dialogFinger.setTvType(name);
                        dialogFinger.setLinearImageResource(R.drawable.yongcan);
                        codeBitmap = generateBitmap("https://wechat.robotime.com/attendance?type=2&imei=" + imei + "&fg_time=" + serveTime, 480, 480);
                        dialogFinger.setImageBitmap(codeBitmap);
                    }
                    // TODO: 2019/3/26 将餐厅打卡时间等数据存下来
                    SPUtils.getInstance().put(SysConstant.CANTEEN_NAME, name);
                    int anInt = SPUtils.getInstance().getInt(SysConstant.CANTEEN_TIME_NUM, 0);
                    if (anInt > 0 && anInt != time_data.size()) {
                        for (int i = 0; i < anInt; i++) {
                            SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.clear();
                            edit.commit();
                        }
                    }
                    SPUtils.getInstance().put(SysConstant.CANTEEN_TIME_NUM, time_data.size());
                    for (int i = 0; i < time_data.size(); i++) {
                        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(SysConstant.NAME_KEY, time_data.get(i).getName());
                        edit.putString(SysConstant.START_TIME_KEY, time_data.get(i).getStart_time());
                        edit.putString(SysConstant.END_TIME_KEY, time_data.get(i).getEnd_time());
                        edit.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<EatLocationBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 食堂打卡上传
     */
    private void eatUpload(final UserItem userItem) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("上传中");
        progressDialog.show();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("employee_id", userItem.employeeId);
        hashMap.put("imei_code", imei);
        hashMap.put("fg_time", DateTool.getDateTime());
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<CanteenBean> objectCall = posApi.new_fg_canteen(hashMap);
        objectCall.enqueue(new Callback<CanteenBean>() {
            @Override
            public void onResponse(Call<CanteenBean> call, Response<CanteenBean> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    String message = response.body().getError().getData().getMessage();
                    dialogFinger.setTvMessage(message, Color.RED);
                    if (message.contains("未到")) {
                        mMediaPlayHandler.sendEmptyMessage(7);
                    } else if (message.contains("未录入")) {
                        mMediaPlayHandler.sendEmptyMessage(8);
                    } else if (message.contains("已经打过")) {
                        mMediaPlayHandler.sendEmptyMessage(9);
                    }
                    chushihuaDialog();
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    mMediaPlayHandler.sendEmptyMessage(10);
                    if (dialogFinger != null) {
                        dialogFinger.setImageGlide(userItem.employee_avatar);
                        dialogFinger.setTvMessage(userItem.username + "   欢迎用餐");
                    }
                    chushihuaDialog();
                }
            }

            @Override
            public void onFailure(Call<CanteenBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 获取服务器的时间
     * 在每次打卡的时候获取
     */
    private void get_now_server_time() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<ServerTimeBean> now_server_time = posApi.get_now_server_time(new HashMap());
        now_server_time.enqueue(new Callback<ServerTimeBean>() {
            @Override
            public void onResponse(Call<ServerTimeBean> call, Response<ServerTimeBean> response) {
                progressDialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    serveTime = response.body().getResult().getRes_data().getServer_time();
                }
            }

            @Override
            public void onFailure(Call<ServerTimeBean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 启动时间循环器 获取服务器时间
     * 刷新生成的二维码
     * 微信打卡使用
     */
    private void startGetServerTime() {
        codeTime = new Timer();
        codeHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                if (codeTime != null) {
                    codeTime.cancel();
                    codeTime = null;
                    codeTask.cancel();
                    codeTask = null;
                }
                get_now_server_time();
                super.handleMessage(msg);
            }
        };
        codeTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                codeHandler.sendMessage(message);
            }
        };
        codeTime.schedule(codeTask, 0, 1000 * 60 * 5);
    }


    /**
     * 同步数据
     */
    private void updateCanteenData(final boolean isDelete) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("没有网络连接,自动上传用餐数据失败");
            return;
        }
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("imei_code", imei);
        List<LogItem> logItems = LogsList.getInstance().LoadAll();
        final List<LogItem> selfList = new ArrayList<>();
        //过滤出此员工的食堂打卡记录
        if (logItems.size() > 0) {
            for (int j = 0; j < logItems.size(); j++) {
                if (logItems.get(j).fingertype == 2 && logItems.get(j).state == 0) {
                    selfList.add(logItems.get(j));
                }
            }
        }
        if (selfList.size() <= 0) {
            return;
        }
        List<HashMap<Object, Object>> list = new ArrayList<>();
        if (selfList.size() > 0) {
            for (int i = 0; i < selfList.size(); i++) {
                HashMap<Object, Object> hashMap1 = new HashMap<>();
                hashMap1.put("rt_employee_id", selfList.get(i).userid);
                hashMap1.put("rt_type", selfList.get(i).canteentype);
                hashMap1.put("rt_time", DateTool.utc2Local(selfList.get(i).datetime));
                hashMap1.put("worker_code", selfList.get(i).workcode);
                list.add(hashMap1);
            }
            hashMap.put("canteen_data", list);
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在上传数据");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<CanteenBean> objectCall = posApi.synchronize_canteen_data(hashMap);
        objectCall.enqueue(new Callback<CanteenBean>() {
            @Override
            public void onResponse(Call<CanteenBean> call, Response<CanteenBean> response) {
                dialog.dismiss();
                if (response.body() == null) return;
                if (response.body().getError() != null) {
                    ToastUtils.showShort(response.body().getError().getData().getMessage());
                    return;
                }
                if (response.body().getResult() != null && response.body().getResult().getRes_code() == 1) {
                    ToastUtils.showShort("上传数据成功");
                    if (isDelete) {
                        for (int i = 0; i < selfList.size(); i++) {
                            LogsList.getInstance().deleteLog(selfList.get(i).id);
                        }
                        num_canteen = 0;
                        tvCanteenNum.setText("已打卡  " + num_canteen);
                    }
                }
            }

            @Override
            public void onFailure(Call<CanteenBean> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    private List<String> getTime() {
        List<String> list = new ArrayList<>();
        int anInt = SPUtils.getInstance().getInt(SysConstant.CANTEEN_TIME_NUM, 0);
//        Log.e("robotime", "anInt = " + anInt);
        if (anInt > 0) {
            for (int i = 0; i < anInt; i++) {
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), Activity.MODE_PRIVATE);
                Map<String, ?> all = sharedPreferences.getAll();
                String date = DateTool.getDate();
                String canteen_endtime = date + " " + all.get(SysConstant.END_TIME_KEY) + ":00";
//                Log.e("robotime", "   canteen_endtime = " + canteen_endtime);
                list.add(canteen_endtime);
            }
        }
        return list;
    }

    /**
     * 获取员工信息列表
     */
    private void initData() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("没有网络连接,自动更新员工指纹失败");
            return;
        }
        ToastUtils.setMsgColor(Color.RED);
        ToastUtils.setMsgTextSize(25);
        ToastUtils.setBgColor(R.drawable.white_bac);
        ToastUtils.showLong("正在同步数据库请耐心等待");
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("need_total", true);
        hashMap.put("uid", 1);
        PosApi posApi = RetrofitClient.getInstance(this).create(PosApi.class);
        Call<EmployeeBean> allDepartmentTree = posApi.getAllDepartmentTree(hashMap);
        allDepartmentTree.enqueue(new Callback<EmployeeBean>() {
            @Override
            public void onResponse(Call<EmployeeBean> call, Response<EmployeeBean> response) {
                if (response.body() == null) return;
                if (response.body().getError() != null) {
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
                    createUserDb(mlist);
                }
            }

            @Override
            public void onFailure(Call<EmployeeBean> call, Throwable t) {
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
//                            UsersList.getInstance().UpdateUserAvatar(userItem);
                            if (!StringUtils.isEmpty(mlist.get(i).getFg1())) {
                                UsersList.getInstance().UpdateUserFp1(userItem);
                            }
                            if (!StringUtils.isEmpty(mlist.get(i).getFg2())) {
                                UsersList.getInstance().UpdateUserFp2(userItem);
                            }
                            if (!StringUtils.isEmpty(mlist.get(i).getFg3())) {
                                UsersList.getInstance().UpdateUserFp3(userItem);
                            }
                            if (!StringUtils.isEmpty(mlist.get(i).getWorker_code())) {
                                byte[] bytes = hexString2Bytes(mlist.get(i).getWorker_code());
                                System.arraycopy(bytes, 0, userItem.enlcon1, 1, 4);
                                UsersList.getInstance().UpdateUserNFC(userItem);
                            }
                        } else {
                            if (!StringUtils.isEmpty(mlist.get(i).getWorker_code())) {
                                byte[] bytes = hexString2Bytes(mlist.get(i).getWorker_code());
                                System.arraycopy(bytes, 0, userItem.enlcon1, 1, 4);
                            }
                            UsersList.getInstance().AppendUser(userItem);
                        }
                    }
                }
                Log.e("robotime", "用时多少： " + (SystemClock.currentThreadTimeMillis() - start));
                ToastUtils.showShort("同步完成");
                UsersList.getInstance().LoadAll();
            }
        });
    }


    @OnClick(R.id.btn_show_info)
    public void onViewClicked() {
        startActivity(new Intent(MainActivity.this,CanteenTotalInfoActivity.class));
    }
}
