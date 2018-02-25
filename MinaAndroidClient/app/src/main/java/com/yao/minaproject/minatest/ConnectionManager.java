package com.yao.minaproject.minatest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author: YaoPaine
 * @CreateDate: 2018/2/24 下午8:11
 * @Version:
 */

public class ConnectionManager {
    public static final String BROADCAST_ACTION = "com.yao.mina.broadcast";
    public static final String MESSAGE = "message";

    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnector;
    private IoSession mSession;
    private InetSocketAddress mAddress;


    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<>(config.getContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnector = new NioSocketConnector();
        mConnector.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        mConnector.getFilterChain().addLast("logger", new LoggingFilter());
        mConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        mConnector.setHandler(new DefaultHandler(mConfig.getContext()));
    }

    public boolean connect() {
        try {
            ConnectFuture connectFuture = mConnector.connect(mAddress);
            connectFuture.awaitUninterruptibly();
            mSession = connectFuture.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return mSession != null;
    }

    public void disconnect() {
        mConnector.dispose();
        mConnector = null;
        mSession = null;
        mAddress = null;
        mContext = null;
    }

    private static class DefaultHandler extends IoHandlerAdapter {
        private Context mContext;

        public DefaultHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            //将session保存到我们的session manager中，从而可以发送消息到服务器
            SessionManager.getInstance().setSession(session);
            System.out.println("sessionOpened");
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
            if (mContext != null) {
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(MESSAGE, message.toString());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);//局部广播
            }
            System.out.println("messageReceived");
        }


    }
}
