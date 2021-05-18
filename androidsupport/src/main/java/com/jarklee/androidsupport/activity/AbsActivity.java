/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jarklee.androidsupport.service.ServiceConnector;
import com.jarklee.essential.annotation.BindServiceFlag;
import com.jarklee.essential.common.helper.PermissionHelper;

public abstract class AbsActivity extends AppCompatActivity {

    @CheckResult
    protected final ServiceConnector bindToService(Class<? extends Service> serviceClass) {
        return bindToService(serviceClass, Context.BIND_AUTO_CREATE);
    }

    @CheckResult
    protected final ServiceConnector bindToService(Class<? extends Service> serviceClass,
                                                   @BindServiceFlag int flags) {
        ServiceConnector serviceConnector = new ServiceConnector(this, flags);
        serviceConnector.bindService(serviceClass);
        return serviceConnector;
    }

    protected final void navigateToActivity(Class<? extends Activity> activity) {
        navigateToActivity(activity, 0);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activity,
                                            @Nullable Bundle data) {
        navigateToActivity(activity, data, 0);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activity,
                                            int flags) {
        navigateToActivity(activity, null, flags);
    }

    protected final void navigateToActivity(@NonNull Class<? extends Activity> activity,
                                            @Nullable Bundle data, int flags) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(flags);
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    protected final void navigateToActivityForResult(Class<? extends Activity> activity, int requestCode) {
        navigateToActivityForResult(activity, null, requestCode, 0);
    }

    protected final void navigateToActivityForResult(Class<? extends Activity> activity,
                                                     @Nullable Bundle data,
                                                     int requestCode) {
        navigateToActivityForResult(activity, data, requestCode, 0);
    }

    protected final void navigateToActivityForResult(Class<? extends Activity> activity,
                                                     @Nullable Bundle data,
                                                     int requestCode,
                                                     int flags) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(flags);
        if (data != null) {
            intent.putExtras(data);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @MainThread
    @UiThread
    protected void showProgress(@StringRes int strRes) {
        showProgress(getString(strRes));
    }

    private ProgressDialog mProgressDialog;

    @MainThread
    @UiThread
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, null, msg, false, true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    @MainThread
    @UiThread
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    public void hideAllSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            decorView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    protected boolean hasPermissions(String... permissions) {
        return PermissionHelper.has(this, permissions);
    }

    protected void requestPermissions(int id, String... permissions) {
        PermissionHelper.request(this, id, permissions);
    }

    protected boolean isPermissionsGranted(int[] grantResults) {
        return PermissionHelper.isGranted(grantResults);
    }
}
