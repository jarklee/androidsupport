/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.adapter

import android.os.Bundle
import com.jarklee.androidsupport.callback.OnAdapterCallback
import com.jarklee.androidsupport.common.Releasable

interface CallbackableAdapterPlugin : Releasable {

    var callback: OnAdapterCallback?

    fun registerCallback(callback: OnAdapterCallback) {
        this.callback = callback
    }

    fun unregisterCallback() {
        callback = null
    }

    fun postEvent(eventId: Int, bundle: Bundle? = null) {
        callback?.onAdapterEvent(eventId, bundle)
    }

    override fun release() {
        unregisterCallback()
    }
}
