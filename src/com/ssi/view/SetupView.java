package com.ssi.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ssi.main.Application;
import com.vguang.VguangApi;
import com.wicky.util.DrawableUtils;

public class SetupView extends JPanel implements ActionListener{

	private static final long serialVersionUID = 8096656238618262028L;
	
	private JButton jbtHome;
	private JTextArea decodeTextArea;
	private JLabel lblDeviceStatus;
	private JTextField textDecodeTime;
	private JTextField textAiLimit;
	private JTextField textAiResponeTime;
	private JCheckBox chckbxQr;
	private JCheckBox checkboxDm;
	private JCheckBox chckbxBar;
	private JCheckBox chckbxBeep;
	private JCheckBox chckbxAi;

	public static Locale currentLocale = new Locale("zh", "CN");
	
	/**
	 * Create the application.
	 */
	public SetupView() {
        Dimension frameSize = Application.MAIN_FRAME.getSize();
        int frameWidth = (int)frameSize.getWidth();
        int frameHeight = (int)frameSize.getHeight();
        
		//初始化控件
        this.setOpaque(false);
        this.setLayout(null);
        
		ImageIcon imgHome = new ImageIcon("res/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(),
				imgHome.getIconHeight());
		DrawableUtils.setMouseListener(jbtHome, "res/home");
		jbtHome.addActionListener(this);
		this.add(jbtHome);
		
		
		JButton buttonBegin = new JButton("打开设备");
		buttonBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VguangApi.openDevice();
			}
		});
		
		
		int btnLine1YIdx = 180 + 380;
		int btnLine2YIdx = 180 + 413;
		
		int inputLineYIdx = 180 + 78;
		int statusLineYIdx = 180 + 324;
		
        buttonBegin.setBounds(123, btnLine2YIdx, 93, 23);
		this.add(buttonBegin);
		
		JButton buttonEnd = new JButton("关闭设备");
		buttonEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.closeDevice();
				lblDeviceStatus.setText("设备无效");
				lblDeviceStatus.setEnabled(false);
			}
		});
		buttonEnd.setBounds(347, btnLine2YIdx, 93, 23);
		this.add(buttonEnd);
		
		JButton buttonQuit = new JButton("退出程序");
		buttonQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.closeDevice();
				System.exit(0);
			}
		});
		buttonQuit.setBounds(464, btnLine2YIdx, 93, 23);
		this.add(buttonQuit);
		
		decodeTextArea = new JTextArea();
		decodeTextArea.setRows(5);
		decodeTextArea.setLineWrap(true);
		decodeTextArea.setColumns(10);
		decodeTextArea.setBounds(113, inputLineYIdx, 347, 211);
		this.add(decodeTextArea);
		
		JLabel label = new JLabel("解码结果：");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("宋体", Font.BOLD, 14));
		label.setBounds(10, inputLineYIdx, 93, 23);
		this.add(label);
		
		chckbxQr = new JCheckBox("QR");
		chckbxQr.setSelected(true);
		chckbxQr.setBounds(567, 60, 87, 23);
		this.add(chckbxQr);
		
		checkboxDm = new JCheckBox("DM");
		checkboxDm.setSelected(true);
		checkboxDm.setBounds(567, 87, 103, 23);
		this.add(checkboxDm);
		
		chckbxBar = new JCheckBox("条形码");
		chckbxBar.setSelected(true);
		chckbxBar.setBounds(567, 112, 103, 23);
		this.add(chckbxBar);
		
		chckbxBeep = new JCheckBox("蜂鸣器");
		chckbxBeep.setSelected(true);
		chckbxBeep.setBounds(581, 311, 103, 23);
		this.add(chckbxBeep);
		
		chckbxAi = new JCheckBox("自动休眠");
		chckbxAi.setSelected(true);
		chckbxAi.setBounds(567, 201, 103, 23);
		this.add(chckbxAi);
		
		JLabel lblNewLabel = new JLabel("灵敏度：");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(533, 233, 66, 15);
		this.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("响应时间：");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(533, 264, 66, 15);
		this.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("解码间隔时间：");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(507, 144, 93, 15);
		this.add(lblNewLabel_2);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(507, 168, 191, 2);
		this.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(507, 49, 191, 2);
		this.add(separator_1);
		
		JLabel label_1 = new JLabel("码制：");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("宋体", Font.BOLD, 14));
		label_1.setBounds(507, 60, 54, 23);
		this.add(label_1);
		
		JLabel lblNewLabel_3 = new JLabel("自动休眠：");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_3.setBounds(507, 180, 93, 15);
		this.add(lblNewLabel_3);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(518, 303, 180, 2);
		this.add(separator_2);
		
		JLabel lblNewLabel_4 = new JLabel("蜂鸣器：");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_4.setBounds(507, 315, 66, 15);
		this.add(lblNewLabel_4);
		
		JButton buttonBeep1 = new JButton("响一声");
		buttonBeep1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.beep(1);
			}
		});
		buttonBeep1.setBounds(265, btnLine1YIdx, 93, 23);
		this.add(buttonBeep1);
		
		JButton buttonBeep2 = new JButton("响二声");
		buttonBeep2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.beep(2);
			}
		});
		buttonBeep2.setBounds(374, btnLine1YIdx, 93, 23);
		this.add(buttonBeep2);
		
		JButton buttonBeep3 = new JButton("响三声");
		buttonBeep3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.beep(3);
			}
		});
		buttonBeep3.setBounds(477, btnLine1YIdx, 93, 23);
		this.add(buttonBeep3);
		
		JLabel lblNewLabel_5 = new JLabel("设备状态：");
		lblNewLabel_5.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_5.setBounds(10, statusLineYIdx, 93, 23);
		this.add(lblNewLabel_5);
		
		lblDeviceStatus = new JLabel("设备无效");
		lblDeviceStatus.setBounds(122, statusLineYIdx, 93, 23);
		lblDeviceStatus.setEnabled(false);
		this.add(lblDeviceStatus);
		
		JButton buttonLightOn = new JButton("开灯");
		buttonLightOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.lightOn();
			}
		});
		buttonLightOn.setBounds(113, btnLine1YIdx, 66, 23);
		this.add(buttonLightOn);
		
		JButton buttonLightOff = new JButton("关灯");
		buttonLightOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VguangApi.lightOff();
			}
		});
		buttonLightOff.setBounds(189, btnLine1YIdx, 66, 23);
		this.add(buttonLightOff);
		
		JLabel label_2 = new JLabel("毫秒");
		label_2.setBounds(684, 144, 32, 15);
		this.add(label_2);
		
		JLabel lblNewLabel_11 = new JLabel("秒");
		lblNewLabel_11.setBounds(684, 315, 32, 15);
		this.add(lblNewLabel_11);
		
		textDecodeTime = new JTextField();
		textDecodeTime.setText("1000");
		textDecodeTime.setBounds(604, 141, 66, 21);
		this.add(textDecodeTime);
		textDecodeTime.setColumns(10);
		
		textAiLimit = new JTextField();
		textAiLimit.setText("20");
		textAiLimit.setBounds(604, 230, 66, 21);
		this.add(textAiLimit);
		textAiLimit.setColumns(10);
		
		textAiResponeTime = new JTextField();
		textAiResponeTime.setText("300");
		textAiResponeTime.setBounds(604, 261, 66, 21);
		this.add(textAiResponeTime);
		textAiResponeTime.setColumns(10);
		
		JButton btnNewButton = new JButton("更新设置");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySetting();
			}
		});
		btnNewButton.setBounds(234, btnLine2YIdx, 93, 23);
		this.add(btnNewButton);

	}
	
	//应用设置
    public void applySetting(){
        //设置QR状态
        VguangApi.setQRable(chckbxQr.isSelected());
        //设置DM状态
        VguangApi.setDMable(checkboxDm.isSelected());
        //设置Bar状态
        VguangApi.setBarcode(chckbxBar.isSelected());
        
        // 设置解码间隔时间，单位毫秒
        VguangApi.setDeodeIntervalTime(StringToInt(textDecodeTime.getText(), 300));
        
        //设置自动休眠状态
        VguangApi.setAI(chckbxAi.isSelected());
        int aiLimit = StringToInt(textAiLimit.getText(), 20);
        if(aiLimit < 1 || aiLimit > 64){
            aiLimit = 20;
        }
        // 设置自动休眠灵敏度
        VguangApi.setAISensitivity(aiLimit);
        // 设置自动休眠响应时间，单位秒
        VguangApi.setAIResponseTime(StringToInt(textAiResponeTime.getText(), 30));

        //设置扬声器状态
        VguangApi.setBeepable(chckbxBeep.isSelected());
    }
	
	public void setResultString(final String str){
		//在主线程中更新UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				decodeTextArea.setText(str);
			}
		});
	}
	
	public void setDeviceStatus(final int istatus){
		//在主线程中更新UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if(istatus == VguangApi.DEVICE_VALID){
					lblDeviceStatus.setText("设备有效");
					lblDeviceStatus.setEnabled(true);
				}else{
					lblDeviceStatus.setText("设备无效");
					lblDeviceStatus.setEnabled(false);
				}
			}
		});
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtHome) {
            JFrame frame = Application.MAIN_FRAME;
            frame.getContentPane().remove(this);
            JPanel panel = ((MainView) frame).getMainJpanel();
            frame.getContentPane().add(panel);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        }
    }
}
