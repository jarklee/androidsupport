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
import com.jarklee.androidsupport.ext.isOnePermissionsGranted
import com.jarklee.androidsupport.ext.isPermissionsGranted
import java.util.*

internal fun getPermissionRequester(activity: Activity): IPermissionRequester {
    return PermissionRequesterImpl(activity)
}

internal fun getPermissionRequester(fragment: android.support.v4.app.Fragment): IPermissionRequester {
    return PermissionRequesterImpl(fragment)
}

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
internal fun getPermissionRequester(fragment: android.app.Fragment): IPermissionRequester {
    return PermissionRequesterImpl(fragment)
}

internal fun getPermissionRequester(context: Context): IPermissionRequester {
    return PermissionRequesterImpl(context)
}

class PermissionManager private constructor(internal val permissionRequester: IPermissionRequester,
                                            internal val defaultCancelAction: Runnable?) {

    private val mPermissionRequests: MutableMap<Int, PermissionRequest>

    constructor(activity: Activity) : this(getPermissionRequester(activity))

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    constructor(fragment: android.app.Fragment) : this(getPermissionRequester(fragment))

    constructor(fragment: android.support.v4.app.Fragment) : this(getPermissionRequester(fragment))

    constructor(context: Context) : this(getPermissionRequester(context))

    constructor(activity: Activity,
                defaultCancelAction: Runnable?) : this(getPermissionRequester(activity), defaultCancelAction)

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    constructor(fragment: android.app.Fragment,
                defaultCancelAction: Runnable?) : this(getPermissionRequester(fragment), defaultCancelAction)

    constructor(fragment: android.support.v4.app.Fragment,
                defaultCancelAction: Runnable?) : this(getPermissionRequester(fragment), defaultCancelAction)

    constructor(context: Context,
                defaultCancelAction: Runnable?) : this(getPermissionRequester(context), defaultCancelAction)

    private constructor(mPermissionRequester: IPermissionRequester) : this(mPermissionRequester, null)

    init {
        this.mPermissionRequests = LinkedHashMap<Int, PermissionRequest>()
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        val permissionRequest = mPermissionRequests.remove(requestCode) ?: return
        if (permissionRequest.requireAll()) {
            if (isPermissionsGranted(grantResults)) {
                permissionRequest.executeAction()
            } else {
                permissionRequest.executeCancel()
            }
        } else {
            if (isOnePermissionsGranted(grantResults)) {
                permissionRequest.executeAction()
            } else {
                permissionRequest.executeCancel()
            }
        }
    }

    fun requireAll(): PermissionsRequestQuery {
        return PermissionsRequestBaseQuery.newRequireAllQuery(this)
    }

    fun requireOne(): PermissionsRequestQuery {
        return PermissionsRequestBaseQuery.newRequireOneQuery(this)
    }

    fun requireAll(permissions: Collection<String>): PermissionsRequestQuery {
        val requestQuery = PermissionsRequestBaseQuery.newRequireAllQuery(this)
        requestQuery.addPermissions(permissions)
        return requestQuery
    }

    fun requireOne(permissions: Collection<String>): PermissionsRequestQuery {
        val requestQuery = PermissionsRequestBaseQuery.newRequireOneQuery(this)
        requestQuery.addPermissions(permissions)
        return requestQuery
    }

    fun requireAll(vararg permissions: String): PermissionsRequestQuery {
        val requestQuery = PermissionsRequestBaseQuery.newRequireAllQuery(this)
        requestQuery.addPermissions(*permissions)
        return requestQuery
    }

    fun requireOne(vararg permissions: String): PermissionsRequestQuery {
        val requestQuery = PermissionsRequestBaseQuery.newRequireOneQuery(this)
        requestQuery.addPermissions(*permissions)
        return requestQuery
    }

    internal fun putPermissionRequest(requestId: Int,
                                      request: PermissionRequest) {
        mPermissionRequests.put(requestId, request)
    }

    internal fun obtainRequestId(any: Any?): Int {
        var hash = (any?.hashCode() ?: 0) and 0xffff
        while (mPermissionRequests.containsKey(hash)) {
            hash++
            if (hash >= 0xffff) {
                hash = 0
            }
        }
        return hash
    }
}


