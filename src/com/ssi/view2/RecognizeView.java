package com.ssi.view2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechRecognizer;
import com.ssi.view.MainView;
import com.wicky.util.DrawableUtils;
import com.wicky.util.Version;


public class RecognizeView extends JPanel implements ActionListener {	
	private static final long serialVersionUID = 1L;
	
	JLabel labelWav;
	
	private JButton jbtEntire;
	private JButton jbtLeft;
	private JButton jbtRight;	
	private JButton jbtHome;
	
	JTextArea resultArea;
	
	private boolean mState = true;
	
	/**
	 * 初始化按钮对象.
	 * 设置按钮背景图片、大小、鼠标点击事件
	 * 初始化文本框，设置字体类型、大小
	 */
	public RecognizeView()
	{		
		
		ImageIcon imgEntire = new ImageIcon("res/button.png");
		jbtEntire = DrawableUtils.createImageButton("开始说话", imgEntire, null);		
		jbtEntire.setBounds(0, 320, imgEntire.getIconWidth(), imgEntire.getIconHeight()*4/5);
		DrawableUtils.setMouseListener(jbtEntire, "res/button");
		
		ImageIcon imgLeft = new ImageIcon("res/button_left.png");
		jbtLeft = DrawableUtils.createImageButton("说完了", imgLeft, "right");		
		jbtLeft.setBounds(0, 320, imgLeft.getIconWidth(), imgLeft.getIconHeight()*4/5);
		DrawableUtils.setMouseListener(jbtLeft, "res/button_left");
		
		ImageIcon img = new ImageIcon("res/mic_01.png");
		labelWav = new JLabel(img);
		labelWav.setBounds(0, 0, img.getIconWidth(), img.getIconHeight()*4/5);
		jbtLeft.add(labelWav, BorderLayout.WEST);
		
		ImageIcon imgRight = new ImageIcon("res/button_right.png");
		jbtRight = DrawableUtils.createImageButton("取消", imgRight, null);		
		jbtRight.setBounds(330, 320, imgRight.getIconWidth(), imgRight.getIconHeight()*4/5);
		DrawableUtils.setMouseListener(jbtRight, "res/button_right");	
		
		ImageIcon imgHome =  new ImageIcon("res/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(), imgHome.getIconHeight());
		DrawableUtils.setMouseListener(jbtHome, "res/home");
		
		setButtonVisible(false);
		
		resultArea = new JTextArea("");
		resultArea.setBounds(40, 100, 560, 400);
		resultArea.setOpaque(false);
		resultArea.setEditable(false);
		resultArea.setLineWrap(true);
		resultArea.setForeground(Color.BLACK);
		Font font=new Font("宋体", Font.BOLD, 30);
		resultArea.setFont(font);
		
		setOpaque(false);
		
		setLayout(null);
		add(jbtEntire);
		add(jbtLeft);
		add(jbtRight);
		add(resultArea);
		add(jbtHome);
		
		//获取识别对象
		if(SpeechRecognizer.getRecognizer() == null)
			SpeechRecognizer.createRecognizer("appid=" + Version.getAppid());
		
		jbtEntire.addActionListener(this);
		jbtLeft.addActionListener(this);
		jbtHome.addActionListener(this);
		jbtRight.addActionListener(this);	
	}

	private void setButtonVisible(boolean tag)
	{
		jbtLeft.setVisible(tag);
		jbtRight.setVisible(tag);
		jbtEntire.setVisible(!tag);
	}
	
	/***
	 * 监听器实现.
	 * 按钮按下动作实现
	 */
	public void actionPerformed(ActionEvent e) {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();		
		if(e.getSource() == jbtEntire){
			setButtonVisible(true);
			//开始监听语音输入
			recognizer.startListening(resultListener, "sms", null, null);
			mState = false;
		}else if(e.getSource() == jbtLeft){
			if(mState)
				recognizer.startListening(resultListener, "sms", null, null);
			else
				recognizer.stopListening();
		}else if(e.getSource() == jbtRight){
			recognizer.cancel();
		}else if(e.getSource() == jbtHome)
		{
			JFrame frame = MainView.getFrame();
			frame.getContentPane().remove(this);
			JPanel panel = ((MainView) frame).getMainJpanel();
			frame.getContentPane().add(panel);
			frame.getContentPane().validate();
			frame.getContentPane().repaint(); 
		}
			
}
	
	private RecognizerListener resultListener = new RecognizerListener(){

		@Override
		public void onCancel() {
		}

		@Override
		public void onEnd(SpeechError mLastError) {		
			mState = true;
		}

		@Override
		public void onBeginOfSpeech() {			
		}

		@Override
		public void onEndOfSpeech() {
			
		}

		/**
		 * 获取识别结果.
		 * 获取ArrayList类型的识别结果，并对结果进行累加，显示到Area里
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onResults(ArrayList results,
				boolean islast) {
			String text = "";
			for(int i = 0; i < results.size(); i++)
			{
				RecognizerResult result = (RecognizerResult)results.get(i);
				text += result.text;
			}
			resultArea.append(text);
			
			if(resultArea.getText().length() >= 30)
				resultArea.setText("");
		}

		@Override
		public void onVolumeChanged(int volume) {
			if(volume == 0)
				volume = 1;
			else if(volume >= 6)
				volume = 6;
			labelWav.setIcon(new ImageIcon("res/mic_0" + volume + ".png"));	
			
		}
	};

}
