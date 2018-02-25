package com.yao.minaproject.minatest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @Description:
 * @Author: YaoPaine
 * @CreateDate: 2018/2/24 下午8:14
 * @Version:
 */

public class MinaService extends Service {

    private ConnectionThread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        thread = new ConnectionThread("mina", getApplicationContext());
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.disconnect();
        thread = null;
    }

    /**
     * 负责调用connection manager类中的方法完成与服务器的连接
     */
    class ConnectionThread extends HandlerThread {

        private boolean isConnection;
        private ConnectionManager mManager;

        public ConnectionThread(String name, Context context) {
            super(name);
            ConnectionConfig config = new ConnectionConfig
                    .Builder(context)
                    .setIp("192.168.90.137")
                    .setPort(9123)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000)
                    .build();
            mManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            for (; ; ) {
                isConnection = mManager.connect();//完成服务器连接
                if (isConnection) {
                    break;
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
            }
        }

        public void disconnect() {
            mManager.disconnect();
        }
    }
}
