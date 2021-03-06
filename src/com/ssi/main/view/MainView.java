package com.ssi.main.view;
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.log4j.Logger;

import com.ssi.i18n.I18NUtil;
import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.util.DrawableUtils;
import com.ssi.util.ResizeUtil;
import com.ssi.util.StringUtil;



/**SmartSingIn
 * Smart Sign In System
 * @author williamz
 * 2014-10-09
 */
public class MainView extends JFrame implements ActionListener {
    
	private static final long serialVersionUID = 4333872831401104854L;

	private Logger log = Logger.getLogger(this.getClass());
    
	private JPanel mMainJpanel;
	private JPanel mContentPanel;

	private JButton jbtSignIn;
	private JButton jbtRecord;
	private JButton jbtStaff;
	private JButton jbtSetup;

    private JLabel label;
	
    private JDialog pwdDialog = new JDialog(this, true);

	private JPasswordField jfPwd;

	private JPanel nextView;

	private JButton btnPwdOk;
    
	public void showPwdDialog(JPanel view) {
		nextView = view;
		pwdDialog.setLocationRelativeTo(null);
		pwdDialog.setVisible(true);
		jfPwd.requestFocus();
	}
    
	/**
	 * 界面初始化.
	 * 
	 */
	public MainView()
	{
	    log.debug("Current Resoluation: "+DrawableUtils.getScreenWidth()+" x "+DrawableUtils.getScreenHeight());
	    
		ImageIcon background = getBackgroundImage();
		label = new JLabel(background);
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		
		int frameWidth = background.getIconWidth();
		int frameHeight = background.getIconHeight();
		
		setSize(frameWidth, frameHeight);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		ImageIcon imgRecord = new ImageIcon("res/img/btn_record.png");
		jbtRecord = this.createImageButton(imgRecord);
		jbtRecord.setBounds(60, 150, imgRecord.getIconWidth(), imgRecord.getIconHeight());	
		DrawableUtils.setMouseListener(jbtRecord, "res/img/btn_record");
		
		ImageIcon imgStaff = new ImageIcon("res/img/btn_staff.png");
		jbtStaff = this.createImageButton(imgStaff);
		jbtStaff.setBounds(60, 150, imgStaff.getIconWidth(), imgStaff.getIconHeight());	
		DrawableUtils.setMouseListener(jbtStaff, "res/img/btn_staff");
		
		ImageIcon imgSignIn = new ImageIcon("res/img/btn_signin.png");
		jbtSignIn = this.createImageButton(imgSignIn);
		jbtSignIn.setBounds(240, 150, imgSignIn.getIconWidth(), imgSignIn.getIconHeight());
		DrawableUtils.setMouseListener(jbtSignIn, "res/img/btn_signin");
		
		ImageIcon imgSetup = new ImageIcon("res/img/btn_setup.png");
		jbtSetup = this.createImageButton(imgSetup);
		jbtSetup.setBounds(420, 150, imgSetup.getIconWidth(), imgSetup.getIconHeight());
		DrawableUtils.setMouseListener(jbtSetup, "res/img/btn_setup");

		GridLayout gridlayout = new GridLayout(0, 4); 
		gridlayout.setHgap(10);   
		mMainJpanel = new JPanel(gridlayout);
		mMainJpanel.setOpaque(false);
		
		mMainJpanel.add(jbtRecord);
		mMainJpanel.add(jbtStaff);
		mMainJpanel.add(jbtSignIn);
		mMainJpanel.add(jbtSetup);
	
		jbtRecord.addActionListener(this);
		jbtStaff.addActionListener(this);
		jbtSignIn.addActionListener(this);
		jbtSetup.addActionListener(this);
		
		mContentPanel = new JPanel(new BorderLayout());
		mContentPanel.setOpaque(false);
		mContentPanel.add(mMainJpanel, BorderLayout.CENTER);
		
		setLocationRelativeTo(null); 
		setContentPane(mContentPanel);
		
		if(!Application.debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.fullscreen"))){
	        /** 
	         * true无边框 全屏显示 
	         * false有边框 全屏显示 
	         */  
	        this.setUndecorated(true);
        }  
		setVisible(true);
		
		if(!Application.debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.fullscreen"))){
		    // 全屏设置  
//	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	        GraphicsDevice gd = ge.getDefaultScreenDevice();
//	        gd.setFullScreenWindow(this);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle bounds = new Rectangle( screenSize );
			this.setBounds(bounds);
		}
		
		createPwdDialog();
	}

	private void createPwdDialog() {
		pwdDialog = new JDialog(this);
		pwdDialog.setModal(true);
		pwdDialog.setTitle(I18NUtil.getInstance().getString("Application.password.dialog.title"));
		pwdDialog.setPreferredSize(new Dimension(300, 120));
		pwdDialog.setSize(new Dimension(300, 120));
		pwdDialog.setResizable(false);
		pwdDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pwdDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jfPwd.setText("");
				nextView = null;
			}
		});
		JLabel lbPwd = new JLabel(I18NUtil.getInstance().getString("Application.password.dialog.text"));
		jfPwd = new JPasswordField();
		jfPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					btnPwdOk.doClick();
				}
			}
		});
		Container contentPane = pwdDialog.getContentPane();
//		VirtualKeyboardView contentPane = new VirtualKeyboardView();
//		pwdDialog.setContentPane(contentPane);
//		contentPane.createVirtualKeyboard(contentPane, "pwdDialog"); //$NON-NLS-1$
		contentPane.setLayout(new BorderLayout());
		contentPane.add(lbPwd, BorderLayout.NORTH);
		contentPane.add(jfPwd, BorderLayout.CENTER);
		JPanel southPan = new JPanel();
		btnPwdOk = new JButton(I18NUtil.getInstance().getString("Application.password.dialog.btn_ok"));
		btnPwdOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sysPwd = SSIConfig.get("system.password");
				String curPwd = new String(jfPwd.getPassword());
				if(sysPwd.equals(curPwd)){
					Application.switchView(nextView, false);
					jfPwd.setText("");
					nextView = null;
					pwdDialog.dispose();
				}else{
					JOptionPane.showMessageDialog(pwdDialog, I18NUtil.getInstance().getString("Application.password.dialog.incorrect"));
					jfPwd.selectAll();
					jfPwd.requestFocus();
				}
			}
		});
		southPan.add(btnPwdOk);
		JButton btnPwdCancel = new JButton(I18NUtil.getInstance().getString("Application.password.dialog.btn_cancel"));
		btnPwdCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jfPwd.setText("");
				nextView = null;
				pwdDialog.dispose();
			}
		});
		southPan.add(btnPwdCancel);
		contentPane.add(southPan, BorderLayout.SOUTH);
		
//		contentPane.applyVirtualKeyboard(contentPane, "pwdDialog");
	}

    private ImageIcon getBackgroundImage() {
        try {
            //设置界面大小，背景图片
            String bgImage = SSIConfig.get("system.startup.background");
            ImageIcon background = new ImageIcon(bgImage);
            if(Application.debugMode){
                if(SSIConfig.isBooleanValue("debug.background") 
                        && Boolean.FALSE.equals(SSIConfig.getBoolean("debug.background"))) {
                    int dotPosition = bgImage.lastIndexOf(".");
                    if(dotPosition != -1) {
                        String prefix = bgImage.substring(0, dotPosition);
                        String suffix = bgImage.substring(dotPosition);
                        File image = new File(prefix + "_d" + suffix);
                        if(image.exists()){
                            background = new ImageIcon(prefix + "_d" + suffix);
                        }
                    }
                }else if(!StringUtil.isEmpty(SSIConfig.get("debug.background"))){
                    background = new ImageIcon(SSIConfig.get("debug.background"));
                }
            }
            if(!Application.debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.fullscreen"))){
                background = ResizeUtil.resizeImageToScreenSize(background);
            }
            return background;
        } catch (Exception e) {
            log.error("Loading Background Image issued. Restored default background!", e);
            String bgImage = "res/img/index_bg.png";
            SSIConfig.put("system.startup.background", bgImage);
            ImageIcon background = new ImageIcon(bgImage);
            
            if(!Application.debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.fullscreen"))){
                background = ResizeUtil.resizeImageToScreenSize(background);
            }
            return background;
        }
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
			Application.switchView(Application.RECORD_VIEW); 
		}else if(e.getSource() == jbtStaff){
			Application.switchView(Application.STAFF_VIEW); 
		}else if(e.getSource() == jbtSetup){
			Application.switchView(Application.SETUP_VIEW);
		}else if(e.getSource() == jbtSignIn){
			Application.switchView(Application.SIGNIN_VIEW); 
		}
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