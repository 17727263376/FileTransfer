package com.client.views;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import com.client.business.TcpClient;
import com.client.business.TcpServer;
import com.client.utils.SocketUtil;
import com.client.utils.UiUtil;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class InitPage extends JFrame {

	private JPanel contentPane;
	private JTextField txtip;
	private JTextField innerDisIp;
	private JTextField txtip_1;
	private JTextField outServeIp;
	private JButton outButton;
	private JButton innerButton;
	
	private static InitPage page;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Initial Socket Server
					TcpServer ts = TcpServer.getInstance();
					ts.startListen();
					InitPage frame = InitPage.getInstance();
					frame.setVisible(true);
				} catch (IOException e1) {
					UiUtil.showDialogPage("警告：程序已运行或11111端口被占用", true);
					e1.printStackTrace();
				}
			}
		});
	}

	//
	public static InitPage getInstance() {
		if(page == null) {
			synchronized (InitPage.class) {
				if(page == null) {
					page = new InitPage();
				}
			}
		}
		return page; 
	}
	
	/**
	 * Create the frame.
	 */
	private InitPage() {
		setTitle("17\u7F51\u7EDC\u5DE5\u7A0B1\u73ED\u8FC7\u5C0F\u864E");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 433, 141);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel ipPanel = new JPanel();
		ipPanel.setBackground(Color.WHITE);
		ipPanel.setBounds(10, 10, 400, 74);
		contentPane.add(ipPanel);
		ipPanel.setLayout(null);
		
		txtip = new JTextField();
		txtip.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		txtip.setBorder(null);
		txtip.setForeground(Color.WHITE);
		txtip.setDisabledTextColor(Color.BLACK);
		txtip.setBackground(Color.WHITE);
		txtip.setEnabled(false);
		txtip.setEditable(false);
		txtip.setText("\u76EE\u7684IP\uFF1A");
		txtip.setBounds(10, 10, 57, 21);
		ipPanel.add(txtip);
		txtip.setColumns(10);
		
		innerDisIp = new JTextField();
		innerDisIp.setText("127.0.0.1");
		innerDisIp.setBounds(101, 10, 174, 21);
		ipPanel.add(innerDisIp);
		innerDisIp.setColumns(10);
		
		innerButton = new JButton("\u5185\u7F51\u8FDE\u63A5");
		innerButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		innerButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//内网连接button
				if(innerDisIp.getText().length() != 0 && innerDisIp.getText() != null) {
					SocketUtil.HOST = innerDisIp.getText();
					TcpClient tc =TcpClient.getInstance();
					tc.startConnect();
				}
			}
		});
		innerButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		innerButton.setBackground(Color.WHITE);
		innerButton.setBounds(285, 9, 93, 23);
		ipPanel.add(innerButton);
		
		outButton = new JButton("\u516C\u7F51\u8FDE\u63A5");
		outButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		outButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//公网连接
				UiUtil.showDialogPage("此功能正在开发中...", true);
			}
		});
		outButton.setActionCommand("\u516C\u7F51\u8FDE\u63A5");
		outButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		outButton.setBackground(Color.WHITE);
		outButton.setBounds(285, 41, 93, 23);
		ipPanel.add(outButton);
		
		txtip_1 = new JTextField();
		txtip_1.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		txtip_1.setText("\u4E91\u670D\u52A1\u5668IP\uFF1A");
		txtip_1.setForeground(Color.WHITE);
		txtip_1.setEnabled(false);
		txtip_1.setEditable(false);
		txtip_1.setDisabledTextColor(Color.BLACK);
		txtip_1.setColumns(10);
		txtip_1.setBorder(null);
		txtip_1.setBackground(Color.WHITE);
		txtip_1.setBounds(10, 43, 82, 21);
		ipPanel.add(txtip_1);
		
		outServeIp = new JTextField();
		outServeIp.setColumns(10);
		outServeIp.setBounds(101, 41, 174, 21);
		ipPanel.add(outServeIp);
	}
}
