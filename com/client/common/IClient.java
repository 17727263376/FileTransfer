package com.client.common;

import java.io.IOException;

public interface IClient {

	
	
	public void startConnect();
	public void close();
	public void interruptConnect();
	public void reciveMassage();
}
