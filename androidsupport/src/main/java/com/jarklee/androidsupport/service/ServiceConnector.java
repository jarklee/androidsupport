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
    private ServiceConnectorDelegate connectionCallback;
    private final List<IServiceTask> pendingTasks;
    private final Context context;
    private final Class<? extends Service> serviceClass;
    private final int flag;
    private final Lock pendingTasksLock = new ReentrantLock();

    public ServiceConnector(Context context, Class<? extends Service> serviceClass) {
        this(context, serviceClass, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnector(Context context, Class<? extends Service> serviceClass,
                            @BindServiceFlag int flag) {
        pendingTasks = new Vector<>();
        this.context = context;
        this.serviceClass = serviceClass;
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
        if (connectionCallback != null) {
            connectionCallback.onMessageServiceReady(name, service);
        }
    }

    @Override
    public final void onServiceDisconnected(ComponentName name) {
        if (connectionCallback != null) {
            connectionCallback.onMessageServiceTerminal(name);
        }
        pendingTasksLock.lock();
        try {
            messenger = null;
            pendingTasks.clear();
        } finally {
            pendingTasksLock.unlock();
        }
    }

    public final void bindService() {
        bindService(null);
    }

    public final void bindService(ServiceConnectorDelegate connectionCallback) {
        this.connectionCallback = connectionCallback;
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

    public interface ServiceConnectorDelegate {
        void onMessageServiceReady(ComponentName name, IBinder service);

        void onMessageServiceTerminal(ComponentName name);
    }
}
