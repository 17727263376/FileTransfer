package com.client.business;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.SwingUtilities;

import com.client.common.IServer;
import com.client.common.IView;
import com.client.utils.SocketUtil;
import com.client.utils.UiUtil;
/**
 *	文件传输的过程中断开一方的连接还没有做适配
 * 
 */
public class TcpServer implements IServer {
	private ServerSocket ss;
	private Socket socket;
	private ExecutorService pool;
	private IView view;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private static TcpServer instance;
	
	private String filePath;
	
	private long fileSize = 0;
	private long curFileIndex = 0;
	
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
			Thread.currentThread().setName("Server Listen");
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
	
	@Override
	public void receiveFile(String filePath) {
		this.filePath = filePath;
		
		DataInputStream dis = new DataInputStream(inputStream);
		
		pool.execute(() -> {
			
			Thread.currentThread().setName("Server ReceiveFile");
			
			try {
				while(true) {
					//1. 接收客户处传来的文件信息
					String mas = dis.readUTF();
					//2. 如果文件信息为“over”代表文件传输结束，退出循环
					System.out.println(mas);
					if(mas.equals(SocketUtil.OVER)) {
						UiUtil.showDialogPage("文件接收完成~", true);
						break;
					}
					//3. 拆分文件信息，[0]为文件名[1]为文件大小
					String []mass = mas.split("&");
					String path = mass[0];
					
					fileSize = 0;
					curFileIndex = 0;
					
					boolean isDirectory = false;
					
					if(mass.length > 1) {
						if(mass[1].equals(SocketUtil.IS_DIRECTORY)) {
							isDirectory = true;
						}else {
							isDirectory = false;
							fileSize = Long.parseLong(mass[1]);
						}
					}else {
						//文件传输出现错误
						UiUtil.showDialogPage("文件传输出现错误", true);
					}
					
					//4. 如果接收方选取的路径为D:这种跟盘符，则不需要加File.separatorChar
					File file = null;
					if(filePath.charAt(filePath.length() - 1) == File.separatorChar) {
						file = new File(filePath + path);
					}else {
						file = new File(filePath + File.separatorChar + path);
					}
					
					
					//5. 根据文件路径名是否还有“.”，判断接下来传输的是文件还是文件夹
					if(!isDirectory) {
						//创建新文件
						file.createNewFile();
						
						DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
						int len = 0;
						int length = 0;
						//  如果文件大小小于1024则直接用文件size作为缓存数组，避免将over信号接收进去
						if(fileSize < 1024 * 8)	length = (int)fileSize;
						else						length = 1024;
						byte[] buffer = new byte[length];
						
						while((len = dis.read(buffer)) != -1) {
							curFileIndex += len;
							dos.write(buffer, 0, len);
							dos.flush();
							view.refrushProgress((int) (((double)curFileIndex / fileSize)*100));
							
							//动态调整buffer数组，使其可以刚好接受到文件的末尾部分，避免混乱
							if(fileSize - curFileIndex < 1024 && fileSize - curFileIndex >= 0) {
								buffer = new byte[(int)(fileSize - curFileIndex)];
							}
							
							// 已传送文件大于等于文件总大小，则表明这个文件传输完了
							if(curFileIndex >= fileSize) {
								System.out.println("break");
								break;
							}
						}
					}else {
						file.mkdirs();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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
		pool.execute(()->{
			
			Thread.currentThread().setName("Server SendMessage");
			
			try {
				DataOutputStream dos = new DataOutputStream(this.outputStream);
				dos.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		
	}
	
	
}
