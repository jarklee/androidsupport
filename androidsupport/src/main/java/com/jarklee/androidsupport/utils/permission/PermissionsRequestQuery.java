/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/14
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.utils.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;

public interface PermissionsRequestQuery {

    PermissionsRequestQuery addPermission(String permission);

    PermissionsRequestQuery addPermissions(Collection<String> permissions);

    PermissionsRequestQuery addPermissions(String... permissions);

    boolean satisfyToRequestedPermissions();

    void executeAction(@NonNull Runnable action,
                       @Nullable String explainMessage);

    void executeAction(@NonNull Runnable action,
                       boolean useDefaultCancelAction,
                       @Nullable String explainMessage);

    void executeAction(@NonNull Runnable action,
                       @Nullable Runnable cancelAction,
                       @Nullable String explainMessage);

    void executeAction(@NonNull Runnable action,
                       @Nullable Runnable cancelAction,
                       boolean useDefaultCancelAction,
                       @Nullable String explainMessage);

    void checkAndRequest(@Nullable Runnable cancelAction,
                         @Nullable String explainMessage);

    void checkAndRequest(@Nullable Runnable cancelAction,
                         boolean useDefaultCancelAction,
                         @Nullable String explainMessage);
}
