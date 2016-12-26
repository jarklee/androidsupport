/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.jarklee.androidsupport.callback.LifeCycleListener

class HeadlessFragmentCompat : Fragment(), LifeCycleHook {

    private lateinit var cycleListeners: LifeCycleCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cycleListeners.onCreate()
    }

    override fun onStart() {
        super.onStart()
        cycleListeners.onStart()
    }

    override fun onResume() {
        super.onResume()
        cycleListeners.onResume()
    }

    override fun onPause() {
        super.onPause()
        cycleListeners.onPause()
    }

    override fun onStop() {
        super.onStop()
        cycleListeners.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cycleListeners.onDestroy()
    }

    fun addListener(listener: LifeCycleListener) {
        cycleListeners.add(listener)
        listener.onReady(this)
    }

    override fun removeLifeCycleHook(listener: LifeCycleListener) {
        cycleListeners.remove(listener)
        if (cycleListeners.size() != 0) {
            return
        }
        var context: Context? = activity
        if (context == null) {
            context = getContext()
        }
        if (context != null && context is FragmentActivity) {
            val manager = context.supportFragmentManager
            manager.beginTransaction().remove(this).commit()
        }
    }

    companion object {

        fun newInstance(listener: LifeCycleListener): HeadlessFragmentCompat {
            val fragment = HeadlessFragmentCompat()
            fragment.cycleListeners = LifeCycleCollection()
            fragment.addListener(listener)
            return fragment
        }
    }
}
