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
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat

fun Context.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(this, *permission)
}

fun android.support.v4.app.Fragment.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(context, *permission)
}

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
fun android.app.Fragment.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(activity, *permission)
}

fun Context.hasOnePermissions(vararg permission: String): Boolean {
    return PermissionHelper.hasOne(this, *permission)
}

fun android.support.v4.app.Fragment.hasOnePermissions(vararg permission: String): Boolean {
    return PermissionHelper.hasOne(context, *permission)
}

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
fun android.app.Fragment.hasOnePermissions(vararg permission: String): Boolean {
    return PermissionHelper.hasOne(activity, *permission)
}

fun Activity.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(this, id, *permission)
}

fun android.support.v4.app.Fragment.requestPermissions(id: Int, vararg permission: String) {
    this.requestPermissions(permission, id)
}

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
fun android.app.Fragment.requestPermissions(id: Int, vararg permission: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.requestPermissions(permission, id)
    }
}

fun isPermissionsGranted(grantedResults: IntArray?): Boolean {
    if (grantedResults == null) {
        return true
    }
    return grantedResults.none { it != PackageManager.PERMISSION_GRANTED }
}

fun isOnePermissionsGranted(grantedResults: IntArray?): Boolean {
    if (grantedResults == null) {
        return true
    }
    return grantedResults.contains(PackageManager.PERMISSION_GRANTED)
}

object PermissionHelper {

    fun has(context: Context?, vararg permissions: String): Boolean {
        if (context == null) {
            return false
        }
        return permissions.none {
            ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasOne(context: Context?, vararg permissions: String): Boolean {
        if (context == null) {
            return false
        }
        return permissions.any {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun request(activity: Activity?, requestID: Int, vararg permissions: String) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions, requestID)
        }
    }
}

