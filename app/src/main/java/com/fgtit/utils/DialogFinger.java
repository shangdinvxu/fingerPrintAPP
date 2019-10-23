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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogFinger extends Dialog {

    @BindView(com.fgtit.fingerprintapp.R.id.tv_type)
    TextView tvType;
    @BindView(com.fgtit.fingerprintapp.R.id.img_tip)
    ImageView imgTip;
    @BindView(com.fgtit.fingerprintapp.R.id.tv_state_message)
    TextView tvStateMessage;
    @BindView(com.fgtit.fingerprintapp.R.id.img_close)
    ImageView imgClose;
    @BindView(com.fgtit.fingerprintapp.R.id.linear_bac)
    LinearLayout linearBac;
    private LayoutInflater inflater;
    private Display display;
    private View view;
    private Context context;

    public DialogFinger(@NonNull Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView();
    }

    public DialogFinger(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        inflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView();
    }

    protected DialogFinger(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        inflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView();
    }

    private void initView() {
        view = inflater.inflate(com.fgtit.fingerprintapp.R.layout.dialog_finger, null);
        setContentView(view);
        ButterKnife.bind(this, view);


        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.CENTER;
        wl.width = (int) (display.getWidth() * 0.9);
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wl);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    public DialogFinger setTvType(String type) {
        tvType.setText(type);
        return this;
    }

    public DialogFinger setTvMessage(String stateMessage) {
        tvStateMessage.setText(stateMessage);
        return this;
    }

    public DialogFinger setTvMessage(String stateMessage, int color) {
        tvStateMessage.setText(stateMessage);
        tvStateMessage.setTextColor(color);
        return this;
    }

    public DialogFinger setImageBitmap(Bitmap bitmap) {
        imgTip.setImageBitmap(bitmap);
        return this;
    }

    public DialogFinger setImageBitmapVisible(boolean show) {
        if (show){
            imgTip.setVisibility(View.VISIBLE);
        }else {
            imgTip.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public DialogFinger setImageResource(int source) {
        imgTip.setImageResource(source);
        return this;
    }

    public DialogFinger setImageGlide(String employee_avatar) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context).load(employee_avatar).apply(requestOptions).into(imgTip);
        return this;
    }

    public DialogFinger setLinearImageResource(int source) {
        linearBac.setBackgroundResource(source);
        return this;
    }

    public DialogFinger setCloseVisible(boolean visible) {
        if (visible) {
            imgClose.setVisibility(View.VISIBLE);
        } else {
            imgClose.setVisibility(View.GONE);
        }
        return this;
    }

    public DialogFinger setClickClose(final View.OnClickListener listener) {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }
}
