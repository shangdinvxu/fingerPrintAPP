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
public class CanteenTotalAdapter extends BaseQuickAdapter<CanteenTotalBean.ResultBean.ResDataBean, BaseViewHolder> {
    private Context context;

    public CanteenTotalAdapter(int layoutResId, @Nullable List<CanteenTotalBean.ResultBean.ResDataBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CanteenTotalBean.ResultBean.ResDataBean item) {
        helper.setText(R.id.name,item.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
         CanteenItemAdapter childAdapter = new CanteenItemAdapter(R.layout.item_item_canteen_total,item.getDetail_lines(), context);
        recyclerView.setAdapter(childAdapter);
    }

}
