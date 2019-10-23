package com.fgtit.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogEnroll extends Dialog {

    @BindView(com.fgtit.fingerprintapp.R.id.user_name)
    TextView userName;
    @BindView(com.fgtit.fingerprintapp.R.id.user_post)
    TextView userZw;
    @BindView(com.fgtit.fingerprintapp.R.id.user_email)
    TextView userEmail;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_state_message)
    TextView tvStateMessage;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_state_wx)
    TextView tvStateWx;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_enroll1)
    TextView tvEnroll1;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_enroll2)
    TextView tvEnroll2;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_enroll3)
    TextView tvEnroll3;
    @BindView(com.fgtit.fingerprintapp.R.id.img_state)
    ImageView imgState;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_wx)
    LinearLayout linearWx;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_enroll1)
    LinearLayout linearEnroll1;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_enroll2)
    LinearLayout linearEnroll2;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_enroll3)
    LinearLayout linearEnroll3;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_nfc)
    LinearLayout linearNfc;
    @BindView(com.fgtit.fingerprintapp.R.id.img_close)
    ImageView imgClose;
    @BindView(com.fgtit.fingerprintapp.R.id.btn_finish)
    Button btnFinish;
    @BindView(com.fgtit.fingerprintapp.R.id.btn_retest)
    Button btnRetest;
    @BindView(com.fgtit.fingerprintapp.R.id.btn_back)
    Button btnBack;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_fail)
    LinearLayout linearFail;
    @BindView(com.fgtit.fingerprintapp.R.id.btn_clear_message)
    Button btnClearMessage;
    @BindView(com.fgtit.fingerprintapp.R.id.img_back)
    ImageView imgBack;
    @BindView(com.fgtit.fingerprintapp.R.id.nfc_tv)
    TextView nfcTv;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_nfv_state)
    TextView nfcTvState;
    private LayoutInflater inflater;
    private Display display;
    //    private OnSendCommonClickListener sendCommonClickListener;
    private View view;
    private Context context;

    public DialogEnroll(@NonNull Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView();
    }

    public DialogEnroll(@NonNull Context context, int themeResId) {
        super(context, themeResId);
//        this.sendCommonClickListener = sendCommonClickListener;
        this.context = context;
        inflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView();
    }

    public interface OnSendCommonClickListener {
        void OnSendCommonClick();
    }

    protected DialogEnroll(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        view = inflater.inflate(com.fgtit.fingerprintapp.R.layout.dialog_get, null);
        setContentView(view);
        ButterKnife.bind(this, view);


        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.CENTER;
        wl.width = (int) (display.getWidth() * 0.8);
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wl);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

    }


    public DialogEnroll setClickClose(final View.OnClickListener listener) {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }




    public DialogEnroll setClickBack(final View.OnClickListener listener) {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setVisibleBack(boolean visibleBack) {
        if (visibleBack) {
            imgBack.setVisibility(View.VISIBLE);
        } else {
            imgBack.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setClickEnroll1(final View.OnClickListener listener) {
        linearEnroll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


    public DialogEnroll setClickEnroll2(final View.OnClickListener listener) {
        linearEnroll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickEnroll3(final View.OnClickListener listener) {
        linearEnroll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickNFC(final View.OnClickListener listener) {
        linearNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickWX(final View.OnClickListener listener) {
        linearWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


    public DialogEnroll setUserName(String name) {
        userName.setText(name);
        return this;
    }

    public DialogEnroll setUserEmail(String email) {
        userEmail.setText(email);
        return this;
    }

    public DialogEnroll setUserPost(String post) {
        userZw.setText(post);
        return this;
    }

    public DialogEnroll setStateMessage(String title) {
        this.setAllLinearVisible(false);
        tvStateMessage.setText(title);
        tvStateMessage.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogEnroll setStateMessageVisible(boolean visible) {
        if (visible) {
            tvStateMessage.setVisibility(View.VISIBLE);
        } else {
            tvStateMessage.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setTvWx(String title) {
        tvStateWx.setText(title);
        return this;
    }

    public DialogEnroll setTvEnroll1(String title) {
        tvEnroll1.setText(title);
        return this;
    }

    public DialogEnroll setTvenroll2(String title) {
        tvEnroll2.setText(title);
        return this;
    }

    public DialogEnroll setTvenroll3(String title) {
        tvEnroll3.setText(title);
        return this;
    }

    public DialogEnroll setTvNFC(String title) {
        nfcTv.setText(title);
        return this;
    }


    public DialogEnroll setAllLinearVisible(boolean visible) {
        if (visible) {
            linearWx.setVisibility(View.VISIBLE);
            linearEnroll1.setVisibility(View.VISIBLE);
            linearEnroll2.setVisibility(View.VISIBLE);
            linearEnroll3.setVisibility(View.VISIBLE);
            linearNfc.setVisibility(View.VISIBLE);
        } else {
            linearWx.setVisibility(View.GONE);
            linearEnroll1.setVisibility(View.GONE);
            linearEnroll2.setVisibility(View.GONE);
            linearEnroll3.setVisibility(View.GONE);
            linearNfc.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setStateImageByBitmap(Bitmap bitmap) {
        this.setAllLinearVisible(false);
        imgState.setImageBitmap(bitmap);
        imgState.setVisibility(View.VISIBLE);
        this.setStateMessage("微信小程序搜索【若态考勤助手】扫描二维码绑定");
        return this;
    }

    public DialogEnroll setStateImageByDrawable(int source) {
        this.setAllLinearVisible(false);
        imgState.setImageResource(source);
        imgState.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogEnroll setStateImageVisible(boolean visible) {
        if (visible) {
            imgState.setVisibility(View.VISIBLE);
        } else {
            imgState.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setCloseVisible(boolean visible) {
        if (visible) {
            imgClose.setVisibility(View.VISIBLE);
        } else {
            imgClose.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setBtnFinish(boolean visible) {
        if (visible) {
            btnFinish.setVisibility(View.VISIBLE);
        } else {
            btnFinish.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setCloseImage(final View.OnClickListener listener) {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickBtnFinish(final View.OnClickListener listener) {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setBtnFailLinearVisible(boolean visible) {
        if (visible) {
            linearFail.setVisibility(View.VISIBLE);
        } else {
            linearFail.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogEnroll setClickButtonRetest(final View.OnClickListener listener) {
        btnRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickButtonBack(final View.OnClickListener listener) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setClickClearMessage(final View.OnClickListener listener) {
        btnClearMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public DialogEnroll setVisibleClearMessage(boolean visible) {
        if (visible) {
            btnClearMessage.setVisibility(View.VISIBLE);
        } else {
            btnClearMessage.setVisibility(View.GONE);
        }
        return this;
    }
}
