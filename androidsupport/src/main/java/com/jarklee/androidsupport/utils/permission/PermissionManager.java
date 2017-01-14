/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/13
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.jarklee.essential.common.helper.PermissionHelper;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PermissionManager {

    private final Map<Integer, PermissionRequest> mPermissionRequests;
    private final Runnable mDefaultCancelAction;
    private final IPermissionRequester mPermissionRequester;

    public PermissionManager(@NonNull Activity activity) {
        this(PermissionRequesterFactory.create(activity));
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(@NonNull android.app.Fragment fragment) {
        this(PermissionRequesterFactory.create(fragment));
    }

    public PermissionManager(@NonNull android.support.v4.app.Fragment fragment) {
        this(PermissionRequesterFactory.create(fragment));
    }

    public PermissionManager(@NonNull Context context) {
        this(PermissionRequesterFactory.create(context));
    }

    public PermissionManager(@NonNull Activity activity,
                             @Nullable Runnable defaultCancelAction) {
        this(PermissionRequesterFactory.create(activity), defaultCancelAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(@NonNull android.app.Fragment fragment,
                             @Nullable Runnable defaultCancelAction) {
        this(PermissionRequesterFactory.create(fragment), defaultCancelAction);
    }

    public PermissionManager(@NonNull android.support.v4.app.Fragment fragment,
                             @Nullable Runnable defaultCancelAction) {
        this(PermissionRequesterFactory.create(fragment), defaultCancelAction);
    }

    public PermissionManager(@NonNull Context context,
                             @Nullable Runnable defaultCancelAction) {
        this(PermissionRequesterFactory.create(context), defaultCancelAction);
    }

    private PermissionManager(@NonNull IPermissionRequester mPermissionRequester) {
        this(mPermissionRequester, null);
    }

    private PermissionManager(@NonNull IPermissionRequester permissionRequester,
                              @Nullable Runnable defaultCancelAction) {
        this.mDefaultCancelAction = defaultCancelAction;
        this.mPermissionRequests = new LinkedHashMap<>();
        this.mPermissionRequester = permissionRequester;
    }

    public final void onRequestPermissionsResult(final int requestCode,
                                                 final @NonNull String[] permissions,
                                                 final @NonNull int[] grantResults) {
        final PermissionRequest permissionRequest = mPermissionRequests.remove(requestCode);
        if (permissionRequest == null) {
            return;
        }
        if (permissionRequest.requireAll()) {
            if (PermissionHelper.isGranted(grantResults)) {
                permissionRequest.executeAction();
            } else {
                permissionRequest.executeCancel();
            }
        } else {
            if (PermissionHelper.isOneGranted(grantResults)) {
                permissionRequest.executeAction();
            } else {
                permissionRequest.executeCancel();
            }
        }
    }

    public PermissionsRequestQuery requireAll() {
        return PermissionsRequestBaseQuery.newRequireAllQuery(this);
    }

    public PermissionsRequestQuery requireOne() {
        return PermissionsRequestBaseQuery.newRequireOneQuery(this);
    }

    public PermissionsRequestQuery requireAll(Collection<String> permissions) {
        PermissionsRequestQuery requestQuery = PermissionsRequestBaseQuery.newRequireAllQuery(this);
        requestQuery.addPermissions(permissions);
        return requestQuery;
    }

    public PermissionsRequestQuery requireOne(Collection<String> permissions) {
        PermissionsRequestQuery requestQuery = PermissionsRequestBaseQuery.newRequireOneQuery(this);
        requestQuery.addPermissions(permissions);
        return requestQuery;
    }

    public PermissionsRequestQuery requireAll(String... permissions) {
        PermissionsRequestQuery requestQuery = PermissionsRequestBaseQuery.newRequireAllQuery(this);
        requestQuery.addPermissions(permissions);
        return requestQuery;
    }

    public PermissionsRequestQuery requireOne(String... permissions) {
        PermissionsRequestQuery requestQuery = PermissionsRequestBaseQuery.newRequireOneQuery(this);
        requestQuery.addPermissions(permissions);
        return requestQuery;
    }

    IPermissionRequester getPermissionRequester() {
        return mPermissionRequester;
    }

    void putPermissionRequest(final int requestId,
                              final PermissionRequest request) {
        mPermissionRequests.put(requestId, request);
    }

    Runnable getDefaultCancelAction() {
        return mDefaultCancelAction;
    }

    int obtainRequestId(Object o) {
        int hash = (o == null ? 0 : o.hashCode()) & 0xffff;
        while (mPermissionRequests.containsKey(hash)) {
            hash++;
            if (hash >= 0xffff) {
                hash = 0;
            }
        }
        return hash;
    }
}
