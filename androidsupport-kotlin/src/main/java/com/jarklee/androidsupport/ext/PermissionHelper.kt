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
import android.support.v7.app.AlertDialog
import com.jarklee.androidsupport.R
import java.util.*

fun Context.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(this, *permission)
}

fun android.support.v4.app.Fragment.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(context, *permission)
}

private fun android.app.Fragment.hasPermissions(vararg permission: String): Boolean {
    return PermissionHelper.has(context, *permission)
}

fun Activity.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(this, id, *permission)
}

fun android.support.v4.app.Fragment.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(activity, id, *permission)
}

fun android.app.Fragment.requestPermissions(id: Int, vararg permission: String) {
    PermissionHelper.request(activity, id, *permission)
}

fun isPermissionsGranted(grantedResults: IntArray?): Boolean {
    if (grantedResults == null) {
        return true
    }
    val granted = grantedResults.none { it != PackageManager.PERMISSION_GRANTED }
    return granted
}

object PermissionHelper {

    fun has(context: Context?, vararg permissions: String): Boolean {
        if (context == null) {
            return false
        }
        val granted = permissions.none {
            ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        return granted
    }

    fun request(activity: Activity?, requestID: Int, vararg permissions: String) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions, requestID)
        }
    }
}

private fun getPermissionRequester(activity: Activity): PermissionManager.IPermissionRequester {
    return PermissionManager.PermissionRequesterImpl(activity)
}

private fun getPermissionRequester(fragment: android.support.v4.app.Fragment): PermissionManager.IPermissionRequester {
    return PermissionManager.PermissionRequesterImpl(fragment)
}

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
private fun getPermissionRequester(fragment: android.app.Fragment): PermissionManager.IPermissionRequester {
    return PermissionManager.PermissionRequesterImpl(fragment)
}

private fun getPermissionRequester(context: Context): PermissionManager.IPermissionRequester {
    return PermissionManager.PermissionRequesterImpl(context)
}

class PermissionManager(private val mPermissionRequester: PermissionManager.IPermissionRequester,
                        private val mDefaultCancelAction: Runnable? = null) {

    private val mPermissionRequests: MutableMap<Int, PermissionRequest>

    constructor(activity: Activity,
                defaultCancelAction: Runnable? = null)
            : this(getPermissionRequester(activity), defaultCancelAction) {
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    constructor(fragment: android.app.Fragment,
                defaultCancelAction: Runnable? = null)
            : this(getPermissionRequester(fragment), defaultCancelAction) {
    }

    constructor(fragment: android.support.v4.app.Fragment,
                defaultCancelAction: Runnable? = null)
            : this(getPermissionRequester(fragment), defaultCancelAction) {
    }

    constructor(context: Context,
                defaultCancelAction: Runnable? = null)
            : this(getPermissionRequester(context), defaultCancelAction) {
    }

    init {
        this.mPermissionRequests = HashMap<Int, PermissionRequest>()
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<out String>,
                                   grantResults: IntArray) {
        val permissionRequest = mPermissionRequests.remove(requestCode) ?: return
        if (isPermissionsGranted(grantResults)) {
            permissionRequest.executeAction()
        } else {
            permissionRequest.executeCancel()
        }
    }

    fun executeAction(action: Runnable,
                      explainMessage: String?,
                      vararg requirePermissions: String) {
        executeAction(action, false, explainMessage, *requirePermissions)
    }

    fun executeAction(action: Runnable,
                      useDefaultCancelAction: Boolean,
                      explainMessage: String?,
                      vararg requirePermissions: String) {
        executeAction(action, if (useDefaultCancelAction) mDefaultCancelAction else null,
                explainMessage, *requirePermissions)
    }

    fun executeAction(action: Runnable,
                      cancelAction: Runnable?,
                      explainMessage: String?,
                      vararg requirePermissions: String) {
        executeAction(action, cancelAction, false, explainMessage, *requirePermissions)
    }

    fun executeAction(action: Runnable,
                      cancelAction: Runnable?,
                      useDefaultCancelAction: Boolean,
                      explainMessage: String?,
                      vararg requirePermissions: String) {
        val permissionRequester = this.mPermissionRequester
        if (permissionRequester.hasPermissions(*requirePermissions)) {
            action.run()
            return
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run()
            } else if (useDefaultCancelAction && mDefaultCancelAction != null) {
                mDefaultCancelAction.run()
            }
            return
        }
        val requestId = action.hashCode()
        val request = PermissionRequest(action,
                cancelAction ?: if (useDefaultCancelAction) mDefaultCancelAction else null)
        if (permissionRequester.shouldShowExplainMessage() && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(this,
                    explainMessage,
                    requestId,
                    request,
                    *requirePermissions)
            return
        }
        mPermissionRequests.put(requestId, request)
        permissionRequester.requestPermissions(requestId, *requirePermissions)
    }

    interface IPermissionRequester {
        fun requestPermissions(requestId: Int, vararg permissions: String)

        fun hasPermissions(vararg requirePermissions: String): Boolean

        fun shouldRequestPermissions(): Boolean

        fun shouldShowExplainMessage(vararg requirePermissions: String): Boolean

        fun showExplainMessage(permissionManager: PermissionManager,
                               explainMessage: String?,
                               requestId: Int,
                               request: PermissionRequest,
                               vararg requirePermissions: String)
    }

    internal class PermissionRequesterImpl internal constructor(private val activity: Activity?,
                                                                private val fragment1: android.app.Fragment?,
                                                                private val fragment2: android.support.v4.app.Fragment?,
                                                                private val context: Context?) : IPermissionRequester {

        internal constructor(activity: Activity) : this(activity, null, null, null) {
        }

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        internal constructor(fragment: android.app.Fragment) : this(null, fragment, null, null) {
        }

        internal constructor(fragment: android.support.v4.app.Fragment) : this(null, null, fragment, null) {
        }

        internal constructor(context: Context) : this(null, null, null, context) {
        }

        @Suppress("IfThenToSafeAccess")
        override fun requestPermissions(requestId: Int, vararg permissions: String) {
            if (activity != null) {
                activity.requestPermissions(requestId, *permissions)
                return
            }
            if (fragment1 != null) {
                fragment1.requestPermissions(requestId, *permissions)
                return
            }
            if (fragment2 != null) {
                fragment2.requestPermissions(requestId, *permissions)
            }
        }

        override fun hasPermissions(vararg requirePermissions: String): Boolean {
            if (activity != null) {
                return activity.hasPermissions(*requirePermissions)
            }
            if (fragment1 != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    return fragment1.hasPermissions(*requirePermissions)
                }
            }
            if (fragment2 != null) {
                return fragment2.hasPermissions(*requirePermissions)
            }
            return context != null && PermissionHelper.has(context, *requirePermissions)
        }

        override fun shouldRequestPermissions(): Boolean {
            return activity != null || fragment1 != null || fragment2 != null
        }

        override fun shouldShowExplainMessage(vararg requirePermissions: String): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return false
            }
            var activity = this.activity
            if (activity == null && fragment1 != null) {
                activity = fragment1.activity
            }
            if (activity == null && fragment2 != null) {
                activity = fragment2.activity
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
                                        explainMessage: String?,
                                        requestId: Int,
                                        request: PermissionRequest,
                                        vararg requirePermissions: String) {
            AlertDialog.Builder(getContext())
                    .setMessage(explainMessage)
                    .setPositiveButton(R.string.button_accept, { dialog, which ->
                        permissionManager.mPermissionRequests.put(requestId, request)
                        requestPermissions(requestId, *requirePermissions)
                    })
                    .setNegativeButton(R.string.button_cancel, { dialog, which -> request.executeCancel() })
                    .setOnCancelListener({ request.executeCancel() }).show()
        }

        private fun getContext(): Context {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return activity ?: if (fragment1 == null)
                    fragment2!!.context
                else
                    fragment1.activity
            }
            return activity ?: fragment2!!.context
        }
    }

    class PermissionRequest internal constructor(private val actionOnGranted: Runnable?,
                                                 private val actionOnCanceled: Runnable?) {

        fun executeAction() {
            actionOnGranted?.run()
        }

        fun executeCancel() {
            actionOnCanceled?.run()
        }
    }
}
