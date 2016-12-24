/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.callback;

import com.jarklee.androidsupport.lifecycle.LifeCycleHook;

public interface LifeCycleListener {

    void onReady(LifeCycleHook hook);

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
