/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle

import com.jarklee.androidsupport.callback.LifeCycleListener

interface LifeCycleHook {

    fun removeLifeCycleHook(listener: LifeCycleListener)
}
