/**
 * package com.ssi.main.view
 * class com.ssi.main.view.NewSetupView
 * Created on 2014年10月23日, 下午1:01:30
 * @author williamz
 *
 * Copyright (c) 2013, Synnex and/or its affiliates. All rights reserved.
 * SYNNEX PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ssi.main.view;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.ssi.i18n.Messages;
import com.ssi.main.Application;
import com.ssi.util.DrawableUtils;
import com.vguang.VguangApi;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
public class NewSetupView extends JPanel implements IView, ActionListener{
    private static final long serialVersionUID = 522247831168522239L;
    private static final Logger LOG = Logger.getLogger(NewSetupView.class);
    
    private JButton btnHome;
    private JPasswordField pwfVerifySignInView;
    private JTextField tfAiTime;
    private JComboBox tfBeepTimes;
    private JInternalFrame fileChooseiFrame;
    private JDesktopPane desktopPane;
    private JTextPane tpDecodeResult;
    private JTextPane tpScanResult;
    private JLabel lbDeviceState;

    public NewSetupView() {
        setLayout(null);
        
        int screenWidth = 1366;
        int screenHeight = 768;
        
        ImageIcon imgHome = new ImageIcon("res/img/home.png");
        btnHome = DrawableUtils.createImageButton("", imgHome, null);
        btnHome.setBounds(20, 20, imgHome.getIconWidth(),
                imgHome.getIconHeight());
        DrawableUtils.setMouseListener(btnHome, "res/img/home");
        btnHome.addActionListener(this);
        this.add(btnHome);
        
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(SystemColor.window);
        desktopPane.setBounds(0, 0, screenWidth, screenHeight);
        add(desktopPane);
        
        fileChooseiFrame = new JInternalFrame(Messages.getString("NewSetupView.fileChooseiFrame.title"));
        fileChooseiFrame.setBounds(300, 145, 573, 430);
        fileChooseiFrame.setClosable(true);
        fileChooseiFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        fileChooseiFrame.getContentPane().setLayout(new CardLayout(0, 0));
        
        JFileChooser jfcBgImg = new JFileChooser(new File("res/img"));
        jfcBgImg.setAcceptAllFileFilterUsed(false);
        addFileChooserFilters(jfcBgImg);
        fileChooseiFrame.getContentPane().add(jfcBgImg, "name_267237042361245");
        desktopPane.add(fileChooseiFrame);
        fileChooseiFrame.pack();
        fileChooseiFrame.setVisible(true);
        try {
            fileChooseiFrame.setClosed(true);
        } catch (PropertyVetoException e1) {
            e1.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, screenWidth, screenHeight);
        desktopPane.add(mainPanel);
        mainPanel.setLayout(null);
        
        JPanel panelUI = new JPanel();
        panelUI.setBorder(new TitledBorder(null, Messages.getString("SetupView.panelUI.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
        panelUI.setBounds(100, 100, 420, 200);
        mainPanel.add(panelUI);
        panelUI.setLayout(null);
        
        JLabel lbSysLang = new JLabel(Messages.getString("SetupView.lbSysLang.text")); //$NON-NLS-1$
        lbSysLang.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSysLang.setBounds(0, 35, 180, 25);
        panelUI.add(lbSysLang);
        
        JComboBox cbSysLang = new JComboBox();
        cbSysLang.setModel(new DefaultComboBoxModel(new String[] {"中文", "English"}));
        cbSysLang.setBounds(190, 35, 200, 25);
        panelUI.add(cbSysLang);
        
        JLabel lbStartingView = new JLabel(Messages.getString("SetupView.lbStartingView.text")); //$NON-NLS-1$
        lbStartingView.setHorizontalAlignment(SwingConstants.RIGHT);
        lbStartingView.setBounds(0, 70, 180, 25);
        panelUI.add(lbStartingView);
        
        JComboBox cbStartingView = new JComboBox();
        cbStartingView.setBounds(190, 70, 200, 25);
        panelUI.add(cbStartingView);
        
        JLabel lbBgImg = new JLabel(Messages.getString("SetupView.lbBgImg.text")); //$NON-NLS-1$
        lbBgImg.setHorizontalAlignment(SwingConstants.RIGHT);
        lbBgImg.setBounds(0, 105, 180, 25);
        panelUI.add(lbBgImg);
        
        JTextField tfBgImg = new JTextField();
        tfBgImg.setBounds(190, 105, 120, 25);
        panelUI.add(tfBgImg);
        tfBgImg.setColumns(10);
        
        JLabel lbVerifySignInView = new JLabel(Messages.getString("SetupView.lbVerifySignInView.text")); //$NON-NLS-1$
        lbVerifySignInView.setHorizontalAlignment(SwingConstants.RIGHT);
        lbVerifySignInView.setBounds(0, 140, 180, 25);
        panelUI.add(lbVerifySignInView);
        
        JRadioButton rdbtnYes_VerifySignInView = new JRadioButton(Messages.getString("SetupView.rdbtnYes_VerifySignInView.text")); //$NON-NLS-1$
        rdbtnYes_VerifySignInView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pwfVerifySignInView.setEnabled(true);
            }
        });
        rdbtnYes_VerifySignInView.setSelected(true);
        rdbtnYes_VerifySignInView.setBounds(190, 140, 45, 25);
        panelUI.add(rdbtnYes_VerifySignInView);
        
        JRadioButton rdbtnNo_VerifySignInView = new JRadioButton(Messages.getString("SetupView.rdbtnNo_VerifySignInView.text")); //$NON-NLS-1$
        rdbtnNo_VerifySignInView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pwfVerifySignInView.setEnabled(false);
            }
        });
        rdbtnNo_VerifySignInView.setBounds(235, 140, 40, 25);
        panelUI.add(rdbtnNo_VerifySignInView);
        
        ButtonGroup btg_VerifySignInView = new ButtonGroup();
        btg_VerifySignInView.add(rdbtnYes_VerifySignInView);
        btg_VerifySignInView.add(rdbtnNo_VerifySignInView);
        
        pwfVerifySignInView = new JPasswordField();
        pwfVerifySignInView.setBounds(280, 140, 110, 25);
        panelUI.add(pwfVerifySignInView);
        
        JButton btnChoose = new JButton(Messages.getString("SetupView.btnChoose.text")); //$NON-NLS-1$
        btnChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desktopPane.add(fileChooseiFrame);
                fileChooseiFrame.pack();
                fileChooseiFrame.setVisible(true);
            }
        });
        btnChoose.setBounds(320, 105, 70, 25);
        panelUI.add(btnChoose);
        
        JPanel panelSynth = new JPanel();
        panelSynth.setLayout(null);
        panelSynth.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("SetupView.panelSynth.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panelSynth.setBounds(100, 320, 420, 200);
        mainPanel.add(panelSynth);
        
        JLabel lbSynthVoice = new JLabel(Messages.getString("SetupView.lbSynthVoice.text")); //$NON-NLS-1$
        lbSynthVoice.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthVoice.setBounds(0, 35, 180, 25);
        panelSynth.add(lbSynthVoice);
        
        JComboBox cbSynthVoice = new JComboBox();
        cbSynthVoice.setBounds(190, 35, 200, 25);
        panelSynth.add(cbSynthVoice);
        
        JLabel lbSynthSpeed = new JLabel(Messages.getString("SetupView.lbSynthSpeed.text")); //$NON-NLS-1$
        lbSynthSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthSpeed.setBounds(0, 70, 180, 25);
        panelSynth.add(lbSynthSpeed);
        
        JSlider sdSynthSpeed = new JSlider();
        sdSynthSpeed.setSnapToTicks(true);
        sdSynthSpeed.setPaintTicks(true);
        sdSynthSpeed.setMinorTickSpacing(10);
        sdSynthSpeed.setMajorTickSpacing(20);
        sdSynthSpeed.setBounds(190, 70, 200, 25);
        panelSynth.add(sdSynthSpeed);
        
        JLabel lbSynthRate = new JLabel(Messages.getString("SetupView.lbSynthRate.text")); //$NON-NLS-1$
        lbSynthRate.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthRate.setBounds(0, 105, 180, 25);
        panelSynth.add(lbSynthRate);
        
        JSlider sdSynthRate = new JSlider();
        sdSynthRate.setSnapToTicks(true);
        sdSynthRate.setValue(80);
        sdSynthRate.setMaximum(120);
        sdSynthRate.setPaintTicks(true);
        sdSynthRate.setMajorTickSpacing(40);
        sdSynthRate.setBounds(190, 105, 200, 25);
        panelSynth.add(sdSynthRate);
        
        JLabel lbSynthVolume = new JLabel(Messages.getString("SetupView.lbSynthVolume.text")); //$NON-NLS-1$
        lbSynthVolume.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthVolume.setBounds(0, 140, 180, 25);
        panelSynth.add(lbSynthVolume);
        
        JSlider sdSynthVolume = new JSlider();
        sdSynthVolume.setSnapToTicks(true);
        sdSynthVolume.setValue(80);
        sdSynthVolume.setPaintTicks(true);
        sdSynthVolume.setMinorTickSpacing(5);
        sdSynthVolume.setMajorTickSpacing(10);
        sdSynthVolume.setBounds(190, 140, 200, 25);
        panelSynth.add(sdSynthVolume);
        
        JPanel panelScanner = new JPanel();
        panelScanner.setLayout(null);
        panelScanner.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("SetupView.panelScanner.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panelScanner.setBounds(560, 100, 420, 295);
        mainPanel.add(panelScanner);
        
        JLabel lbCode = new JLabel(Messages.getString("SetupView.lbCode.text")); //$NON-NLS-1$
        lbCode.setHorizontalAlignment(SwingConstants.RIGHT);
        lbCode.setBounds(0, 35, 180, 25);
        panelScanner.add(lbCode);
        
        JCheckBox cbCodeQr = new JCheckBox(Messages.getString("SetupView.cbCodeQr.text")); //$NON-NLS-1$
        cbCodeQr.setBounds(190, 36, 45, 25);
        panelScanner.add(cbCodeQr);
        
        JCheckBox cbCodeDm = new JCheckBox(Messages.getString("SetupView.cbCodeDm.text")); //$NON-NLS-1$
        cbCodeDm.setBounds(235, 35, 45, 25);
        panelScanner.add(cbCodeDm);
        
        JCheckBox cbCodeBar = new JCheckBox(Messages.getString("SetupView.cbCodeBar.text")); //$NON-NLS-1$
        cbCodeBar.setBounds(280, 35, 45, 25);
        panelScanner.add(cbCodeBar);
        
        JLabel lbInterval = new JLabel(Messages.getString("SetupView.lbInterval.text")); //$NON-NLS-1$
        lbInterval.setHorizontalAlignment(SwingConstants.RIGHT);
        lbInterval.setBounds(0, 70, 180, 25);
        panelScanner.add(lbInterval);
        
        JTextField tfInterval = new JTextField();
        tfInterval.setBounds(190, 70, 120, 25);
        panelScanner.add(tfInterval);
        tfInterval.setColumns(10);
        
        JLabel lbIntervalUnit = new JLabel(Messages.getString("SetupView.lbIntervalUnit.text")); //$NON-NLS-1$
        lbIntervalUnit.setBounds(320, 70, 50, 25);
        panelScanner.add(lbIntervalUnit);
        
        JLabel lbAi = new JLabel(Messages.getString("SetupView.lbAi.text")); //$NON-NLS-1$
        lbAi.setHorizontalAlignment(SwingConstants.RIGHT);
        lbAi.setBounds(0, 105, 180, 25);
        panelScanner.add(lbAi);
        
        JRadioButton rdbtnYes_Ai = new JRadioButton(Messages.getString("SetupView.rdbtnYes_Ai.text")); //$NON-NLS-1$
        rdbtnYes_Ai.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfAiTime.setEnabled(true);
            }
        });
        rdbtnYes_Ai.setSelected(true);
        rdbtnYes_Ai.setBounds(190, 105, 45, 25);
        panelScanner.add(rdbtnYes_Ai);
        
        JRadioButton rdbtnNo_Ai = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Ai.text")); //$NON-NLS-1$
        rdbtnNo_Ai.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfAiTime.setEnabled(false);
            }
        });
        rdbtnNo_Ai.setBounds(235, 105, 40, 25);
        panelScanner.add(rdbtnNo_Ai);
        
        ButtonGroup btg_Ai = new ButtonGroup();
        btg_Ai.add(rdbtnYes_Ai);
        btg_Ai.add(rdbtnNo_Ai);
        
        JLabel lbAiTimePrefix = new JLabel(Messages.getString("SetupView.lbAiTimePrefix.text")); //$NON-NLS-1$
        lbAiTimePrefix.setBounds(280, 105, 25, 25);
        panelScanner.add(lbAiTimePrefix);
        
        tfAiTime = new JTextField();
        tfAiTime.setBounds(300, 105, 50, 25);
        panelScanner.add(tfAiTime);
        tfAiTime.setColumns(10);
        
        JLabel lbAiTimeSuffix = new JLabel(Messages.getString("SetupView.lbAiTimeSuffix.text")); //$NON-NLS-1$
        lbAiTimeSuffix.setBounds(355, 105, 50, 25);
        panelScanner.add(lbAiTimeSuffix);
        
        JLabel lbBeep = new JLabel(Messages.getString("SetupView.lbBeep.text")); //$NON-NLS-1$
        lbBeep.setHorizontalAlignment(SwingConstants.RIGHT);
        lbBeep.setBounds(0, 140, 180, 25);
        panelScanner.add(lbBeep);
        
        JRadioButton rdbtnYes_Beep = new JRadioButton(Messages.getString("SetupView.rdbtnYes_Beep.text")); //$NON-NLS-1$
        rdbtnYes_Beep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfBeepTimes.setEnabled(true);
            }
        });
        rdbtnYes_Beep.setSelected(true);
        rdbtnYes_Beep.setBounds(190, 140, 45, 25);
        panelScanner.add(rdbtnYes_Beep);
        
        JRadioButton rdbtnNo_Beep = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Beep.text")); //$NON-NLS-1$
        rdbtnNo_Beep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfBeepTimes.setEnabled(false);
            }
        });
        rdbtnNo_Beep.setBounds(235, 140, 40, 25);
        panelScanner.add(rdbtnNo_Beep);
        
        ButtonGroup btg_Beep = new ButtonGroup();
        btg_Beep.add(rdbtnYes_Beep);
        btg_Beep.add(rdbtnNo_Beep);
        
        tfBeepTimes = new JComboBox();
        tfBeepTimes.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
        tfBeepTimes.setBounds(300, 142, 50, 21);
        panelScanner.add(tfBeepTimes);
        
        JLabel lbBeepTimes = new JLabel(Messages.getString("SetupView.lbBeepTimes.text")); //$NON-NLS-1$
        lbBeepTimes.setBounds(355, 140, 50, 25);
        panelScanner.add(lbBeepTimes);
        
        JLabel lbLight = new JLabel(Messages.getString("SetupView.lbLight.text")); //$NON-NLS-1$
        lbLight.setHorizontalAlignment(SwingConstants.RIGHT);
        lbLight.setBounds(0, 170, 180, 25);
        panelScanner.add(lbLight);
        
        JRadioButton rdbtnYes_Light = new JRadioButton(Messages.getString("SetupView.rdbtnYes_Light.text")); //$NON-NLS-1$
        rdbtnYes_Light.setSelected(true);
        rdbtnYes_Light.setBounds(190, 170, 45, 25);
        panelScanner.add(rdbtnYes_Light);
        
        JRadioButton rdbtnNo_Light = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Light.text")); //$NON-NLS-1$
        rdbtnNo_Light.setBounds(235, 170, 40, 25);
        panelScanner.add(rdbtnNo_Light);
        
        ButtonGroup btng_Light = new ButtonGroup();
        btng_Light.add(rdbtnYes_Light);
        btng_Light.add(rdbtnNo_Light);
        
        JButton btnOpen = new JButton(Messages.getString("SetupView.btnOpen.text")); //$NON-NLS-1$
        btnOpen.setBounds(20, 240, 120, 25);
        panelScanner.add(btnOpen);
        
        JButton btnClose = new JButton(Messages.getString("SetupView.btnClose.text")); //$NON-NLS-1$
        btnClose.setBounds(150, 240, 120, 25);
        panelScanner.add(btnClose);
        
        JButton btnApply = new JButton(Messages.getString("SetupView.btnApply.text")); //$NON-NLS-1$
        btnApply.setBounds(280, 240, 120, 25);
        panelScanner.add(btnApply);
        
        JLabel lbDevice = new JLabel(Messages.getString("SetupView.lbDevice.text")); //$NON-NLS-1$
        lbDevice.setBounds(0, 205, 180, 25);
        panelScanner.add(lbDevice);
        lbDevice.setHorizontalAlignment(SwingConstants.RIGHT);
        
        lbDeviceState = new JLabel(Messages.getString("SetupView.lbDeviceState.active.text"));
        lbDeviceState.setBounds(190, 205, 80, 25);
        panelScanner.add(lbDeviceState);
        
        JPanel panelDecode = new JPanel();
        panelDecode.setLayout(null);
        panelDecode.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("SetupView.panelDecode.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panelDecode.setBounds(560, 415, 420, 280);
        mainPanel.add(panelDecode);
        
        JLabel lbScanResult = new JLabel(Messages.getString("SetupView.lbScanResult.text")); //$NON-NLS-1$
        lbScanResult.setHorizontalAlignment(SwingConstants.RIGHT);
        lbScanResult.setBounds(0, 35, 100, 25);
        panelDecode.add(lbScanResult);
        
        tpScanResult = new JTextPane();
        tpScanResult.setBounds(110, 35, 290, 100);
        panelDecode.add(tpScanResult);
        
        JLabel lbDecodeResult = new JLabel(Messages.getString("SetupView.lbDecodeResult.text")); //$NON-NLS-1$
        lbDecodeResult.setHorizontalAlignment(SwingConstants.RIGHT);
        lbDecodeResult.setBounds(0, 145, 100, 25);
        panelDecode.add(lbDecodeResult);
        
        tpDecodeResult = new JTextPane();
        tpDecodeResult.setBounds(110, 145, 290, 100);
        panelDecode.add(tpDecodeResult);
        
        JButton btnAboutSoftware = new JButton(Messages.getString("SetupView.btnAboutSoftware.text")); //$NON-NLS-1$
        btnAboutSoftware.setBounds(100, 565, 120, 25);
        mainPanel.add(btnAboutSoftware);
        
        JButton btnAboutThisVersion = new JButton(Messages.getString("SetupView.btnAboutThisVersion.text")); //$NON-NLS-1$
        btnAboutThisVersion.setBounds(235, 565, 120, 25);
        mainPanel.add(btnAboutThisVersion);
    }

    private void addFileChooserFilters(JFileChooser jfcBgImg) {
        jfcBgImg.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "*.png";  
            }
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png")||f.getName().endsWith(".PNG");  
            }
        });
        jfcBgImg.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "*.jpg";  
            }
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".jpg")||f.getName().endsWith(".JPG");  
            }
        });
        jfcBgImg.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "*.gif";  
            }
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".GIF")||f.getName().endsWith(".GIF");  
            }
        });
        jfcBgImg.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "*.bmp";  
            }
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".bmp")||f.getName().endsWith(".BMP");  
            }
        });
    }
    
    //应用设置
    public void applySetting(){
//        //设置QR状态
//        VguangApi.setQRable(chckbxQr.isSelected());
//        //设置DM状态
//        VguangApi.setDMable(checkboxDm.isSelected());
//        //设置Bar状态
//        VguangApi.setBarcode(chckbxBar.isSelected());
//        
//        // 设置解码间隔时间，单位毫秒
//        VguangApi.setDeodeIntervalTime(StringToInt(textDecodeTime.getText(), 300));
//        
//        //设置自动休眠状态
//        VguangApi.setAI(chckbxAi.isSelected());
//        int aiLimit = StringToInt(textAiLimit.getText(), 20);
//        if(aiLimit < 1 || aiLimit > 64){
//            aiLimit = 20;
//        }
//        // 设置自动休眠灵敏度
//        VguangApi.setAISensitivity(aiLimit);
//        // 设置自动休眠响应时间，单位秒
//        VguangApi.setAIResponseTime(StringToInt(textAiResponeTime.getText(), 30));
//
//        //设置扬声器状态
//        VguangApi.setBeepable(chckbxBeep.isSelected());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHome) {
            JFrame frame = Application.MAIN_FRAME;
            frame.getContentPane().remove(this);
            JPanel panel = ((MainView) frame).getMainJpanel();
            frame.getContentPane().add(panel);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        }
    }
    @Override
    public void openSubDialog(String title, String message, IDataVector<ISubDataVector> idata) {
    }
    @Override
    public void closeSubDialog() {
    }
    @Override
    public String getTemplate() {
        return null;
    }

    private static int StringToInt(String str, int defautValue){
        if(str != null){
            try{
                return Integer.parseInt(str);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return defautValue;
    }
    
    public void setDeviceStatus(final int istatus){
        //在主线程中更新UI
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if(istatus == VguangApi.DEVICE_VALID){
                    lbDeviceState.setText(Messages.getString("SetupView.lbDeviceState.active.text"));
                    lbDeviceState.setEnabled(true);
                }else{
                    lbDeviceState.setText(Messages.getString("SetupView.lbDeviceState.inactive.text"));
                    lbDeviceState.setEnabled(false);
                }
            }
        });
    }
    
    public void setResultString(final String str){
        //在主线程中更新UI
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                tpScanResult.setText(str);
            }
        });
    }
    
    public void setMessageString(final String message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                tpDecodeResult.setText(message);
            }
        });
    }
    
    public static void main(String[] args) {
        JFrame mainframe = new JFrame();
        mainframe.getContentPane().add(new NewSetupView());
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.pack();
        mainframe.setVisible(true);
    }
}
