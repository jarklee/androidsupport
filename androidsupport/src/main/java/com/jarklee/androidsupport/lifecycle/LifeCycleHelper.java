/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.jarklee.androidsupport.callback.LifeCycleListener;

public class LifeCycleHelper {

    private static final String LIFE_CYCLE_TAG = "life_cycle_tag";

    public static void injectLifeCycle(Context context, LifeCycleListener listener) {
        if (context == null || listener == null) {
            return;
        }
        if (context instanceof FragmentActivity) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                attachHoneyComb((FragmentActivity) context, listener);
            } else {
                attachGingerBread((FragmentActivity) context, listener);
            }
            return;
        }
        throw new IllegalArgumentException("context must be extend from FragmentActivity");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void attachHoneyComb(FragmentActivity activity, LifeCycleListener listener) {
        android.app.FragmentManager manager = activity.getFragmentManager();
        HeadlessFragment fragment = (HeadlessFragment) manager.findFragmentByTag(LIFE_CYCLE_TAG);
        if (fragment != null) {
            fragment.addListener(listener);
        } else {
            fragment = HeadlessFragment.newInstance(listener);
            manager.beginTransaction()
                    .add(fragment, LIFE_CYCLE_TAG)
                    .commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void attachGingerBread(FragmentActivity activity, LifeCycleListener listener) {
        FragmentManager manager = activity.getSupportFragmentManager();
        HeadlessFragmentCompat fragment = (HeadlessFragmentCompat) manager.findFragmentByTag(LIFE_CYCLE_TAG);
        if (fragment != null) {
            fragment.addListener(listener);
        } else {
            fragment = HeadlessFragmentCompat.newInstance(listener);
            manager.beginTransaction()
                    .add(fragment, LIFE_CYCLE_TAG)
                    .commit();
        }
    }

}
