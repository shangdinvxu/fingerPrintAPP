package com.fgtit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fgtit.fingerprintapp.R;

import java.util.List;

/**
 * Created by Daniel.Xu on 2019-10-22.
 */
public class CanteenItemAdapter extends BaseQuickAdapter<CanteenTotalBean.ResultBean.ResDataBean.DetailLinesBean, BaseViewHolder> {
    private Context context;

    public CanteenItemAdapter(int layoutResId, @Nullable List<CanteenTotalBean.ResultBean.ResDataBean.DetailLinesBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CanteenTotalBean.ResultBean.ResDataBean.DetailLinesBean item) {
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.number,item.getRt_real_total()+"");
        helper.setText(R.id.time,item.getRt_start_time()+"-"+item.getRt_end_time());
    }
}
