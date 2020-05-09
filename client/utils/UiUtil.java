package client.utils;

import javax.swing.JDialog;

import client.views.DialogPage;
import client.views.ReceivePage;
import client.views.SentPage;

public class UiUtil {

	
	public static void showDialogPage(String msg){
		DialogPage dialog = DialogPage.getInstance();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setText(msg);
		dialog.setVisible(true);
	}
	
	public static void showSentPage() {
		SentPage frame = new SentPage();
		frame.setVisible(true);
	}
	
	public static void showReceivePage() {
		ReceivePage frame = new ReceivePage();
		frame.setVisible(true);
	}
}
