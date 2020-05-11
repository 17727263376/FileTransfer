package com.client.utils;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import com.client.views.DialogPage;
import com.client.views.InitPage;
import com.client.views.ReceivePage;
import com.client.views.SentPage;

public class UiUtil {

	
	public static void showDialogPage(String msg, boolean hasClose){
		DialogPage dialog = DialogPage.getInstance();
		dialog.setDefaultCloseOperation(hasClose ? JDialog.DISPOSE_ON_CLOSE : JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setText(msg);
		dialog.setVisible(true);
	}
	
	public static File showFileSelect(int type) {
		File file = null;
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(type);
        int returnValue = jfc.showDialog(null, "选择");
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			file = jfc.getSelectedFile();
		}
		return file;
	}
	
	
	public static void cancelDialogPage() {
		DialogPage.getInstance().setVisible(false);
	}
	
	
	public static void showSentPage() {
		SentPage frame = new SentPage();
		frame.setVisible(true);
	}
	
	public static void cancelInitPage() {
		InitPage.getInstance().setVisible(false);
	}
	
	public static void showReceivePage(String ip) {
		ReceivePage frame = new ReceivePage(ip);
		frame.setVisible(true);
	}
}
