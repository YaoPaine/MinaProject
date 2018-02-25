package com.yao.minaproject.minatest;

import org.apache.mina.core.session.IoSession;

/**
 * @Description:
 * @Author: YaoPaine
 * @CreateDate: 2018/2/24 下午8:14
 * @Version:
 */

public class SessionManager {

    private static class ManagerHolder {

        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return ManagerHolder.INSTANCE;
    }

    public SessionManager() {

    }

    private IoSession mSession;

    public void setSession(IoSession ioSession) {
        mSession = ioSession;
    }

    /**
     * 将对象写服务器
     *
     * @param message
     */
    public void writeToServer(Object message) {
        if (mSession != null) {
            mSession.write(message);
        }
    }

    public void colseSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        mSession = null;
    }
}
