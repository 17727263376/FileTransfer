package client.business;

import java.io.IOException;
import java.net.*;
import client.common.IClient;
import client.common.IView;
import client.utils.SocketUtil;
import client.utils.UiUtil;

public class TcpClient implements IClient {
	
	private InetAddress address;
	private Socket socket;
	private IView view;
	
	
	public TcpClient(String host) throws IOException {
		address =  InetAddress.getByName(host);
		//socket = new Socket(address, SocketUtil.SERCER_POST);
	}


	@Override
	public void startConnect() throws IOException {
		UiUtil.showDialogPage("正在连接，请稍等...");
		
		socket = new Socket(address, SocketUtil.SERCER_POST);
		
		
	}
	
	
	
	
}
