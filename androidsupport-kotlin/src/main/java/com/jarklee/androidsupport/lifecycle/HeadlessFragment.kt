/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle

import android.annotation.TargetApi
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jarklee.androidsupport.callback.LifeCycleListener
import com.jarklee.androidsupport.lifecycle.LifeCycleCollection
import com.jarklee.androidsupport.lifecycle.LifeCycleHook

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class HeadlessFragment : Fragment(), LifeCycleHook {

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context = getContext()
            }
        }
        if (context != null && context is FragmentActivity) {
            val manager = context.fragmentManager
            manager.beginTransaction().remove(this).commit()
        }
    }

    companion object {

        fun newInstance(listener: LifeCycleListener): HeadlessFragment {
            val fragment = HeadlessFragment()
            fragment.cycleListeners = LifeCycleCollection()
            fragment.addListener(listener)
            return fragment
        }
    }
}
