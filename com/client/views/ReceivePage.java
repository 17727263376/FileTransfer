package com.client.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.client.business.TcpServer;
import com.client.common.IView;
import com.client.common.WindowListennerAdapter;
import com.client.utils.SocketUtil;
import com.client.utils.UiUtil;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ReceivePage extends JFrame implements IView {

	private JPanel contentPane;
	private JTextField receiveFilePath;
	private JTextField textField_1;
	private JButton refuseButton;
	private JTextField receiveTitleText;
	private JTextField toastText;
	private JButton choiceFileButton;
	private JButton acceptButton;
	private JProgressBar receiveProgress;
	private File selectFile;
	private TcpServer server;
	
	private boolean canReceive = false;

	/**
	 * Create the frame.
	 */
	public ReceivePage(String ip) {
		server = TcpServer.getInstance();
		server.setView(this);
		
		setTitle("17\u7F51\u7EDC\u5DE5\u7A0B1\u73ED\u8FC7\u5C0F\u864E");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListennerAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				server.close();
				InitPage page = InitPage.getInstance();
				page.setVisible(true);
			}
		});
		setBounds(100, 100, 450, 205);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		receiveFilePath = new JTextField();
		receiveFilePath.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		receiveFilePath.setColumns(10);
		receiveFilePath.setBounds(64, 44, 257, 21);
		contentPane.add(receiveFilePath);
		
		choiceFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
		choiceFileButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		choiceFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//选择文件
				selectFile = UiUtil.showFileSelect(JFileChooser.DIRECTORIES_ONLY);
				if(selectFile != null) {
					receiveFilePath.setText(selectFile.getAbsolutePath());
				}
			}
		});
		choiceFileButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		choiceFileButton.setBackground(Color.WHITE);
		choiceFileButton.setBounds(331, 43, 93, 23);
		contentPane.add(choiceFileButton);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		textField_1.setText("\u53E6\u5B58\u4E3A:");
		textField_1.setEnabled(false);
		textField_1.setEditable(false);
		textField_1.setDisabledTextColor(Color.BLACK);
		textField_1.setColumns(10);
		textField_1.setBorder(null);
		textField_1.setBackground(Color.WHITE);
		textField_1.setBounds(11, 44, 51, 21);
		contentPane.add(textField_1);
		
		acceptButton = new JButton("\u63A5\u53D7");
		acceptButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//接受
				if(selectFile != null) {
					server.sendMessage(SocketUtil.ACCEPT_CONNECT);
					
					server.receiveFile(selectFile.getAbsolutePath());
					
					System.out.println("开始接收文件");
				}else {
					UiUtil.showDialogPage("您还未选择文件噢~", true);
				}
				
			}
		});
		acceptButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		acceptButton.setBackground(Color.WHITE);
		acceptButton.setBounds(11, 75, 177, 23);
		contentPane.add(acceptButton);
		
		receiveProgress = new JProgressBar();
		receiveProgress.setForeground(new Color(50, 205, 50));
		receiveProgress.setBorder(new LineBorder(new Color(0, 0, 0)));
		receiveProgress.setBackground(Color.WHITE);
		receiveProgress.setBounds(11, 131, 413, 24);
		contentPane.add(receiveProgress);
		
		refuseButton = new JButton("\u62D2\u6536");
		refuseButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		refuseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//拒绝
				server.sendMessage(SocketUtil.REFUSE_CONNECT);
				setVisible(false);
				server.close();
				InitPage page = InitPage.getInstance();
				page.setVisible(true);
			}
		});
		refuseButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		refuseButton.setBackground(Color.WHITE);
		refuseButton.setBounds(228, 75, 196, 23);
		contentPane.add(refuseButton);
		
		receiveTitleText = new JTextField();
		receiveTitleText.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		receiveTitleText.setText("收到IP为"+ ip +"发来的文件");
		receiveTitleText.setEnabled(false);
		receiveTitleText.setEditable(false);
		receiveTitleText.setDisabledTextColor(Color.BLACK);
		receiveTitleText.setColumns(10);
		receiveTitleText.setBorder(null);
		receiveTitleText.setBackground(Color.WHITE);
		receiveTitleText.setBounds(11, 10, 413, 33);
		contentPane.add(receiveTitleText);
		
		toastText = new JTextField();
		toastText.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		toastText.setText("\u6B63\u5728\u63A5\u6536\u6587\u4EF6\uFF1A");
		toastText.setEnabled(false);
		toastText.setEditable(false);
		toastText.setDisabledTextColor(Color.BLACK);
		toastText.setColumns(10);
		toastText.setBorder(null);
		toastText.setBackground(Color.WHITE);
		toastText.setBounds(11, 97, 413, 33);
		contentPane.add(toastText);
	}

	@Override
	public void refrushProgress(int value) {
		this.receiveProgress.setValue(value);
	}

	@Override
	public void setCanUse(boolean val) {
		
	}

}
