package com.client.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;

import javax.swing.border.EtchedBorder;

import com.client.business.TcpClient;
import com.client.common.WindowListennerAdapter;

import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class DialogPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static DialogPage instance;
	private static JTextArea textArea;
	
	/**
	 * Create the dialog.
	 */
	private DialogPage() {
		setTitle("17\u7F51\u7EDC\u5DE5\u7A0B1\u73ED\u8FC7\u5C0F\u864E");
		setBounds(100, 100, 309, 166);
		addWindowListener(new WindowListennerAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		});
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setAlignmentY(Component.TOP_ALIGNMENT);
		textArea.setBackground(new Color(255, 255, 255));
		textArea.setBorder(null);
		textArea.setDisabledTextColor(Color.BLACK);
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		textArea.setBounds(10, 10, 273, 107);
		contentPanel.add(textArea);
	}

	public static DialogPage getInstance() {
		//单例：DCL模式
		if(instance == null) {
			synchronized (DialogPage.class) {
				if(instance == null)	instance = new DialogPage();
			}
		}
		return instance;
	}
	
	public void setText(String msg) {
		textArea.setText(msg);
	}
}
