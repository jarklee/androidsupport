/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle;

import com.jarklee.androidsupport.callback.LifeCycleListener;

public interface LifeCycleHook {

    void detachListener(LifeCycleListener listener);
}
