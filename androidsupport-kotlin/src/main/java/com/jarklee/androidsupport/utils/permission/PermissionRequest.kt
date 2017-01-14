/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission

internal class PermissionRequest private constructor(private val mActionOnGranted: Runnable?,
                                                     private val mActionOnCanceled: Runnable?,
                                                     private val mIsRequireAll: Boolean) {

    fun executeAction() {
        mActionOnGranted?.run()
    }

    fun executeCancel() {
        mActionOnCanceled?.run()
    }

    fun requireAll(): Boolean {
        return mIsRequireAll
    }

    companion object {

        fun newGrantOnly(actionOnGranted: Runnable?,
                         requireAll: Boolean): PermissionRequest {
            return PermissionRequest(actionOnGranted, null, requireAll)
        }

        fun newCancelOnly(actionOnCanceled: Runnable?,
                          requireAll: Boolean): PermissionRequest {
            return PermissionRequest(null, actionOnCanceled, requireAll)
        }

        fun newRequest(actionOnGranted: Runnable?,
                       actionOnCanceled: Runnable?,
                       requireAll: Boolean): PermissionRequest {
            return PermissionRequest(actionOnGranted, actionOnCanceled, requireAll)
        }
    }
}