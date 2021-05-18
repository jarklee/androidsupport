/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.jarklee.androidsupport.service.ServiceConnector;
import com.jarklee.essential.annotation.BindServiceFlag;

public abstract class AbsFragment extends Fragment {

    @CheckResult
    protected final ServiceConnector bindToService(Class<? extends Service> serviceClass) {
        return bindToService(serviceClass, Context.BIND_AUTO_CREATE);
    }

    @CheckResult
    protected final ServiceConnector bindToService(Class<? extends Service> serviceClass,
                                                   @BindServiceFlag int flags) {
        ServiceConnector serviceConnector = new ServiceConnector(getContext(), flags);
        serviceConnector.bindService(serviceClass);
        return serviceConnector;
    }

    protected final void sendBroadcast(Intent intent) {
        getActivity().sendBroadcast(intent);
    }

    protected final void registerBroadcast(BroadcastReceiver receiver, IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    protected final void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activityClass) {
        navigateToActivity(activityClass, 0);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activityClass,
                                            @Nullable Bundle bundle) {
        navigateToActivity(activityClass, bundle, 0);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activityClass, int flag) {
        navigateToActivity(activityClass, null, flag);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activityClass,
                                            @Nullable Bundle bundle, int flags) {
        Intent intent = new Intent(getActivity(), activityClass);
        intent.addFlags(flags);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected final void navigateToActivityForResult(@NonNull Class<? extends Activity> activityClass,
                                                     int requestCode) {
        navigateToActivityForResult(activityClass, null, requestCode, 0);
    }

    protected final void navigateToActivityForResult(@NonNull Class<? extends Activity> activityClass,
                                                     @Nullable Bundle bundle, int requestCode) {
        navigateToActivityForResult(activityClass, bundle, requestCode, 0);
    }

    protected final void navigateToActivityForResult(@NonNull Class<? extends Activity> activityClass,
                                                     @Nullable Bundle bundle,
                                                     int requestCode,
                                                     int flags) {
        Intent intent = new Intent(getActivity(), activityClass);
        intent.addFlags(flags);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void showAlert(String title, String message) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @UiThread
    @MainThread
    protected void showProgress(@StringRes int strRes) {
        showProgress(getString(strRes));
    }

    private ProgressDialog mProgressDialog;

    @UiThread
    @MainThread
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(), null, msg, false, true);
        }
        mProgressDialog.setMessage(msg);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @UiThread
    @MainThread
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
