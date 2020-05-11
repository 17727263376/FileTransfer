package com.client.views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.EtchedBorder;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.client.business.TcpClient;
import com.client.common.IView;
import com.client.common.WindowListennerAdapter;
import com.client.utils.UiUtil;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class SentPage extends JFrame implements IView{

	private JPanel contentPane;
	private JTextField FilePathTextField;
	private JTextField textField_1;
	private JProgressBar sendProgress;
	private JButton choiceFileButton;
	private JButton sendButton;
	private File selectFile;
	private boolean canSend = false;
	private TcpClient client;

	/**
	 * Create the frame.
	 */
	public SentPage() {
		
		client = TcpClient.getInstance();
		client.setView(this);
		
		setTitle("17\u7F51\u7EDC\u5DE5\u7A0B1\u73ED\u8FC7\u5C0F\u864E");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListennerAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.close();
				InitPage.getInstance().setVisible(true);
			}
		});
		setBounds(100, 100, 450, 138);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		FilePathTextField = new JTextField();
		FilePathTextField.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		FilePathTextField.setBounds(87, 21, 234, 21);
		contentPane.add(FilePathTextField);
		FilePathTextField.setColumns(10);
		
		choiceFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
		choiceFileButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		choiceFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//选择文件夹
				selectFile = UiUtil.showFileSelect(JFileChooser.FILES_AND_DIRECTORIES);
				if(selectFile != null) {
					FilePathTextField.setText(selectFile.getAbsolutePath());
				}
			}
		});
		choiceFileButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		choiceFileButton.setBackground(Color.WHITE);
		choiceFileButton.setBounds(331, 20, 93, 23);
		contentPane.add(choiceFileButton);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		textField_1.setBackground(Color.WHITE);
		textField_1.setBorder(null);
		textField_1.setDisabledTextColor(Color.BLACK);
		textField_1.setEnabled(false);
		textField_1.setEditable(false);
		textField_1.setText("\u6587\u4EF6\u8DEF\u5F84\uFF1A");
		textField_1.setBounds(21, 21, 66, 21);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		sendButton = new JButton("\u53D1\u9001");
		sendButton.setFont(new Font("微雅软黑", Font.PLAIN, 12));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//发送按钮
				if(selectFile != null) {
					if(canSend) {
						System.out.println("开始发送文件");
						
						
					}else {
						UiUtil.showDialogPage("对方暂未选择接收您的文件，请等待...", true);
					}
				}else {
					UiUtil.showDialogPage("您还未选择文件噢~", true);
				}
			}
		});
		sendButton.setBackground(Color.WHITE);
		sendButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sendButton.setBounds(331, 53, 93, 23);
		contentPane.add(sendButton);
		
		sendProgress = new JProgressBar();
		sendProgress.setBorder(new LineBorder(new Color(0, 0, 0)));
		sendProgress.setForeground(new Color(50, 205, 50));
		sendProgress.setBackground(Color.WHITE);
		sendProgress.setBounds(21, 52, 300, 24);
		contentPane.add(sendProgress);
	}


	@Override
	public void refrushProgress(int value) {
		this.sendProgress.setValue(value);
	}

	@Override
	public void setCanUse(boolean val) {
		this.canSend = val;
	}
}
