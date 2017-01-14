/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/13
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

final class PermissionRequest {

    private final Runnable mActionOnGranted;
    private final Runnable mActionOnCanceled;
    private final boolean mIsRequireAll;

    private PermissionRequest(final Runnable actionOnGranted,
                              final Runnable actionOnCanceled,
                              boolean requireAll) {
        this.mActionOnCanceled = actionOnCanceled;
        this.mActionOnGranted = actionOnGranted;
        this.mIsRequireAll = requireAll;
    }

    void executeAction() {
        if (mActionOnGranted != null) {
            mActionOnGranted.run();
        }
    }

    void executeCancel() {
        if (mActionOnCanceled != null) {
            mActionOnCanceled.run();
        }
    }

    boolean requireAll() {
        return mIsRequireAll;
    }

    static PermissionRequest newGrantOnly(final Runnable actionOnGranted,
                                          final boolean requireAll) {
        return new PermissionRequest(actionOnGranted, null, requireAll);
    }

    static PermissionRequest newCancelOnly(final Runnable actionOnCanceled,
                                           final boolean requireAll) {
        return new PermissionRequest(null, actionOnCanceled, requireAll);
    }

    static PermissionRequest newRequest(final Runnable actionOnGranted,
                                        final Runnable actionOnCanceled,
                                        final boolean requireAll) {
        return new PermissionRequest(actionOnGranted, actionOnCanceled, requireAll);
    }

}
