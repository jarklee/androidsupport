/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.annotation.CheckResult
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.support.annotation.UiThread
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog

import com.jarklee.androidsupport.annotation.BindServiceFlag
import com.jarklee.androidsupport.service.ServiceConnector

@CheckResult
fun Fragment.bindToService(serviceClass: Class<out Service>,
                           @BindServiceFlag flag: Int = Context.BIND_AUTO_CREATE): ServiceConnector {
    val serviceConnector = ServiceConnector(context, flag)
    serviceConnector.bindService(serviceClass)
    return serviceConnector
}

fun Fragment.sendBroadcast(intent: Intent) {
    activity.sendBroadcast(intent)
}

fun Fragment.registerBroadcast(receiver: BroadcastReceiver, filter: IntentFilter) {
    activity.registerReceiver(receiver, filter)
}

fun Fragment.unregisterReceiver(receiver: BroadcastReceiver) {
    try {
        activity.unregisterReceiver(receiver)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.navigateToActivity(activityClass: Class<out Activity>,
                                bundle: Bundle? = null,
                                flag: Int = 0) {
    val intent = Intent(activity, activityClass)
    intent.addFlags(flag)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

fun Fragment.navigateToActivityForResult(activityClass: Class<out Activity>,
                                         bundle: Bundle? = null,
                                         requestCode: Int,
                                         flags: Int = 0) {
    val intent = Intent(activity, activityClass)
    intent.addFlags(flags)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

fun Fragment.showAlert(title: String, message: String) {
    val context = context ?: return
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
}

abstract class AbsFragment : Fragment() {

    @UiThread
    @MainThread
    protected fun showProgress(@StringRes strRes: Int) {
        showProgress(getString(strRes))
    }

    private var mProgressDialog: ProgressDialog? = null

    @UiThread
    @MainThread
    protected fun showProgress(msg: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(activity, null, msg, false, true)
        }
        mProgressDialog?.setMessage(msg)
        mProgressDialog?.show()
    }

    @UiThread
    @MainThread
    protected fun hideProgress() {
        mProgressDialog?.dismiss()
    }
}
