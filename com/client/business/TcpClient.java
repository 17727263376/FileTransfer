package com.client.business;

import java.io.IOException;
import java.io.*;
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

	private InputStream inputStream;
	private OutputStream outputStream;
	
	private ExecutorService pool;
	
	private IView view;
	
	private static TcpClient instance;
	
	private TcpClient() {
		pool = Executors.newFixedThreadPool(5);
	}
	
	public void setView(IView view) {
		this.view = view;
	}
	
	public static TcpClient getInstance() {
		if(instance == null) {
			synchronized (TcpClient.class) {
				if(instance == null)	instance = new TcpClient();
			}
		}
		return instance;
	}


	/**
	 * 建立连接，设置超时时间为4秒，并在连接建立成功以后监听服务端发来的是否接收信号
	 */
	@Override
	public void startConnect() {
		UiUtil.showDialogPage("正在连接，请稍等...", false);
		pool.execute(() -> {
			try {
				address = InetAddress.getByName(SocketUtil.HOST);
				socketAddress = new InetSocketAddress(address, SocketUtil.SERCER_POST);
				socket = new Socket();
				
				socket.connect(socketAddress, 4000);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				
				reciveMassage();
				
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

	/**
	 * 等待接收服务端“接收”信号，并通知View可以发送文件了
	 */
	@Override
	public void reciveMassage() {
		pool.execute(() -> {
			try {
				DataInputStream dis = new DataInputStream(inputStream);
				String result = dis.readUTF();
				if(result.equals(SocketUtil.ACCEPT_CONNECT)) {
					view.setCanUse(true);
				}else {
					view.setCanUse(false);
					UiUtil.showDialogPage("对方拒绝接收您的文件", true);
				}
			} catch (IOException e) {
				UiUtil.showDialogPage("连接中断", true);
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * 中断Socket请求的连接，但是shutdownNow并不能中断，可能和socket.conntect方法内部有关
	 */
	@Override
	public void interruptConnect() {
		pool.shutdownNow();
	}

	/**
	 * 关闭socket
	 */
	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("socket.close exception");
			e.printStackTrace();
		}
	}	
}