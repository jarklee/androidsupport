/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jarklee.androidsupport.R;
import com.jarklee.essential.common.Releasable;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<VH extends BaseRecyclerViewAdapter.BaseViewHolder<DATA>, DATA>
        extends RecyclerView.Adapter<VH> implements Releasable {

    public static final int POSITION_TAG = R.id.position_tag;

    private Context context;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public abstract DATA getItemAtPosition(int position);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DATA data = getItemAtPosition(position);
        if (data != null) {
            holder.bindData(data, position);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        DATA data = getItemAtPosition(position);
        if (data != null) {
            holder.bindData(data, position, payloads);
        }
    }

    @CallSuper
    @Override
    public void release() {
        context = null;
    }

    public static abstract class BaseViewHolder<DATA> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(DATA data, int position);

        public void bindData(DATA data, int position, List<Object> payloads) {
            bindData(data, position);
        }
    }
}
