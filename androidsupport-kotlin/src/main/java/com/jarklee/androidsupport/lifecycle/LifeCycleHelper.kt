/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.jarklee.androidsupport.callback.LifeCycleListener

object LifeCycleHelper {

    private val LIFE_CYCLE_TAG = "life_cycle_tag"

    fun injectLifeCycle(activity: Activity?, listener: LifeCycleListener?) {
        if (activity == null || listener == null) {
            return
        }
        if (activity is FragmentActivity) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                attachHoneyComb(activity, listener)
            } else {
                attachGingerBread(activity, listener)
            }
            return
        }
        throw IllegalArgumentException("activity must be extend from FragmentActivity")
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun attachHoneyComb(activity: FragmentActivity, listener: LifeCycleListener) {
        val manager = activity.fragmentManager
        var fragment: HeadlessFragment? =
            manager.findFragmentByTag(LIFE_CYCLE_TAG) as HeadlessFragment
        if (fragment != null) {
            fragment.addListener(listener)
        } else {
            fragment = HeadlessFragment.newInstance(listener)
            manager.beginTransaction().add(fragment, LIFE_CYCLE_TAG).commit()
        }
    }

    private fun attachGingerBread(activity: FragmentActivity, listener: LifeCycleListener) {
        val manager = activity.supportFragmentManager
        var fragment: HeadlessFragmentCompat? =
            manager.findFragmentByTag(LIFE_CYCLE_TAG) as HeadlessFragmentCompat
        if (fragment != null) {
            fragment.addListener(listener)
        } else {
            fragment = HeadlessFragmentCompat.newInstance(listener)
            manager.beginTransaction().add(fragment, LIFE_CYCLE_TAG).commit()
        }
    }

}
