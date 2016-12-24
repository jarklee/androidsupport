/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.ext

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment

fun Context.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(this, *permission)
}

fun Fragment.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(context, *permission)
}

fun Activity.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(this, id, *permission)
}

fun Fragment.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(activity, id, *permission)
}

fun isPermissionsGranted(grantedResults: IntArray?): Boolean {
    if (grantedResults == null) {
        return true
    }
    var granted = true
    for (grantedResult in grantedResults) {
        if (grantedResult != PackageManager.PERMISSION_GRANTED) {
            granted = false
            break
        }
    }
    return granted
}

object PermissionHelper {

    fun has(context: Context?, vararg permissions: String): Boolean {
        if (context == null) {
            return false
        }
        var granted = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false
                break
            }
        }
        return granted
    }

    fun request(activity: Activity?, requestID: Int, vararg permissions: String) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions, requestID)
        }
    }
}
