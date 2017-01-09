/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;

import com.jarklee.androidsupport.R;
import com.jarklee.essential.common.helper.PermissionHelper;
import com.jarklee.essential.common.helper.StringHelper;

import java.util.HashMap;
import java.util.Map;

public final class PermissionManager {

    private final Map<Integer, PermissionRequest> mPermissionRequests;
    private final Runnable mDefaultCancelAction;

    private final IPermissionRequester mPermissionRequester;

    public PermissionManager(@NonNull Activity activity) {
        this(getPermissionRequester(activity), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(@NonNull android.app.Fragment fragment) {
        this(getPermissionRequester(fragment), null);
    }

    public PermissionManager(@NonNull android.support.v4.app.Fragment fragment) {
        this(getPermissionRequester(fragment), null);
    }

    public PermissionManager(@NonNull Context context) {
        this(getPermissionRequester(context), null);
    }

    public PermissionManager(@NonNull Activity activity,
                             @Nullable Runnable defaultCancelAction) {
        this(getPermissionRequester(activity), defaultCancelAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PermissionManager(@NonNull android.app.Fragment fragment,
                             @Nullable Runnable defaultCancelAction) {
        this(getPermissionRequester(fragment), defaultCancelAction);
    }

    public PermissionManager(@NonNull android.support.v4.app.Fragment fragment,
                             @Nullable Runnable defaultCancelAction) {
        this(getPermissionRequester(fragment), defaultCancelAction);
    }

    public PermissionManager(@NonNull Context context,
                             @Nullable Runnable defaultCancelAction) {
        this(getPermissionRequester(context), defaultCancelAction);
    }

    public PermissionManager(@NonNull IPermissionRequester mPermissionRequester) {
        this(mPermissionRequester, null);
    }

    public PermissionManager(@NonNull IPermissionRequester permissionRequester,
                             @Nullable Runnable defaultCancelAction) {
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

    public final PermissionManager executeAction(@NonNull final Runnable action,
                                                 @Nullable final String explainMessage,
                                                 final String... requirePermissions) {
        return executeAction(action, false, explainMessage, requirePermissions);
    }

    public final PermissionManager executeAction(@NonNull final Runnable action,
                                                 final boolean useDefaultCancelAction,
                                                 @Nullable final String explainMessage,
                                                 final String... requirePermissions) {
        return executeAction(action, useDefaultCancelAction ? mDefaultCancelAction : null,
                explainMessage, requirePermissions);
    }

    public final PermissionManager executeAction(@NonNull final Runnable action,
                                                 @Nullable final Runnable cancelAction,
                                                 @Nullable final String explainMessage,
                                                 final String... requirePermissions) {
        return executeAction(action, cancelAction, false, explainMessage, requirePermissions);
    }

    public final PermissionManager executeAction(@NonNull final Runnable action,
                                                 @Nullable final Runnable cancelAction,
                                                 final boolean useDefaultCancelAction,
                                                 @Nullable final String explainMessage,
                                                 final String... requirePermissions) {
        IPermissionRequester permissionRequester = this.mPermissionRequester;
        if (permissionRequester.hasPermissions(requirePermissions)) {
            action.run();
            return this;
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run();
            } else if (useDefaultCancelAction && mDefaultCancelAction != null) {
                mDefaultCancelAction.run();
            }
            return this;
        }
        final int requestId = action.hashCode();
        final PermissionRequest request = new PermissionRequest(action,
                cancelAction == null ? useDefaultCancelAction ? mDefaultCancelAction : null : cancelAction);
        if (permissionRequester.shouldShowExplainMessage()
                && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(this,
                    explainMessage,
                    requestId,
                    request,
                    requirePermissions);
            return this;
        }
        mPermissionRequests.put(requestId, request);
        permissionRequester.requestPermissions(requestId, requirePermissions);
        return this;
    }

    public final PermissionManager checkAndRequest(final @Nullable Runnable cancelAction,
                                                   final @Nullable String explainMessage,
                                                   final String... requirePermissions) {
        return checkAndRequest(cancelAction, false, explainMessage, requirePermissions);
    }

    public final PermissionManager checkAndRequest(final @Nullable Runnable cancelAction,
                                                   final boolean useDefaultCancelAction,
                                                   final @Nullable String explainMessage,
                                                   String... requirePermissions) {
        IPermissionRequester permissionRequester = this.mPermissionRequester;
        if (permissionRequester.hasPermissions(requirePermissions)) {
            return this;
        }
        if (!permissionRequester.shouldRequestPermissions()) {
            if (cancelAction != null) {
                cancelAction.run();
            } else if (useDefaultCancelAction && mDefaultCancelAction != null) {
                mDefaultCancelAction.run();
            }
            return this;
        }
        final PermissionRequest request = new PermissionRequest(null,
                cancelAction == null ? useDefaultCancelAction ? mDefaultCancelAction : null : cancelAction);
        final int requestId = cancelAction == null ? request.hashCode() : cancelAction.hashCode();
        if (permissionRequester.shouldShowExplainMessage()
                && !StringHelper.isEmpty(explainMessage)) {
            permissionRequester.showExplainMessage(this,
                    explainMessage,
                    requestId,
                    request,
                    requirePermissions);
            return this;
        }
        mPermissionRequests.put(requestId, request);
        permissionRequester.requestPermissions(requestId, requirePermissions);
        return this;
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

    interface IPermissionRequester {
        void requestPermissions(int requestId, String... permissions);

        boolean hasPermissions(String[] requirePermissions);

        boolean shouldRequestPermissions();

        boolean shouldShowExplainMessage(String... requirePermissions);

        void showExplainMessage(PermissionManager permissionManager,
                                String explainMessage,
                                int requestId,
                                PermissionRequest request,
                                String... requirePermissions);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    PermissionHelper.request(fragment1, requestId, permissions);
                }
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
            return context != null && PermissionHelper.has(context, requirePermissions);
        }

        @Override
        public boolean shouldRequestPermissions() {
            return activity != null || fragment1 != null || fragment2 != null;
        }

        @Override
        public boolean shouldShowExplainMessage(String... requirePermissions) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return false;
            }
            Activity activity = this.activity;
            if (activity == null && fragment1 != null) {
                activity = fragment1.getActivity();
            }
            if (activity == null && fragment2 != null) {
                activity = fragment2.getActivity();
            }
            if (activity == null) {
                return false;
            }
            for (String requirePermission : requirePermissions) {
                if (activity.shouldShowRequestPermissionRationale(requirePermission)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void showExplainMessage(final PermissionManager manager,
                                       final String explainMessage,
                                       final int requestId,
                                       final PermissionRequest request,
                                       final String... requirePermissions) {
            new AlertDialog.Builder(getContext())
                    .setMessage(explainMessage)
                    .setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            manager.mPermissionRequests.put(requestId, request);
                            requestPermissions(requestId, requirePermissions);
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            request.executeCancel();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            request.executeCancel();
                        }
                    }).show();
        }

        private Context getContext() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return activity == null ? fragment1 == null
                        ? fragment2.getContext()
                        : fragment1.getActivity() : activity;
            }
            return activity == null ? fragment2.getContext()
                    : activity;
        }
    }

    public static final class PermissionRequest {

        private final Runnable actionOnGranted;
        private final Runnable actionOnCanceled;

        private PermissionRequest(final Runnable actionOnGranted,
                                  final Runnable actionOnCanceled) {
            this.actionOnGranted = actionOnGranted;
            this.actionOnCanceled = actionOnCanceled;
        }

        public void executeAction() {
            if (actionOnGranted != null) {
                actionOnGranted.run();
            }
        }

        public void executeCancel() {
            if (actionOnCanceled != null) {
                actionOnCanceled.run();
            }
        }
    }
}
