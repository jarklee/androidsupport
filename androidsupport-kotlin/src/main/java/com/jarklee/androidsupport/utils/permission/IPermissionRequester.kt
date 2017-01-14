/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import com.jarklee.androidsupport.R
import com.jarklee.androidsupport.ext.*

internal interface IPermissionRequester {
    fun requestPermissions(requestId: Int, vararg permissions: String)

    fun hasPermissions(vararg requirePermissions: String): Boolean

    fun hasOnePermissions(vararg requirePermissions: String): Boolean

    fun shouldRequestPermissions(): Boolean

    fun shouldShowExplainMessage(vararg requirePermissions: String): Boolean

    fun showExplainMessage(permissionManager: PermissionManager,
                           explainMessage: String,
                           requestId: Int,
                           request: PermissionRequest,
                           vararg requirePermissions: String)
}

internal class PermissionRequesterImpl internal constructor(private val mActivity: Activity?,
                                                            private val mFragment: android.app.Fragment?,
                                                            private val mSupportFragment: android.support.v4.app.Fragment?,
                                                            private val mContext: Context?) : IPermissionRequester {

    constructor(activity: Activity) : this(activity, null, null, null)

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(fragment: android.app.Fragment) : this(null, fragment, null, null)

    constructor(fragment: android.support.v4.app.Fragment) : this(null, null, fragment, null)

    constructor(context: Context) : this(null, null, null, context)

    override fun requestPermissions(requestId: Int, vararg permissions: String) {
        if (mActivity != null) {
            PermissionHelper.request(mActivity, requestId, *permissions)
            return
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mFragment.requestPermissions(requestId, *permissions)
                return
            }
        }
        mSupportFragment?.requestPermissions(requestId, *permissions)
    }

    override fun hasPermissions(vararg requirePermissions: String): Boolean {
        if (mActivity != null) {
            return PermissionHelper.has(mActivity, *requirePermissions)
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return mFragment.hasPermissions(*requirePermissions)
            }
        }
        if (mSupportFragment != null) {
            return mSupportFragment.hasPermissions(*requirePermissions)
        }
        return mContext != null && PermissionHelper.has(mContext, *requirePermissions)
    }

    override fun hasOnePermissions(vararg requirePermissions: String): Boolean {
        if (mActivity != null) {
            return PermissionHelper.hasOne(mActivity, *requirePermissions)
        }
        if (mFragment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return mFragment.hasOnePermissions(*requirePermissions)
            }
        }
        if (mSupportFragment != null) {
            return mSupportFragment.hasOnePermissions(*requirePermissions)
        }
        return mContext != null && PermissionHelper.hasOne(mContext, *requirePermissions)
    }

    override fun shouldRequestPermissions(): Boolean {
        return mActivity != null || mFragment != null || mSupportFragment != null
    }

    override fun shouldShowExplainMessage(vararg requirePermissions: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        var activity = this.mActivity
        if (activity == null && mFragment != null) {
            activity = mFragment.activity
        }
        if (activity == null && mSupportFragment != null) {
            activity = mSupportFragment.activity
        }
        if (activity == null) {
            return false
        }
        for (requirePermission in requirePermissions) {
            if (activity.shouldShowRequestPermissionRationale(requirePermission)) {
                return true
            }
        }
        return false
    }

    override fun showExplainMessage(permissionManager: PermissionManager,
                                    explainMessage: String,
                                    requestId: Int,
                                    request: PermissionRequest,
                                    vararg requirePermissions: String) {
        AlertDialog.Builder(context)
                .setMessage(explainMessage)
                .setPositiveButton(R.string.button_accept) { dialog, which ->
                    permissionManager.putPermissionRequest(requestId, request)
                    requestPermissions(requestId, *requirePermissions)
                }
                .setNegativeButton(R.string.button_cancel) { dialog, which -> request.executeCancel() }
                .setOnCancelListener { request.executeCancel() }.show()
    }

    private val context: Context
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return mActivity ?: if (mFragment == null)
                    mSupportFragment!!.context
                else
                    mFragment.activity
            }
            return mActivity ?: mSupportFragment!!.context
        }
}