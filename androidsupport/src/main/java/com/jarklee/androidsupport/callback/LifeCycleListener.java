/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
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
