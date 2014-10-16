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
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sun.misc.BASE64Decoder;

import com.wicky.util.ConfigUtil;
import com.wicky.util.DrawableUtils;
import com.wicky.util.Resize;
import com.wicky.util.StringUtil;



/**SmartSingIn
 * Smart Sign In System
 * @author williamz
 * 2014-10-09
 */
@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener {		
	private JPanel mMainJpanel;
	private JPanel mContentPanel;

	private JButton jbtSignIn;
	private JButton jbtRecord;
	private JButton jbtSetup;
	
	private static JFrame mJframe;
	private static JFrame authFrame;
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
	    System.out.println("Current Resoluation: "+DrawableUtils.getScreenWidth()+" x "+DrawableUtils.getScreenHeight());
	    
		//设置界面大小，背景图片
		ImageIcon background = new ImageIcon("img/index_bg.png");
		background = resizeImageToScreenSize(background);
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
		
        /** 
         * true无边框 全屏显示 
         * false有边框 全屏显示 
         */  
        this.setUndecorated(true);  
		setVisible(true);
		
	    // 全屏设置  
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
	}

    private ImageIcon resizeImageToScreenSize(ImageIcon imageIcon) {
        Image img = imageIcon.getImage();
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        int screenWidth = DrawableUtils.getScreenWidth();
        int screenHeight = DrawableUtils.getScreenHeight();

        if(screenWidth == imageWidth && screenHeight == imageHeight){
            return imageIcon;
        }
        
        // Draw the image on to the buffered image
        BufferedImage bimage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return new ImageIcon(Resize.rize(bimage, screenWidth, screenHeight));
    }
	
    private static boolean authorization() {
        try {
            String user = ConfigUtil.get("user");
            String authKey = ConfigUtil.get("authKey");
            String decoded = new String(new BASE64Decoder().decodeBuffer(authKey));
            System.out.println("Checking Authorization Information!");
            URL url = new URL("http://mcvalidate.sinaapp.com/?authKey="+decoded+"&user="+user);
            InputStream inputStream = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            if(!StringUtil.isEmpty(line) && line != "false"){
                int idxOfD = line.indexOf("D");
                int idxOfA = line.indexOf("a");
                if(idxOfD != -1 && idxOfA != -1){
                    String pd = line.substring(0, idxOfD);
                    String pa = line.substring(idxOfD + 5, idxOfA);
                    if(!StringUtil.isEmpty(pa)){
                        int idxOfPa11 = line.indexOf(pa+"11");
                        if(idxOfPa11 != -1){
                            String pm = line.substring(idxOfPa11 + pa.length() + 2, line.length() - 4);
                            
                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d");
                            String source = (Integer.valueOf(pm) - 5541) + "/"+ (Integer.valueOf(pa) - 7) + "/" + (Integer.valueOf(pd) - 91);
                            String todayStr = fmt.format(new Date());
                            if(source.equals(todayStr)){
                                System.out.println("Authorized to '" + user + "' successfully!");
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Authorization failed!");
        return false;
    }
    
	/**
	 * Demo入口函数.
	 * @param args
	 */
	public static void main(String args[]) {
        if(authorization()){
            initFrames();
        }else{
            final JFrame authFrame = new JFrame("Authrization");
            
            ImageIcon background = new ImageIcon("img/auth_bg.png");
            JLabel label = new JLabel(background);
            label.setBounds(0, 0, background.getIconWidth(), background
                        .getIconHeight());
            authFrame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
            
            int frameWidth = background.getIconWidth();
            int frameHeight = background.getIconHeight();
            
            authFrame.setSize(frameWidth, frameHeight);
            authFrame.setResizable(false);
            
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(null);
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setOpaque(false);
            contentPanel.add(panel, BorderLayout.CENTER);
            authFrame.setContentPane(contentPanel);

            int eleHeight = 30;
            int rowSpace = 15;
            int colSpace = 10;
            
            int topSpace = 2 * frameHeight / 12 - rowSpace;
            int leftSpace = 1 * frameWidth / 12 - colSpace;
            
            int titleWidth = 3 * frameWidth / 12 - colSpace;
            int txfWidth = 8 * frameWidth / 12 - colSpace;
            
            int row1 = topSpace + 0;
            int row2 = row1 + rowSpace + eleHeight;
            int row3 = row2 + rowSpace + eleHeight;
            
            int column1 = leftSpace + 0;
            int column2 = column1 + titleWidth + colSpace;
            
            JLabel lbUser = new JLabel("User: ");
            lbUser.setBounds(column1, row1, titleWidth, eleHeight);
            panel.add(lbUser);
            final JTextField txUser = new JTextField();
            txUser.setBounds(column2, row1, txfWidth, eleHeight);
            txUser.setText(ConfigUtil.get("user"));
            panel.add(txUser);
            
            JLabel lbAuth = new JLabel("Authorization Key: ");
            lbAuth.setBounds(column1, row2, titleWidth, eleHeight);
            panel.add(lbAuth);
            final JTextField txAuth = new JTextField();
            txAuth.setBounds(column2, row2, txfWidth, eleHeight);
            txAuth.setText(ConfigUtil.get("authKey"));
            panel.add(txAuth);
            
            JButton btnOk = new JButton("OK");
            btnOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ConfigUtil.put("user", txUser.getText());
                    ConfigUtil.put("authKey", txAuth.getText());
                    ConfigUtil.save();
                    if(authorization()){
                        initFrames();
                        authFrame.dispose();
                    }else{
                        JOptionPane.showMessageDialog(authFrame, "Authoriation failed! Please check your inputs.");
                    }
                }
            });
            btnOk.setBounds(column2, row3, titleWidth, eleHeight);
            panel.add(btnOk);
            JButton btnCancel = new JButton("Exit");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            btnCancel.setBounds(column2 + titleWidth + colSpace, row3, titleWidth, eleHeight);
            panel.add(btnCancel);
            
            authFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            authFrame.setLocationRelativeTo(null); 
            authFrame.setVisible(true);
        }
	}

    private static void initFrames() {
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
//	    VguangApi.closeDevice();
	}
	
	public void applySettingsAndOpenDevice(){
        //应用设置
//        SETUP_VIEW.applySetting();
        //初始化数据
        SIGNIN_VIEW.initDataMap();
        //打开设备
//        VguangApi.openDevice();
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