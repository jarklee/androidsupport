/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

final class PermissionRequesterFactory {

    private PermissionRequesterFactory() {
    }

    static IPermissionRequester create(Activity activity) {
        return new PermissionRequesterImpl(activity);
    }

    static IPermissionRequester create(android.support.v4.app.Fragment fragment) {
        return new PermissionRequesterImpl(fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    static IPermissionRequester create(android.app.Fragment fragment) {
        return new PermissionRequesterImpl(fragment);
    }

    static IPermissionRequester create(Context context) {
        return new PermissionRequesterImpl(context);
    }

}
