package com.yao.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaService {

	public static void main(String[] args) {

		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());// 添加日志过滤器
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new DemoHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			acceptor.bind(new InetSocketAddress("192.168.90.137",9123));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
