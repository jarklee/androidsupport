/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

final class PermissionRequestRequireOneQuery extends PermissionsRequestBaseQuery {

    PermissionRequestRequireOneQuery(PermissionManager requestManager) {
        super(requestManager, false);
    }

    @Override
    public boolean satisfyToRequestedPermissions() {
        String[] requiredPermissions;
        if (mListPermissions == null) {
            requiredPermissions = null;
        } else {
            requiredPermissions = new String[mListPermissions.size()];
            mListPermissions.toArray(requiredPermissions);
        }
        return mRequestManager.getPermissionRequester().hasOnePermissions(requiredPermissions);
    }
}
