package com.client.business;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.client.common.IClient;
import com.client.common.IView;
import com.client.utils.SocketUtil;
import com.client.utils.UiUtil;
import com.client.views.DialogPage;
import com.client.views.InitPage;

public class TcpClient implements IClient {
	
	private InetSocketAddress socketAddress;
	private InetAddress address;
	private Socket socket;

	private ExecutorService pool;
	
	private IView view;
	
	private static TcpClient instance;
	
	private TcpClient() {
		pool = Executors.newFixedThreadPool(5);
	}
	
	public static TcpClient getInstance() {
		if(instance == null) {
			synchronized (TcpClient.class) {
				if(instance == null)	instance = new TcpClient();
			}
		}
		return instance;
	}


	@Override
	public void startConnect() {
		UiUtil.showDialogPage("正在连接，请稍等...", false);
		pool.execute(() -> {
			try {
				address = InetAddress.getByName(SocketUtil.HOST);
				socketAddress = new InetSocketAddress(address, SocketUtil.SERCER_POST);
				socket = new Socket();
				
				socket.connect(socketAddress, 4000);
				
				UiUtil.cancelDialogPage();
				UiUtil.cancelInitPage();
				UiUtil.showSentPage();	
			} catch (Exception e) {
				if(e instanceof IOException) {
					UiUtil.showDialogPage("连接失败，请检查服务端是否打开!", true);
				}
				if(e instanceof UnknownHostException) {
					UiUtil.showDialogPage("IP地址格式错误", true);
				}
				e.printStackTrace();
			}
		});
	}

	@Override
	public void interruptConnect() {
		pool.shutdownNow();
	}	
}