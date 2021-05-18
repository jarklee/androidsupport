/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/13
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import androidx.annotation.Nullable;

import com.jarklee.essential.common.helper.StringHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

abstract class PermissionsRequestBaseQuery implements PermissionsRequestQuery {

    final PermissionManager mRequestManager;
    private final boolean mIsRequireAll;
    Set<String> mListPermissions;

    PermissionsRequestBaseQuery(PermissionManager requestManager, boolean requireAll) {
        this.mRequestManager = requestManager;
        this.mIsRequireAll = requireAll;
    }

    @Override
    public PermissionsRequestQuery addPermission(String permission) {
        initPermissionList();
        mListPermissions.add(permission);
        return this;
    }

    @Override
    public PermissionsRequestQuery addPermissions(Collection<String> permissions) {
        initPermissionList();
        mListPermissions.addAll(permissions);
        return this;
    }

    @Override
    public PermissionsRequestQuery addPermissions(String... permissions) {
        initPermissionList();
        mListPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public abstract boolean satisfyToRequestedPermissions();

    private void initPermissionList() {
        if (mListPermissions == null) {
            mListPermissions = new LinkedHashSet<>();
        }
    }

    @Override
    public final void executeAction(@Nullable final Runnable action,
                                    @Nullable final String explainMessage) {
        executeAction(action, false, explainMessage);
    }

    @Override
    public final void executeAction(@Nullable final Runnable action,
                                    final boolean useDefaultCancelAction,
                                    @Nullable final String explainMessage) {
        executeAction(action, null, useDefaultCancelAction, explainMessage);
    }

    @Override
    public final void executeAction(@Nullable final Runnable action,
                                    @Nullable final Runnable cancelAction,
                                    @Nullable final String explainMessage) {
        executeAction(action, cancelAction, false, explainMessage);
    }

    @Override
    public final void executeAction(@Nullable final Runnable action,
                                    @Nullable final Runnable cancelAction,
                                    final boolean useDefaultCancelAction,
                                    @Nullable final String explainMessage) {
        IPermissionRequester permissionRequester = this.mRequestManager.getPermissionRequester();
        String[] requirePermissions;
        if (mListPermissions == null) {
            requirePermissions = null;
        } else {
            requirePermissions = new String[mListPermissions.size()];
            mListPermissions.toArray(requirePermissions);
        }
        if (satisfyToRequestedPermissions()) {
            if (action != null) {
                action.run();
            }
            return;
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run();
            } else if (useDefaultCancelAction && mRequestManager.getDefaultCancelAction() !=
                    null) {
                mRequestManager.getDefaultCancelAction().run();
            }
            return;
        }

        Runnable canceler = cancelAction;
        if (canceler == null && useDefaultCancelAction) {
            canceler = mRequestManager.getDefaultCancelAction();
        }
        final PermissionRequest request = PermissionRequest.newRequest(action, canceler, mIsRequireAll);
        final int requestId = mRequestManager.obtainRequestId(action == null ? request : action);
        if (permissionRequester.shouldShowExplainMessage()
                && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(mRequestManager,
                    explainMessage,
                    requestId,
                    request,
                    requirePermissions);
            return;
        }
        mRequestManager.putPermissionRequest(requestId, request);
        permissionRequester.requestPermissions(requestId, requirePermissions);
    }

    @Override
    public final void checkAndRequest(final @Nullable Runnable cancelAction,
                                      final @Nullable String explainMessage) {
        checkAndRequest(cancelAction, false, explainMessage);
    }

    @Override
    public final void checkAndRequest(final @Nullable Runnable cancelAction,
                                      final boolean useDefaultCancelAction,
                                      final @Nullable String explainMessage) {
        IPermissionRequester permissionRequester = this.mRequestManager.getPermissionRequester();
        String[] requirePermissions;
        if (mListPermissions == null) {
            requirePermissions = null;
        } else {
            requirePermissions = new String[mListPermissions.size()];
            mListPermissions.toArray(requirePermissions);
        }
        if (satisfyToRequestedPermissions()) {
            return;
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run();
            } else if (useDefaultCancelAction && mRequestManager.getDefaultCancelAction() != null) {
                mRequestManager.getDefaultCancelAction().run();
            }
            return;
        }
        Runnable canceler = cancelAction;
        if (canceler == null && useDefaultCancelAction) {
            canceler = mRequestManager.getDefaultCancelAction();
        }
        final PermissionRequest request = PermissionRequest.newCancelOnly(canceler, mIsRequireAll);
        final int requestId = mRequestManager.obtainRequestId(cancelAction == null ? request : cancelAction);
        if (permissionRequester.shouldShowExplainMessage()
                && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(mRequestManager,
                    explainMessage,
                    requestId,
                    request,
                    requirePermissions);
            return;
        }
        mRequestManager.putPermissionRequest(requestId, request);
        permissionRequester.requestPermissions(requestId, requirePermissions);
    }

    static PermissionsRequestQuery newRequireAllQuery(PermissionManager manager) {
        return new PermissionRequestRequireAllQuery(manager);
    }

    static PermissionsRequestQuery newRequireOneQuery(PermissionManager manager) {
        return new PermissionRequestRequireOneQuery(manager);
    }
}
