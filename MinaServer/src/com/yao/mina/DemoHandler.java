package com.yao.mina;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class DemoHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		String message = arg1.toString();
		arg0.write(new Date().toString());
		System.out.println("receive message: " + message);
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		System.out.println("messageSent");
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		System.out.println("sessionClosed");
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		System.out.println("sessionCreated");
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		System.out.println("sessionOpened");
	}

}
