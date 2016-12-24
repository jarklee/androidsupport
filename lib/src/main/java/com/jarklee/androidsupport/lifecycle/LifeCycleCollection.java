/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.lifecycle;


import com.jarklee.androidsupport.callback.LifeCycleListener;

import java.util.LinkedList;
import java.util.List;

class LifeCycleCollection implements LifeCycleListener {

    private final List<LifeCycleListener> mCycleList;

    LifeCycleCollection() {
        mCycleList = new LinkedList<>();
    }

    int size() {
        return mCycleList.size();
    }

    void add(LifeCycleListener listener) {
        mCycleList.add(listener);
    }

    void remove(LifeCycleListener listener) {
        mCycleList.remove(listener);
    }

    private List<LifeCycleListener> getCycleList() {
        return mCycleList;
    }

    @Override
    public void onReady(LifeCycleHook hook) {
        //
    }

    @Override
    public void onCreate() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onCreate();
        }
    }

    @Override
    public void onStart() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onStart();
        }
    }

    @Override
    public void onResume() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onResume();
        }
    }

    @Override
    public void onPause() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        List<LifeCycleListener> cycleList = getCycleList();
        for (LifeCycleListener lifeCycleListener : cycleList) {
            lifeCycleListener.onDestroy();
        }
    }
}
