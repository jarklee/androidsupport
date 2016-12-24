/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.adapter;

import android.content.Context;
import android.os.Bundle;

import com.jarklee.androidsupport.callback.OnAdapterCallback;

public abstract class CallbackableAdapter<VH extends BaseRecyclerViewAdapter.BaseViewHolder<DATA>, DATA>
        extends BaseRecyclerViewAdapter<VH, DATA> {

    private OnAdapterCallback callback;

    public CallbackableAdapter(Context context) {
        super(context);
    }

    public void registerCallback(OnAdapterCallback callback) {
        this.callback = callback;
    }

    public void unregisterCallback() {
        callback = null;
    }

    public final void postEvent(int eventId) {
        postEvent(eventId, null);
    }

    public final void postEvent(int eventId, Bundle bundle) {
        if (callback != null) {
            callback.onAdapterEvent(eventId, bundle);
        }
    }

    @Override
    public void release() {
        super.release();
        unregisterCallback();
    }
}
