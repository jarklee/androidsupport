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
import com.jarklee.androidsupport.service.IServiceTask
import java.util.*

class ServiceConnector constructor(private val context: Context,
                                   @BindServiceFlag private val flag: Int = Context.BIND_AUTO_CREATE)
: ServiceConnection {

    private var messenger: Messenger? = null
    private val pendingTasks: MutableList<IServiceTask>

    init {
        pendingTasks = Vector<IServiceTask>()
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        messenger = Messenger(service)
        synchronized(pendingTasks) {
            for (pendingTask in pendingTasks) {
                pendingTask.execute(messenger!!)
            }
            pendingTasks.clear()
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        messenger = null
        synchronized(pendingTasks) {
            pendingTasks.clear()
        }
    }

    fun bindService(serviceClass: Class<out Service>) {
        context.bindToService(serviceClass, this, flag)
    }

    fun unbindService() {
        context.unbindService(this)
    }

    fun enqueueTask(task: IServiceTask?) {
        if (task != null) {
            if (messenger != null) {
                task.execute(messenger!!)
            } else {
                synchronized(pendingTasks) {
                    pendingTasks.add(task)
                }
            }
        }
    }

    fun removeTask(task: IServiceTask) {
        synchronized(pendingTasks) {
            pendingTasks.remove(task)
        }
    }
}
