/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/13
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

interface IPermissionRequester {
    void requestPermissions(int requestId, String... permissions);

    boolean hasPermissions(String[] requirePermissions);

    boolean hasOnePermissions(String[] requirePermissions);

    boolean shouldRequestPermissions();

    boolean shouldShowExplainMessage(String... requirePermissions);

    void showExplainMessage(PermissionManager permissionManager,
                            String explainMessage,
                            int requestId,
                            PermissionRequest request,
                            String... requirePermissions);
}
