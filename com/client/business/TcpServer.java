package com.client.business;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.client.common.IServer;
import com.client.common.IView;
import com.client.utils.SocketUtil;
import com.client.utils.UiUtil;

public class TcpServer implements IServer {
	private ServerSocket ss;
	private Socket socket;
	private ExecutorService pool;
	private IView view;
	
	public TcpServer() throws IOException {
		ss = new ServerSocket(SocketUtil.SERCER_POST);
		pool = Executors.newFixedThreadPool(5);
	}


	@Override
	public void startListen() {
		pool.execute(() -> {
			while(true) {
				try {
					socket = ss.accept();
					UiUtil.showReceivePage();
					System.out.println("socket connect success!");
				} catch (IOException e) {
					System.out.println("socket connect fail!");
					e.printStackTrace();
					break;
				}
			}
		});
	}
	
	
	
	
	
	
}
