/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Messenger
import com.jarklee.androidsupport.annotation.BindServiceFlag
import com.jarklee.androidsupport.ext.bindToService
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class ServiceConnector constructor(private val context: Context,
                                   @BindServiceFlag private val flag: Int = Context.BIND_AUTO_CREATE)
    : ServiceConnection {

    private var messenger: Messenger? = null
    private var pendingTasks: MutableList<IServiceTask>
    private val pendingTasksLock = ReentrantLock()

    init {
        pendingTasks = Vector<IServiceTask>()
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        pendingTasksLock.lock()
        try {
            messenger = Messenger(service)
            performPendingTasks()
        } finally {
            pendingTasksLock.unlock()
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        pendingTasksLock.lock()
        try {
            messenger = null
            pendingTasks.clear()
        } finally {
            pendingTasksLock.unlock()
        }
    }

    fun bindService(serviceClass: Class<out Service>) {
        context.bindToService(serviceClass, this, flag)
    }

    fun unbindService() {
        getContext().unbindService(this)
    }

    private fun getContext(): Context {
        return context
    }

    fun enqueueTask(task: IServiceTask?) {
        if (task == null) {
            return
        }
        val messenger = this.messenger
        if (null != messenger) {
            task.execute(messenger)
        } else {
            pendingTasksLock.lock()
            try {
                pendingTasks.add(task)
                performPendingTasks()
            } finally {
                pendingTasksLock.unlock()
            }
        }
    }

    fun removeTask(task: IServiceTask?) {
        if (task == null) {
            return
        }
        pendingTasksLock.lock()
        try {
            pendingTasks.remove(task)
        } finally {
            pendingTasksLock.unlock()
        }
    }

    private fun performPendingTasks() {
        val messenger = this.messenger ?: return
        pendingTasksLock.lock()
        try {
            for (pendingTask in pendingTasks) {
                pendingTask.execute(messenger)
            }
            pendingTasks.clear()
        } finally {
            pendingTasksLock.unlock()
        }
    }
}
