package com.client.business;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.LinkedList;
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
	
	private long curFileIndex;
	private long fileSize;
	
	private String fileRootPath;
	
	//private LinkedList<File> files;
	
	private TcpClient() {
		curFileIndex = 0;
		fileSize = 0;
		fileRootPath = "";
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
	
	/**
	 * 传输文件，并在传输结束发送over信号
	 */
	@Override
	public void sendFile(File file) {
		fileRootPath = file.getName();
		fileSize = getFileSize(file);
		
		sendSingleFile(file);
		
		DataOutputStream dop = new DataOutputStream(outputStream);
		try {
			dop.writeUTF(SocketUtil.OVER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * 获取从选中文件夹开始的路径
	 */
	private String spliceFilePath(String path) {
		int start = path.indexOf(this.fileRootPath);
		return path.substring(start);
	}
	
	
	private void sendSingleFile(File file) {
		try {
			//socket写出
			DataOutputStream dop = new DataOutputStream(outputStream);
			String path = spliceFilePath(file.getAbsolutePath());
			//1. 写出文件名以及文件大小
			dop.writeUTF(path + "&" + file.length());
			//2. 以字节形式写出文件
			if(file.isFile()) {
				//文件读入
				DataInputStream dip = new DataInputStream(new FileInputStream(file));
				int len = 0;
				//如果文件大小小于1024则字节数组大小为文件大小
				int length = 0;
				if(file.length() < (1024 * 8))	length = (int)file.length();
				else							length = 1024 * 8;
				byte[] buffer = new byte[length];
				while((len = dip.read(buffer)) != -1) {
					curFileIndex += len;
					dop.write(buffer, 0, len);
					view.refrushProgress((int) ((curFileIndex / fileSize)*100));
				}
			}else {
				File[] list = file.listFiles();
				for(File i : list) {
					sendSingleFile(i);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归获取文件大小
	 */
	private long getFileSize(File file) {
		if(file.isFile()) {
			//System.out.println(file.getAbsolutePath());
			return file.length();
		}else if (file.isDirectory()){
			//System.out.println(file.getAbsolutePath());
			File[] list = file.listFiles();
			long sum = 0;
			for(File i : list) {
				sum += getFileSize(i);
			}
			return sum;
		}else {
			return -1;
		}
	}
}