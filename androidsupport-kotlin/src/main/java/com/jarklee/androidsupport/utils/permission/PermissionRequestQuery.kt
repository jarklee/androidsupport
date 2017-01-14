/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission

import com.jarklee.androidsupport.ext.StringHelper
import java.util.*

interface PermissionsRequestQuery {

    fun addPermission(permission: String): PermissionsRequestQuery

    fun addPermissions(permissions: Collection<String>): PermissionsRequestQuery

    fun addPermissions(vararg permissions: String): PermissionsRequestQuery

    fun satisfyToRequestedPermissions(): Boolean

    fun executeAction(action: Runnable?,
                      explainMessage: String?)

    fun executeAction(action: Runnable?,
                      useDefaultCancelAction: Boolean,
                      explainMessage: String?)

    fun executeAction(action: Runnable?,
                      cancelAction: Runnable?,
                      explainMessage: String?)

    fun executeAction(action: Runnable?,
                      cancelAction: Runnable?,
                      useDefaultCancelAction: Boolean,
                      explainMessage: String?)

    fun checkAndRequest(cancelAction: Runnable?,
                        explainMessage: String?)

    fun checkAndRequest(cancelAction: Runnable?,
                        useDefaultCancelAction: Boolean,
                        explainMessage: String?)
}

internal abstract class PermissionsRequestBaseQuery(val mRequestManager: PermissionManager,
                                                    private val mIsRequireAll: Boolean) : PermissionsRequestQuery {
    var mListPermissions: MutableSet<String>? = null

    override fun addPermission(permission: String): PermissionsRequestQuery {
        initPermissionList()
        mListPermissions!!.add(permission)
        return this
    }

    override fun addPermissions(permissions: Collection<String>): PermissionsRequestQuery {
        initPermissionList()
        mListPermissions!!.addAll(permissions)
        return this
    }

    override fun addPermissions(vararg permissions: String): PermissionsRequestQuery {
        initPermissionList()
        mListPermissions!!.addAll(Arrays.asList(*permissions))
        return this
    }

    abstract override fun satisfyToRequestedPermissions(): Boolean

    private fun initPermissionList() {
        if (mListPermissions == null) {
            mListPermissions = LinkedHashSet<String>()
        }
    }

    override fun executeAction(action: Runnable?,
                               explainMessage: String?) {
        executeAction(action, false, explainMessage)
    }

    override fun executeAction(action: Runnable?,
                               useDefaultCancelAction: Boolean,
                               explainMessage: String?) {
        executeAction(action, null, useDefaultCancelAction, explainMessage)
    }

    override fun executeAction(action: Runnable?,
                               cancelAction: Runnable?,
                               explainMessage: String?) {
        executeAction(action, cancelAction, false, explainMessage)
    }

    override fun executeAction(action: Runnable?,
                               cancelAction: Runnable?,
                               useDefaultCancelAction: Boolean,
                               explainMessage: String?) {
        val permissionRequester = this.mRequestManager.permissionRequester
        val requirePermissions = mListPermissions?.toTypedArray()
        if (requirePermissions == null) {
            action?.run()
            return
        }
        if (satisfyToRequestedPermissions()) {
            action?.run()
            return
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run()
            } else if (useDefaultCancelAction && mRequestManager.defaultCancelAction != null) {
                mRequestManager.defaultCancelAction.run()
            }
            return
        }
        var canceler = cancelAction
        if (canceler == null && useDefaultCancelAction) {
            canceler = mRequestManager.defaultCancelAction
        }
        val request = PermissionRequest.newRequest(action, canceler, mIsRequireAll)
        val requestId = mRequestManager.obtainRequestId(action ?: request)
        if (permissionRequester.shouldShowExplainMessage() && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(mRequestManager,
                    explainMessage!!,
                    requestId,
                    request,
                    *requirePermissions)
            return
        }
        mRequestManager.putPermissionRequest(requestId, request)
        permissionRequester.requestPermissions(requestId, *requirePermissions)
    }

    override fun checkAndRequest(cancelAction: Runnable?,
                                 explainMessage: String?) {
        checkAndRequest(cancelAction, false, explainMessage)
    }

    override fun checkAndRequest(cancelAction: Runnable?,
                                 useDefaultCancelAction: Boolean,
                                 explainMessage: String?) {
        val permissionRequester = this.mRequestManager.permissionRequester
        val requirePermissions = mListPermissions?.toTypedArray() ?: return
        if (satisfyToRequestedPermissions()) {
            return
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run()
            } else if (useDefaultCancelAction && mRequestManager.defaultCancelAction != null) {
                mRequestManager.defaultCancelAction.run()
            }
            return
        }
        var canceler = cancelAction
        if (canceler == null && useDefaultCancelAction) {
            canceler = mRequestManager.defaultCancelAction
        }
        val request = PermissionRequest.newCancelOnly(canceler, mIsRequireAll)
        val requestId = mRequestManager.obtainRequestId(cancelAction ?: request)
        if (permissionRequester.shouldShowExplainMessage()
                && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(mRequestManager,
                    explainMessage!!,
                    requestId,
                    request,
                    *requirePermissions)
            return
        }
        mRequestManager.putPermissionRequest(requestId, request)
        permissionRequester.requestPermissions(requestId, *requirePermissions)
    }

    companion object {

        fun newRequireAllQuery(manager: PermissionManager): PermissionsRequestQuery {
            return PermissionRequestRequireAllQuery(manager)
        }

        fun newRequireOneQuery(manager: PermissionManager): PermissionsRequestQuery {
            return PermissionRequestRequireOneQuery(manager)
        }
    }
}

internal class PermissionRequestRequireAllQuery(requestManager: PermissionManager)
    : PermissionsRequestBaseQuery(requestManager, true) {

    override fun satisfyToRequestedPermissions(): Boolean {
        val requirePermissions = mListPermissions?.toTypedArray() ?: return true
        return mRequestManager.permissionRequester.hasPermissions(*requirePermissions)
    }
}

internal class PermissionRequestRequireOneQuery(requestManager: PermissionManager)
    : PermissionsRequestBaseQuery(requestManager, false) {

    override fun satisfyToRequestedPermissions(): Boolean {
        val requirePermissions = mListPermissions?.toTypedArray() ?: return true
        return mRequestManager.permissionRequester.hasOnePermissions(*requirePermissions)
    }
}