/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle

import com.jarklee.androidsupport.callback.LifeCycleListener
import com.jarklee.androidsupport.lifecycle.LifeCycleHook

import java.util.LinkedList

internal class LifeCycleCollection : LifeCycleListener {

    private val mCycleList: MutableList<LifeCycleListener>

    init {
        mCycleList = LinkedList<LifeCycleListener>()
    }

    fun size(): Int {
        return mCycleList.size
    }

    fun add(listener: LifeCycleListener) {
        mCycleList.add(listener)
    }

    fun remove(listener: LifeCycleListener) {
        mCycleList.remove(listener)
    }

    private val cycleList: List<LifeCycleListener>
        get() = mCycleList

    override fun onReady(hook: LifeCycleHook) {
        //
    }

    override fun onCreate() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onCreate()
        }
    }

    override fun onStart() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onStart()
        }
    }

    override fun onResume() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onResume()
        }
    }

    override fun onPause() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onPause()
        }
    }

    override fun onStop() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onStop()
        }
    }

    override fun onDestroy() {
        val cycleList = cycleList
        for (lifeCycleListener in cycleList) {
            lifeCycleListener.onDestroy()
        }
    }
}
