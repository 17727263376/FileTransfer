package com.client.business;

import java.io.*;
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
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private static TcpServer instance;
	
	private TcpServer(){
		pool = Executors.newFixedThreadPool(5);
	}
	
	public static TcpServer getInstance () {
		if(instance == null) {
			synchronized (TcpServer.class) {
				if(instance == null) {
					instance = new TcpServer();
				}
			}
		}
		return instance;
	}

	public void setView(IView view) {
		this.view = view;
	}
	
	/**
	 * 开启线程循环监听客户端的连接
	 */
	@Override
	public void startListen() throws IOException {
		ss = new ServerSocket(SocketUtil.SERCER_POST);
		pool.execute(() -> {
			while(true) {
				try {
					socket = ss.accept();
					inputStream = socket.getInputStream();
					outputStream = socket.getOutputStream();
					UiUtil.showReceivePage(socket.getInetAddress().getHostAddress());
					System.out.println("socket connect success!");
				} catch (IOException e) {
					System.out.println("socket connect fail!");
					e.printStackTrace();
					break;
				}
			}
		});
	}

	/**
	 * 关闭socket，没有中断端口监听
	 */
	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 向客户端发送是否接收文件信号
	 */
	@Override
	public void sendMessage(String msg) {
		try {
			DataOutputStream dos = new DataOutputStream(this.outputStream);
			dos.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
