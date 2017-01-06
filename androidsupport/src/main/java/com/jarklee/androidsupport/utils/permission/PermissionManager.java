/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2017/1/6
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

import java.util.HashMap;
import java.util.Map;

public final class PermissionManager {

    private final Map<Integer, PermissionRequest> mPermissionRequests;
    private final Runnable mDefaultCancelAction;

    private final IPermissionRequester mPermissionRequester;

    public PermissionManager(Activity activity) {
        this(getPermissionRequester(activity), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(android.app.Fragment fragment) {
        this(getPermissionRequester(fragment), null);
    }

    public PermissionManager(android.support.v4.app.Fragment fragment) {
        this(getPermissionRequester(fragment), null);
    }

    public PermissionManager(Context context) {
        this(getPermissionRequester(context), null);
    }

    public PermissionManager(Activity activity,
                             Runnable defaultCancelAction) {
        this(getPermissionRequester(activity), defaultCancelAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(android.app.Fragment fragment,
                             Runnable defaultCancelAction) {
        this(getPermissionRequester(fragment), defaultCancelAction);
    }

    public PermissionManager(android.support.v4.app.Fragment fragment,
                             Runnable defaultCancelAction) {
        this(getPermissionRequester(fragment), defaultCancelAction);
    }

    public PermissionManager(Context context,
                             Runnable defaultCancelAction) {
        this(getPermissionRequester(context), defaultCancelAction);
    }

    public PermissionManager(IPermissionRequester mPermissionRequester) {
        this(mPermissionRequester, null);
    }

    public PermissionManager(IPermissionRequester permissionRequester, Runnable defaultCancelAction) {
        this.mDefaultCancelAction = defaultCancelAction;
        this.mPermissionRequests = new HashMap<>();
        this.mPermissionRequester = permissionRequester;
    }

    public final void onRequestPermissionsResult(final int requestCode,
                                                 final @NonNull String[] permissions,
                                                 final @NonNull int[] grantResults) {
        final PermissionRequest permissionRequest = mPermissionRequests.remove(requestCode);
        if (permissionRequest == null) {
            return;
        }
        if (PermissionHelper.isGranted(grantResults)) {
            permissionRequest.executeAction();
        } else {
            permissionRequest.executeCancel();
        }
    }

    public final void executeAction(@NonNull final Runnable action,
                                    final String... requirePermissions) {
        executeAction(action, false, requirePermissions);
    }

    public final void executeAction(@NonNull final Runnable action,
                                    final boolean useDefaultCancelAction,
                                    final String... requirePermissions) {
        executeAction(action, useDefaultCancelAction ? mDefaultCancelAction : null,
                requirePermissions);
    }

    public final void executeAction(@NonNull final Runnable action,
                                    @Nullable final Runnable cancelAction,
                                    final String... requirePermissions) {
        executeAction(action, cancelAction, false, requirePermissions);
    }

    public final void executeAction(@NonNull final Runnable action,
                                    @Nullable final Runnable cancelAction,
                                    final boolean useDefaultCancelAction,
                                    final String... requirePermissions) {
        IPermissionRequester permissionRequester = this.mPermissionRequester;
        if (permissionRequester.hasPermissions(requirePermissions)) {
            action.run();
            return;
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run();
            } else if (useDefaultCancelAction && mDefaultCancelAction != null) {
                mDefaultCancelAction.run();
            }
            return;
        }
        final int requestId = action.hashCode();
        final PermissionRequest request = new PermissionRequest(action,
                cancelAction == null ? useDefaultCancelAction ? mDefaultCancelAction : null : cancelAction);
        mPermissionRequests.put(requestId, request);
        permissionRequester.requestPermissions(requestId, requirePermissions);
    }

    private static IPermissionRequester getPermissionRequester(Activity activity) {
        return new PermissionRequesterImpl(activity);
    }

    private static IPermissionRequester getPermissionRequester(android.support.v4.app.Fragment fragment) {
        return new PermissionRequesterImpl(fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private static IPermissionRequester getPermissionRequester(android.app.Fragment fragment) {
        return new PermissionRequesterImpl(fragment);
    }

    private static IPermissionRequester getPermissionRequester(Context context) {
        return new PermissionRequesterImpl(context);
    }

    private interface IPermissionRequester {
        void requestPermissions(int requestId, String... permissions);

        boolean hasPermissions(String[] requirePermissions);

        boolean shouldRequestPermissions();
    }

    private static final class PermissionRequesterImpl implements IPermissionRequester {

        private final Activity activity;
        private final android.app.Fragment fragment1;
        private final android.support.v4.app.Fragment fragment2;
        private Context context;

        PermissionRequesterImpl(Activity activity) {
            this(activity, null, null, null);
        }

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        PermissionRequesterImpl(android.app.Fragment fragment) {
            this(null, fragment, null, null);
        }

        PermissionRequesterImpl(android.support.v4.app.Fragment fragment) {
            this(null, null, fragment, null);
        }

        PermissionRequesterImpl(Context context) {
            this(null, null, null, context);
        }

        PermissionRequesterImpl(Activity activity,
                                android.app.Fragment fragment1,
                                android.support.v4.app.Fragment fragment2,
                                Context context) {
            this.activity = activity;
            this.fragment1 = fragment1;
            this.fragment2 = fragment2;
            this.context = context;
        }

        @Override
        public void requestPermissions(int requestId, String... permissions) {
            if (activity != null) {
                PermissionHelper.request(activity, requestId, permissions);
                return;
            }
            if (fragment1 != null) {
                PermissionHelper.request(fragment1, requestId, permissions);
                return;
            }
            if (fragment2 != null) {
                PermissionHelper.request(fragment2, requestId, permissions);
            }
        }

        @Override
        public boolean hasPermissions(String[] requirePermissions) {
            if (activity != null) {
                return PermissionHelper.has(activity, requirePermissions);
            }
            if (fragment1 != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    return PermissionHelper.has(fragment1, requirePermissions);
                }
            }
            if (fragment2 != null) {
                return PermissionHelper.has(fragment2, requirePermissions);
            }
            if (context != null) {
                return PermissionHelper.has(context, requirePermissions);
            }
            return false;
        }

        @Override
        public boolean shouldRequestPermissions() {
            return activity != null || fragment1 != null || fragment2 != null;
        }
    }

    private static final class PermissionRequest {

        private final Runnable actionOnGranted;
        private final Runnable actionOnCanceled;

        public PermissionRequest(final Runnable actionOnGranted,
                                 final Runnable actionOnCanceled) {
            this.actionOnGranted = actionOnGranted;
            this.actionOnCanceled = actionOnCanceled;
        }

        void executeAction() {
            if (actionOnGranted != null) {
                actionOnGranted.run();
            }
        }

        void executeCancel() {
            if (actionOnCanceled != null) {
                actionOnCanceled.run();
            }
        }
    }
}
