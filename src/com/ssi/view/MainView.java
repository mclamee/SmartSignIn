package com.ssi.view;
/* The Java Version MSC Project
 * All rights reserved.
 *
 * Licensed under the Iflytek License, Version 2.0.1008.1034 (the "License");
 * you may not use this file except in compliance with the License(appId).
 * You may obtain an AppId of this application at
 *
 *      http://www.voiceclouds.cn
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Document we provide for details.
 */


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vguang.VguangApi;
import com.wicky.util.DrawableUtils;



/**SmartSingIn
 * Smart Sign In System
 * @author williamz
 * 2014-10-09
 */
@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener {		
	private JPanel mMainJpanel;
	private JPanel mContentPanel;
	private static JFrame mJframe;

	private JButton jbtSignIn;
	private JButton jbtRecord;
	private JButton jbtSetup;
	
	public static RecordView RECORD_VIEW;
	public static SetupView SETUP_VIEW;
	public static SignInView SIGNIN_VIEW;
	
	/**
	 * 界面初始化.
	 * 
	 */
	public MainView()
	{
//		Setting.saveLogFile(LOG_LEVEL.all, "./msc.log");
		//设置界面大小，背景图片
		ImageIcon background = new ImageIcon("img/index_bg.png");
		JLabel label = new JLabel(background);
		label.setBounds(0, 0, background.getIconWidth(), background
	                .getIconHeight());
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		
		int frameWidth = background.getIconWidth();
		int frameHeight = background.getIconHeight();
		
		setSize(frameWidth, frameHeight);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		ImageIcon imgRecord = new ImageIcon("img/btn_record.png");
		jbtRecord = this.createImageButton(imgRecord);
		jbtRecord.setBounds(60, 150, imgRecord.getIconWidth(), imgRecord.getIconHeight());	
		DrawableUtils.setMouseListener(jbtRecord, "img/btn_record");
		
		ImageIcon imgSignIn = new ImageIcon("img/btn_signin.png");
		jbtSignIn = this.createImageButton(imgSignIn);
		jbtSignIn.setBounds(240, 150, imgSignIn.getIconWidth(), imgSignIn.getIconHeight());
		DrawableUtils.setMouseListener(jbtSignIn, "img/btn_signin");
		
		ImageIcon imgSetup = new ImageIcon("img/btn_setup.png");
		jbtSetup = this.createImageButton(imgSetup);
		jbtSetup.setBounds(420, 150, imgSetup.getIconWidth(), imgSetup.getIconHeight());
		DrawableUtils.setMouseListener(jbtSetup, "img/btn_setup");

		GridLayout gridlayout = new GridLayout(0, 3); 
		gridlayout.setHgap(10);   
		mMainJpanel = new JPanel(gridlayout);
		mMainJpanel.setOpaque(false);
		
		mMainJpanel.add(jbtRecord);
		mMainJpanel.add(jbtSignIn);
		mMainJpanel.add(jbtSetup);
	
		jbtRecord.addActionListener(this);
		jbtSignIn.addActionListener(this);
		jbtSetup.addActionListener(this);
		
		mContentPanel = new JPanel(new BorderLayout());
		mContentPanel.setOpaque(false);
		mContentPanel.add(mMainJpanel, BorderLayout.CENTER);
		
		setLocationRelativeTo(null); 
		setContentPane(mContentPanel);
		setVisible(true);
	}
	
	
	/**
	 * Demo入口函数.
	 * @param args
	 */
	public static void main(String args[]) {
		mJframe = new MainView();
		
		RECORD_VIEW = new RecordView();
        SETUP_VIEW = new SetupView();
        SIGNIN_VIEW = new SignInView();
	}	
	
	public static JFrame getFrame()
	{
		return mJframe;
	}
	
	public JButton createImageButton(ImageIcon img)
	{
		JButton button = new JButton("");
		button.setIcon(img);
		button.setSize(img.getIconWidth(), img.getIconHeight());
		button.setBackground(null);
		
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);

		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbtRecord){
			mContentPanel.remove(mMainJpanel); 
			mContentPanel.add(RECORD_VIEW); 
			closeDevice();
			mContentPanel.revalidate();
			mContentPanel.repaint(); 
		}else if(e.getSource() == jbtSetup){
			mContentPanel.remove(mMainJpanel); 
			SETUP_VIEW = new SetupView();
			mContentPanel.add(SETUP_VIEW);
			applySettingsAndOpenDevice();
			mContentPanel.revalidate();
			mContentPanel.repaint(); 
		}else if(e.getSource() == jbtSignIn){
			mContentPanel.remove(mMainJpanel);
			mContentPanel.add(SIGNIN_VIEW); 
			applySettingsAndOpenDevice();
			mContentPanel.revalidate();
			mContentPanel.repaint();
		}
	}
	
	public void closeDevice(){
	    //关闭设备
	    VguangApi.closeDevice();
	}
	
	public void applySettingsAndOpenDevice(){
        //应用设置
        SETUP_VIEW.applySetting();
        //打开设备
        VguangApi.openDevice();
	}
	
	public JPanel getMainJpanel()
	{
		return mMainJpanel;
	}
	
	public JPanel getContePanel()
	{
		return mContentPanel;
	}
}