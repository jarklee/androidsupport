/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle;

import com.jarklee.androidsupport.callback.LifeCycleListener;

public interface LifeCycleHook {

    void detachListener(LifeCycleListener listener);
}
