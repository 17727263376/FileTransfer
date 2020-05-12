package com.client.common;

import java.io.*;

public interface IClient {

	
	
	public void startConnect();
	public void close();
	public void interruptConnect();
	public void reciveMassage();
	public void sendFile(File file);
	
}
