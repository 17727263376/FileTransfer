package com.client.common;

import java.io.File;
import java.io.IOException;

public interface IServer {
	
	public void startListen() throws IOException;
	
	public void close();
	
	public void sendMessage(String msg);
	
	public void receiveFile(String filePath);
}
