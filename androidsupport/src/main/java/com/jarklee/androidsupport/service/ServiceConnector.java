/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

import com.jarklee.essential.annotation.BindServiceFlag;
import com.jarklee.essential.common.helper.ServiceHelper;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceConnector implements ServiceConnection {

    private Messenger messenger;
    private final List<IServiceTask> pendingTasks;
    private final Context context;
    private final int flag;
    private final Lock pendingTasksLock = new ReentrantLock();

    public ServiceConnector(Context context) {
        this(context, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnector(Context context,
                            @BindServiceFlag int flag) {
        pendingTasks = new Vector<>();
        this.context = context;
        this.flag = flag;
    }

    @Override
    public final void onServiceConnected(ComponentName name, IBinder service) {
        pendingTasksLock.lock();
        try {
            messenger = new Messenger(service);
            performPendingTasks();
        } finally {
            pendingTasksLock.unlock();
        }
    }

    @Override
    public final void onServiceDisconnected(ComponentName name) {
        pendingTasksLock.lock();
        try {
            messenger = null;
            pendingTasks.clear();
        } finally {
            pendingTasksLock.unlock();
        }
    }

    public final void bindService(Class<? extends Service> serviceClass) {
        ServiceHelper.bindToService(getContext(), serviceClass, this, flag);
    }

    public final void unbindService() {
        getContext().unbindService(this);
    }

    private Context getContext() {
        return context;
    }

    public final void enqueueTask(IServiceTask task) {
        if (task == null) {
            return;
        }
        Messenger messenger = this.messenger;
        if (null != messenger) {
            task.execute(messenger);
        } else {
            pendingTasksLock.lock();
            try {
                pendingTasks.add(task);
                performPendingTasks();
            } finally {
                pendingTasksLock.unlock();
            }
        }
    }

    public final void removeTask(IServiceTask task) {
        if (task == null) {
            return;
        }
        pendingTasksLock.lock();
        try {
            pendingTasks.remove(task);
        } finally {
            pendingTasksLock.unlock();
        }
    }

    private void performPendingTasks() {
        Messenger messenger = this.messenger;
        if (messenger == null) {
            return;
        }
        pendingTasksLock.lock();
        try {
            for (IServiceTask pendingTask : pendingTasks) {
                pendingTask.execute(messenger);
            }
            pendingTasks.clear();
        } finally {
            pendingTasksLock.unlock();
        }
    }
}
