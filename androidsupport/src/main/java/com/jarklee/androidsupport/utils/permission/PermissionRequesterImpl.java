/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/13
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;

import com.jarklee.androidsupport.R;
import com.jarklee.essential.common.helper.PermissionHelper;

final class PermissionRequesterImpl implements IPermissionRequester {
    private final Activity mActivity;
    private final android.app.Fragment mFragment;
    private final android.support.v4.app.Fragment mSupportFragment;
    private final Context mContext;

    private PermissionRequesterImpl(Activity activity,
                                    android.app.Fragment fragment,
                                    android.support.v4.app.Fragment supportFragment,
                                    Context context) {
        this.mContext = context;
        this.mActivity = activity;
        this.mSupportFragment = supportFragment;
        this.mFragment = fragment;
    }

    PermissionRequesterImpl(Activity activity) {
        this(activity, null, null, null);
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    PermissionRequesterImpl(android.app.Fragment fragment) {
        this(null, fragment, null, null);
    }

    PermissionRequesterImpl(android.support.v4.app.Fragment fragment) {
        this(null, null, fragment, null);
    }

    PermissionRequesterImpl(Context context) {
        this(null, null, null, context);
    }

    @Override
    public void requestPermissions(int requestId, String... permissions) {
        if (mActivity != null) {
            PermissionHelper.request(mActivity, requestId, permissions);
            return;
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                PermissionHelper.request(mFragment, requestId, permissions);
                return;
            }
        }
        if (mSupportFragment != null) {
            PermissionHelper.request(mSupportFragment, requestId, permissions);
        }
    }

    @Override
    public boolean hasPermissions(String[] requirePermissions) {
        if (mActivity != null) {
            return PermissionHelper.has(mActivity, requirePermissions);
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return PermissionHelper.has(mFragment, requirePermissions);
            }
        }
        if (mSupportFragment != null) {
            return PermissionHelper.has(mSupportFragment, requirePermissions);
        }
        return mContext != null && PermissionHelper.has(mContext, requirePermissions);
    }

    @Override
    public boolean hasOnePermissions(String[] requirePermissions) {
        if (mActivity != null) {
            return PermissionHelper.hasOne(mActivity, requirePermissions);
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return PermissionHelper.hasOne(mFragment, requirePermissions);
            }
        }
        if (mSupportFragment != null) {
            return PermissionHelper.hasOne(mSupportFragment, requirePermissions);
        }
        return mContext != null && PermissionHelper.hasOne(mContext, requirePermissions);
    }

    @Override
    public boolean shouldRequestPermissions() {
        return mActivity != null || mFragment != null || mSupportFragment != null;
    }

    @Override
    public boolean shouldShowExplainMessage(String... requirePermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        Activity activity = this.mActivity;
        if (activity == null && mFragment != null) {
            activity = mFragment.getActivity();
        }
        if (activity == null && mSupportFragment != null) {
            activity = mSupportFragment.getActivity();
        }
        if (activity == null) {
            return false;
        }
        for (String requirePermission : requirePermissions) {
            if (activity.shouldShowRequestPermissionRationale(requirePermission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showExplainMessage(final PermissionManager manager,
                                   final String explainMessage,
                                   final int requestId,
                                   final PermissionRequest request,
                                   final String... requirePermissions) {
        new AlertDialog.Builder(getContext())
                .setMessage(explainMessage)
                .setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.putPermissionRequest(requestId, request);
                        requestPermissions(requestId, requirePermissions);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.executeCancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        request.executeCancel();
                    }
                }).show();
    }

    private Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return mActivity == null ? mFragment == null
                    ? mSupportFragment.getContext()
                    : mFragment.getActivity() : mActivity;
        }
        return mActivity == null ? mSupportFragment.getContext()
                : mActivity;
    }
}
