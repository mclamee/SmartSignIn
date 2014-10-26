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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.ssi.i18n.Messages;
import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.SetupModel;
import com.ssi.util.DrawableUtils;
import com.ssi.util.StringUtil;
import com.vguang.VguangApi;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
public class SetupView extends VirtualKeyboardView implements IView, ActionListener{
    private static final long serialVersionUID = 522247831168522239L;
    private static final Logger LOG = Logger.getLogger(SetupView.class);
    
    private SetupModel model = new SetupModel();
    
    private JButton btnHome;
    private JPasswordField pwfVerifySignInView;
    private JTextField tfAiTime;
    private JComboBox tfBeepTimes;
    private JInternalFrame fileChooseiFrame;
    private JDesktopPane desktopPane;
    private JTextPane tpDecodeResult;
    private JTextPane tpScanResult;
    private JLabel lbDeviceState;

	private JComboBox cbSysLang;
	private JComboBox cbStartingView;
	private JTextField tfBgImg;
	private JCheckBox cbCodeQr;
	private JCheckBox cbCodeDm;
	private JCheckBox cbCodeBar;
	private JTextField tfInterval;
	private JRadioButton rdbtnYes_Ai;
	private JRadioButton rdbtnYes_Beep;
	private JComboBox cbSynthVoice;
	private JSlider sdSynthSpeed;
	private JSlider sdSynthRate;
	private JSlider sdSynthVolume;
	private JTextField tfEmailSenderAddress;
	private JPasswordField pfEmailSenderPwd;
	private JTextField tfEmailSmtpServer;
	private JTextField tfEmailRecipientAddress;
    private int frameWidth;
    private int frameHeight;
    private JFileChooser jfcBgImg;
    
    public SetupView() {
        frameWidth = 1366;
        frameHeight = 768;
//        try {
//            Dimension frameSize = Application.MAIN_FRAME.getSize();
//            frameWidth = (int)frameSize.getWidth();
//            frameHeight = (int)frameSize.getHeight();
//        } catch (Exception e) {
//        }
//        
        this.setSize(frameWidth, frameHeight);
        
        ImageIcon imgHome = new ImageIcon("res/img/home.png");
        btnHome = DrawableUtils.createImageButton("", imgHome, null);
        btnHome.setBounds(20, 20, imgHome.getIconWidth(),
                imgHome.getIconHeight());
        DrawableUtils.setMouseListener(btnHome, "res/img/home");
        btnHome.addActionListener(this);
        this.add(btnHome);
        setLayout(new BorderLayout(0, 0));
        
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(SystemColor.window);
        add(desktopPane);
        
        fileChooseiFrame = new JInternalFrame(Messages.getString("SetupView.fileChooseiFrame.title"));
        fileChooseiFrame.setBounds(300, 145, 573, 430);
        fileChooseiFrame.setClosable(true);
        fileChooseiFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        fileChooseiFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                fileChooseiFrame.setBounds(300, 145, 573, 430);
            }
        });
        fileChooseiFrame.getContentPane().setLayout(new CardLayout(0, 0));
        
        jfcBgImg = new JFileChooser();
        jfcBgImg.setAcceptAllFileFilterUsed(true);
        jfcBgImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)){
                    tfBgImg.setText(jfcBgImg.getSelectedFile().getPath());
                    fileChooseiFrame.doDefaultCloseAction();
                }else if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)){
                    fileChooseiFrame.doDefaultCloseAction();
                }
            }
        });
        addFileChooserFilters(jfcBgImg);
        fileChooseiFrame.getContentPane().add(jfcBgImg, "name_267237042361245");
        desktopPane.add(fileChooseiFrame);
        fileChooseiFrame.pack();
        fileChooseiFrame.setVisible(true);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, frameWidth, frameHeight);
        desktopPane.add(mainPanel);
        mainPanel.setLayout(null);
        
        createVirtualKeyboard(mainPanel, "SetupView");
        
        JPanel panelUI = new JPanel();
        panelUI.setBorder(new TitledBorder(null, Messages.getString("SetupView.panelUI.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
        panelUI.setBounds(100, 100, 420, 185);
        mainPanel.add(panelUI);
        panelUI.setLayout(null);
        
        JLabel lbSysLang = new JLabel(Messages.getString("SetupView.lbSysLang.text")); //$NON-NLS-1$
        lbSysLang.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSysLang.setBounds(0, 35, 180, 25);
        panelUI.add(lbSysLang);
        
        cbSysLang = new JComboBox();
        cbSysLang.setModel(new DefaultComboBoxModel(new String[] {"中文", "English"}));
        cbSysLang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = cbSysLang.getSelectedIndex();
				if(idx == 0){
					SSIConfig.put("system.locale.language", "zh");
					SSIConfig.put("system.locale.country", "CN");
					System.setProperty("locale", "zh_CN");
				}else if(idx == 1){
					SSIConfig.put("system.locale.language", "en");
					SSIConfig.put("system.locale.country", "US");
					System.setProperty("locale", "en_US");
				}
			}
		});
        cbSysLang.setBounds(190, 35, 200, 25);
        panelUI.add(cbSysLang);
        
        JLabel lbStartingView = new JLabel(Messages.getString("SetupView.lbStartingView.text")); //$NON-NLS-1$
        lbStartingView.setHorizontalAlignment(SwingConstants.RIGHT);
        lbStartingView.setBounds(0, 70, 180, 25);
        panelUI.add(lbStartingView);
        
        cbStartingView = new JComboBox();
        cbStartingView.setModel(new DefaultComboBoxModel(new String[] {"MainView", "SignInView", "SetupView", "RecordView", "StaffView"}));
        cbStartingView.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		SSIConfig.put("system.startup.view", (String) cbStartingView.getSelectedItem());
        	}
        });
        cbStartingView.setBounds(190, 70, 200, 25);
        panelUI.add(cbStartingView);
        
        JLabel lbBgImg = new JLabel(Messages.getString("SetupView.lbBgImg.text")); //$NON-NLS-1$
        lbBgImg.setHorizontalAlignment(SwingConstants.RIGHT);
        lbBgImg.setBounds(0, 105, 180, 25);
        panelUI.add(lbBgImg);
        
        tfBgImg = new JTextField();
        tfBgImg.getDocument().addDocumentListener(new JValueChangedListener() {
			@Override
			void actionHandler() {
				SSIConfig.put("system.startup.background", tfBgImg.getText());
			}
		});
        tfBgImg.setEditable(false);
        tfBgImg.setBounds(190, 105, 120, 25);
        panelUI.add(tfBgImg);
        tfBgImg.setColumns(10);
        
        JButton btnChoose = new JButton(Messages.getString("SetupView.btnChoose.text")); //$NON-NLS-1$
        btnChoose.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                jfcBgImg.setSelectedFile(new File(tfBgImg.getText()));
                fileChooseiFrame.setVisible(true);
            }
        });
        btnChoose.setBounds(320, 105, 70, 25);
        panelUI.add(btnChoose);
        
        JLabel lbVerifySignInView = new JLabel(Messages.getString("SetupView.lbVerifySignInView.text")); //$NON-NLS-1$
        lbVerifySignInView.setHorizontalAlignment(SwingConstants.RIGHT);
        lbVerifySignInView.setBounds(0, 140, 180, 25);
        panelUI.add(lbVerifySignInView);
        
        JRadioButton rdbtnYes_VerifySignInView = new JRadioButton(Messages.getString("SetupView.rdbtnYes_VerifySignInView.text")); //$NON-NLS-1$
        rdbtnYes_VerifySignInView.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                pwfVerifySignInView.setEnabled(true);
            }
        });
        rdbtnYes_VerifySignInView.setSelected(true);
        rdbtnYes_VerifySignInView.setBounds(190, 140, 45, 25);
        panelUI.add(rdbtnYes_VerifySignInView);
        
        JRadioButton rdbtnNo_VerifySignInView = new JRadioButton(Messages.getString("SetupView.rdbtnNo_VerifySignInView.text")); //$NON-NLS-1$
        rdbtnNo_VerifySignInView.addActionListener(new ActionListener() {
            @Override
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
        
        JPanel panelSynth = new JPanel();
        panelSynth.setLayout(null);
        panelSynth.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("SetupView.panelSynth.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panelSynth.setBounds(100, 305, 420, 185);
        mainPanel.add(panelSynth);
        
        JLabel lbSynthVoice = new JLabel(Messages.getString("SetupView.lbSynthVoice.text")); //$NON-NLS-1$
        lbSynthVoice.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthVoice.setBounds(0, 35, 180, 25);
        panelSynth.add(lbSynthVoice);
        
        cbSynthVoice = new JComboBox();
        String[] voiceList = model.getVoiceList();
        cbSynthVoice.setModel(new DefaultComboBoxModel(voiceList));
        cbSynthVoice.getModel().addListDataListener(new ListDataListener() {
            @Override
            public void intervalRemoved(ListDataEvent e) {
            }
            
            @Override
            public void intervalAdded(ListDataEvent e) {
            }
            @Override
            public void contentsChanged(ListDataEvent e) {
            	String selectedItem = (String) ((DefaultComboBoxModel)e.getSource()).getSelectedItem();
				System.out.println("Conent Changed: " + selectedItem);
            	SSIConfig.put("synth.voiceName", selectedItem.split("-")[1]);
            }
        });
        cbSynthVoice.setBounds(190, 35, 200, 25);
        panelSynth.add(cbSynthVoice);
        
        JLabel lbSynthSpeed = new JLabel(Messages.getString("SetupView.lbSynthSpeed.text")); //$NON-NLS-1$
        lbSynthSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        lbSynthSpeed.setBounds(0, 70, 180, 25);
        panelSynth.add(lbSynthSpeed);
        
        sdSynthSpeed = new JSlider();
        sdSynthSpeed.addChangeListener(new ChangeListener() {
        	@Override
			public void stateChanged(ChangeEvent e) {
        		SSIConfig.put("synth.speed", sdSynthSpeed.getValue() + "");
        	}
        });
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
        
        sdSynthRate = new JSlider();
        sdSynthRate.addChangeListener(new ChangeListener() {
        	@Override
			public void stateChanged(ChangeEvent e) {
				SSIConfig.put("synth.sampleRate", sdSynthRate.getValue() + "");
        	}
        });
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
        
        sdSynthVolume = new JSlider();
        sdSynthVolume.addChangeListener(new ChangeListener() {
        	@Override
			public void stateChanged(ChangeEvent e) {
        		SSIConfig.put("synth.volume", sdSynthVolume.getValue() + "");
        	}
        });
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
        panelScanner.setBounds(560, 100, 420, 290);
        mainPanel.add(panelScanner);
        
        JLabel lbCode = new JLabel(Messages.getString("SetupView.lbCode.text")); //$NON-NLS-1$
        lbCode.setHorizontalAlignment(SwingConstants.RIGHT);
        lbCode.setBounds(0, 35, 180, 25);
        panelScanner.add(lbCode);
        
        cbCodeQr = new JCheckBox(Messages.getString("SetupView.cbCodeQr.text"));
        cbCodeQr.setBounds(190, 36, 45, 25);
        panelScanner.add(cbCodeQr);
        
        cbCodeDm = new JCheckBox(Messages.getString("SetupView.cbCodeDm.text"));
        cbCodeDm.setBounds(235, 35, 45, 25);
        panelScanner.add(cbCodeDm);
        
        cbCodeBar = new JCheckBox(Messages.getString("SetupView.cbCodeBar.text"));
        cbCodeBar.setBounds(280, 35, 45, 25);
        panelScanner.add(cbCodeBar);
        
        JLabel lbInterval = new JLabel(Messages.getString("SetupView.lbInterval.text")); //$NON-NLS-1$
        lbInterval.setHorizontalAlignment(SwingConstants.RIGHT);
        lbInterval.setBounds(0, 70, 180, 25);
        panelScanner.add(lbInterval);
        
        tfInterval = new JTextField();
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
        
        rdbtnYes_Ai = new JRadioButton(Messages.getString("SetupView.rdbtnYes_Ai.text"));
        rdbtnYes_Ai.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                tfAiTime.setEnabled(true);
            }
        });
        rdbtnYes_Ai.setSelected(true);
        rdbtnYes_Ai.setBounds(190, 105, 45, 25);
        panelScanner.add(rdbtnYes_Ai);
        
        JRadioButton rdbtnNo_Ai = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Ai.text")); //$NON-NLS-1$
        rdbtnNo_Ai.addActionListener(new ActionListener() {
            @Override
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
        
        rdbtnYes_Beep = new JRadioButton(Messages.getString("SetupView.rdbtnYes_Beep.text"));
        rdbtnYes_Beep.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                tfBeepTimes.setEnabled(true);
            }
        });
        rdbtnYes_Beep.setBounds(190, 140, 45, 25);
        panelScanner.add(rdbtnYes_Beep);
        
        JRadioButton rdbtnNo_Beep = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Beep.text")); //$NON-NLS-1$
        rdbtnNo_Beep.addActionListener(new ActionListener() {
            @Override
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
        tfBeepTimes.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		System.out.println("BEEP!");
        	}
        });
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
        rdbtnYes_Light.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		System.out.println("开灯！");
        		VguangApi.lightOn();
        	}
        });
        rdbtnYes_Light.setSelected(true);
        rdbtnYes_Light.setBounds(190, 170, 45, 25);
        panelScanner.add(rdbtnYes_Light);
        
        JRadioButton rdbtnNo_Light = new JRadioButton(Messages.getString("SetupView.rdbtnNo_Light.text")); //$NON-NLS-1$
        rdbtnNo_Light.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		System.out.println("关灯！");
        		VguangApi.lightOff();
        	}
        });
        rdbtnNo_Light.setBounds(235, 170, 40, 25);
        panelScanner.add(rdbtnNo_Light);
        
        ButtonGroup btng_Light = new ButtonGroup();
        btng_Light.add(rdbtnYes_Light);
        btng_Light.add(rdbtnNo_Light);
        
        JButton btnOpen = new JButton(Messages.getString("SetupView.btnOpen.text")); //$NON-NLS-1$
        btnOpen.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		VguangApi.openDevice();
        	}
        });
        btnOpen.setBounds(20, 240, 120, 25);
        panelScanner.add(btnOpen);
        
        JButton btnClose = new JButton(Messages.getString("SetupView.btnClose.text")); //$NON-NLS-1$
        btnClose.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		VguangApi.closeDevice();
                lbDeviceState.setText(Messages.getString("SetupView.lbDeviceState.inactive.text"));
                lbDeviceState.setEnabled(false);
        	}
        });
        btnClose.setBounds(150, 240, 120, 25);
        panelScanner.add(btnClose);
        
        JButton btnApply = new JButton(Messages.getString("SetupView.btnApply.text")); //$NON-NLS-1$
        btnApply.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		applySetting();
        	}
        });
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
        panelDecode.setBounds(560, 410, 420, 280);
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
        
        JPanel panelReport = new JPanel();
        panelReport.setLayout(null);
        panelReport.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("SetupView.panelReport.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-2$
        panelReport.setBounds(100, 510, 420, 180);
        mainPanel.add(panelReport);
        
        JLabel lbEmailSenderAddress = new JLabel(Messages.getString("SetupView.lbAddress.text")); //$NON-NLS-1$
        lbEmailSenderAddress.setHorizontalAlignment(SwingConstants.RIGHT);
        lbEmailSenderAddress.setBounds(0, 35, 180, 25);
        panelReport.add(lbEmailSenderAddress);
        
        tfEmailSenderAddress = new JTextField();
        tfEmailSenderAddress.getDocument().addDocumentListener(new JValueChangedListener() {
			@Override
			void actionHandler() {
				SSIConfig.put("email.address", tfEmailSenderAddress.getText());
			}
		});
        tfEmailSenderAddress.setBounds(190, 35, 200, 25);
        panelReport.add(tfEmailSenderAddress);
        tfEmailSenderAddress.setColumns(10);
        
        JLabel lbEmailSenderPwd = new JLabel(Messages.getString("SetupView.lbPassword.text")); //$NON-NLS-1$
        lbEmailSenderPwd.setHorizontalAlignment(SwingConstants.RIGHT);
        lbEmailSenderPwd.setBounds(0, 70, 180, 25);
        panelReport.add(lbEmailSenderPwd);
        
        pfEmailSenderPwd = new JPasswordField();
        pfEmailSenderPwd.setColumns(10);
        pfEmailSenderPwd.getDocument().addDocumentListener(new JValueChangedListener() {
			@Override
			void actionHandler() {
				SSIConfig.put("email.password", new String(pfEmailSenderPwd.getPassword()));
			}
		});
        pfEmailSenderPwd.setBounds(190, 70, 200, 25);
        panelReport.add(pfEmailSenderPwd);
        
        JLabel lbEmailSmtpServer = new JLabel(Messages.getString("SetupView.lbEmailRecipient.text")); //$NON-NLS-1$
        lbEmailSmtpServer.setHorizontalAlignment(SwingConstants.RIGHT);
        lbEmailSmtpServer.setBounds(0, 105, 180, 25);
        panelReport.add(lbEmailSmtpServer);
        
        tfEmailSmtpServer = new JTextField();
        tfEmailSmtpServer.setColumns(10);
        tfEmailSmtpServer.getDocument().addDocumentListener(new JValueChangedListener() {
			@Override
			void actionHandler() {
				SSIConfig.put("email.smtp", tfEmailSmtpServer.getText());
			}
		});
        tfEmailSmtpServer.setBounds(190, 105, 200, 25);
        panelReport.add(tfEmailSmtpServer);
        
        JLabel lbEmailRecipientAddress = new JLabel(Messages.getString("SetupView.lbSmtp.text")); //$NON-NLS-1$
        lbEmailRecipientAddress.setHorizontalAlignment(SwingConstants.RIGHT);
        lbEmailRecipientAddress.setBounds(0, 140, 180, 25);
        panelReport.add(lbEmailRecipientAddress);
        
        tfEmailRecipientAddress = new JTextField();
        tfEmailRecipientAddress.setColumns(10);
        tfEmailRecipientAddress.getDocument().addDocumentListener(new JValueChangedListener(){
			@Override
			void actionHandler() {
				SSIConfig.put("email.recipients", tfEmailRecipientAddress.getText());
			}
        	
        });
        tfEmailRecipientAddress.setBounds(190, 140, 200, 25);
        panelReport.add(tfEmailRecipientAddress);
        
        JButton btnAboutSoftware = new JButton(Messages.getString("SetupView.btnAboutSoftware.text")); //$NON-NLS-1$
        btnAboutSoftware.setBounds(100, 65, 120, 25);
        mainPanel.add(btnAboutSoftware);
        
        JButton btnAboutThisVersion = new JButton(Messages.getString("SetupView.btnAboutThisVersion.text")); //$NON-NLS-1$
        btnAboutThisVersion.setBounds(230, 65, 120, 25);
        mainPanel.add(btnAboutThisVersion);
        
        setDefaultValues();
        
        applyVirtualKeyboard(mainPanel, "SetupView");
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
    
    private void setDefaultValues(){
        String bgImage = SSIConfig.get("system.startup.background");
        if(!StringUtil.isEmpty(bgImage)){
            File imgFile = new File(bgImage);
            tfBgImg.setText(imgFile.getAbsolutePath());
            jfcBgImg.setSelectedFile(imgFile);
            fileChooseiFrame.doDefaultCloseAction();
        }
        
        String startView = SSIConfig.get("system.startup.view");
        if(!StringUtil.isEmpty(startView)){
            cbStartingView.setSelectedItem(startView);
        }
        
        cbCodeQr.setSelected(true);
        cbCodeDm.setSelected(true);
        cbCodeBar.setSelected(true);
        tfInterval.setText("1000");
        tfAiTime.setText("20");
        rdbtnYes_Beep.setSelected(true);
        
		String synthViceName = SSIConfig.get("synth.voiceName");
		if(!StringUtil.isEmpty(synthViceName)){
	        String[] voiceList = model.getVoiceList();
			for (int i = 0; i < voiceList.length; i++) {
				String voice = voiceList[i];
				if(voice.indexOf(synthViceName) != -1){
					cbSynthVoice.setSelectedIndex(i);
					break;
				}
			}
		}
		String synthSpeed = SSIConfig.get("synth.speed");
		if(!StringUtil.isEmpty(synthSpeed)){
			sdSynthSpeed.setValue(StringUtil.stringToInt(synthSpeed, 70));
		}
		String synthRate = SSIConfig.get("synth.sampleRate");
		if(!StringUtil.isEmpty(synthRate)){
			sdSynthRate.setValue(StringUtil.stringToInt(synthRate, 70));
		}
		String synthVolume = SSIConfig.get("synth.volume");
		if(!StringUtil.isEmpty(synthVolume)){
			sdSynthVolume.setValue(StringUtil.stringToInt(synthVolume, 50));
		}
		
		String emailAddress = SSIConfig.get("email.address");
		if(!StringUtil.isEmpty(emailAddress)){
			tfEmailSenderAddress.setText(emailAddress);
		}
		String emailPwd = SSIConfig.get("email.password");
		if(!StringUtil.isEmpty(emailPwd)){
			pfEmailSenderPwd.setText(emailPwd);
		}
		String emailRecip = SSIConfig.get("email.recipients");
		if(!StringUtil.isEmpty(emailRecip)){
			tfEmailRecipientAddress.setText(emailRecip);
		}
		String emailSmtp = SSIConfig.get("email.smtp");
		if(!StringUtil.isEmpty(emailSmtp)){
			tfEmailSmtpServer.setText(emailSmtp);
		}
        
        
        
    }
    
    //应用设置
    public void applySetting(){
        //设置QR状态
        VguangApi.setQRable(cbCodeQr.isSelected());
        //设置DM状态
        VguangApi.setDMable(cbCodeDm.isSelected());
        //设置Bar状态
        VguangApi.setBarcode(cbCodeBar.isSelected());
        
        // 设置解码间隔时间，单位毫秒
        VguangApi.setDeodeIntervalTime(StringUtil.stringToInt(tfInterval.getText(), 300));
        
        //设置自动休眠状态
        VguangApi.setAI(rdbtnYes_Ai.isSelected());
        int aiLimit = StringUtil.stringToInt(tfAiTime.getText(), 20);
        if(aiLimit < 1 || aiLimit > 64){
            aiLimit = 20;
        }
        // 设置自动休眠灵敏度
        VguangApi.setAISensitivity(aiLimit);
        // 设置自动休眠响应时间，单位秒
        VguangApi.setAIResponseTime(300);

        //设置扬声器状态
        VguangApi.setBeepable(rdbtnYes_Beep.isSelected());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHome) {
            Application.switchView(Application.MAIN_VIEW);
            SSIConfig.save();
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

    public void setDeviceStatus(final int istatus){
        //在主线程中更新UI
        EventQueue.invokeLater(new Runnable() {
            @Override
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
            @Override
			public void run() {
                tpScanResult.setText(str);
            }
        });
    }
    
    public void setMessageString(final String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                tpDecodeResult.setText(message);
            }
        });
    }
    
    public static void main(String[] args) {
        JFrame mainframe = new JFrame();
        mainframe.getContentPane().add(new SetupView());
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.pack();
        mainframe.setVisible(true);
    }
    
	abstract class JValueChangedListener implements DocumentListener { // 我想值在改变时做同一件事,所以写
		abstract void actionHandler(); // 个虚类,并实现接口方法都调用同一个

		@Override
		public void changedUpdate(DocumentEvent e) { // 待实现的方法actionHandler()
			actionHandler();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			actionHandler();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			actionHandler();
		}
	}
}
