/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.jarklee.androidsupport.callback.LifeCycleListener;

public class HeadlessFragmentCompat extends Fragment implements LifeCycleHook {

    private LifeCycleCollection cycleListeners;

    public static HeadlessFragmentCompat newInstance(LifeCycleListener listener) {
        HeadlessFragmentCompat fragment = new HeadlessFragmentCompat();
        fragment.cycleListeners = new LifeCycleCollection();
        fragment.addListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycleListeners.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        cycleListeners.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        cycleListeners.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cycleListeners.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        cycleListeners.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cycleListeners.onDestroy();
    }

    public void addListener(LifeCycleListener listener) {
        cycleListeners.add(listener);
        listener.onReady(this);
    }

    @Override
    public void detachListener(LifeCycleListener listener) {
        cycleListeners.remove(listener);
        if (cycleListeners.size() != 0) {
            return;
        }
        Context context = getActivity();
        if (context == null) {
            context = getContext();
        }
        if (context != null && context instanceof FragmentActivity) {
            FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
            manager.beginTransaction()
                    .remove(this)
                    .commit();
        }
    }
}
