package com.client.utils;

import javax.swing.JDialog;

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
	
	public static void showReceivePage() {
		ReceivePage frame = new ReceivePage();
		frame.setVisible(true);
	}
}
