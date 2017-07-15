/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission

internal class PermissionRequest private constructor(private val mActionOnGranted: PermissionActionCallback?,
                                                     private val mActionOnCanceled: PermissionActionCallback?,
                                                     private val mIsRequireAll: Boolean) {

    fun executeAction() {
        mActionOnGranted?.invoke()
    }

    fun executeCancel() {
        mActionOnCanceled?.invoke()
    }

    fun requireAll(): Boolean {
        return mIsRequireAll
    }

    companion object {

        fun newGrantOnly(actionOnGranted: PermissionActionCallback?,
                         requireAll: Boolean): PermissionRequest {
            return PermissionRequest(actionOnGranted, null, requireAll)
        }

        fun newCancelOnly(actionOnCanceled: PermissionActionCallback?,
                          requireAll: Boolean): PermissionRequest {
            return PermissionRequest(null, actionOnCanceled, requireAll)
        }

        fun newRequest(actionOnGranted: PermissionActionCallback?,
                       actionOnCanceled: PermissionActionCallback?,
                       requireAll: Boolean): PermissionRequest {
            return PermissionRequest(actionOnGranted, actionOnCanceled, requireAll)
        }
    }
}