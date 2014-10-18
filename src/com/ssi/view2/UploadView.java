package com.ssi.view2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.iflytek.Setting;
import com.iflytek.Setting.LOG_LEVEL;
import com.iflytek.speech.DataUploader;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechListener;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.SpeechUser;
import com.ssi.main.Application;
import com.ssi.util.DrawableUtils;
import com.ssi.util.Version;
import com.ssi.view.MainView;

public class UploadView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// 合成的文本内容
	private final static String TEXT_CONTENT = "王小贰\n张小山\n李四\n科大讯飞\n章栋\n";

	private JButton jbtLeft;
	private JButton jbtRight;
	private JButton jbtHome;
	private JButton jbtSetting;

	private JTextArea resultArea;

	/**
	 * 初始化按钮. 初始化按钮图片背景、大小、鼠标点击事件
	 */
	public UploadView() {
		Setting.saveLogFile(LOG_LEVEL.none, null);
		
		ImageIcon imgLeft = new ImageIcon("res/button_left.png");
		jbtLeft = DrawableUtils.createImageButton("上传", imgLeft, "center");
		jbtLeft.setBounds(0, 320, imgLeft.getIconWidth(),
				imgLeft.getIconHeight() * 4 / 5);
		DrawableUtils.setMouseListener(jbtLeft, "res/button_left");

		ImageIcon imgRight = new ImageIcon("res/button_right.png");
		jbtRight = DrawableUtils.createImageButton("识别", imgRight, "center");
		jbtRight.setBounds(330, 320, imgRight.getIconWidth(),
				imgRight.getIconHeight() * 4 / 5);
		DrawableUtils.setMouseListener(jbtRight, "res/button_right");

		ImageIcon imgHome = new ImageIcon("res/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(),
				imgHome.getIconHeight());
		DrawableUtils.setMouseListener(jbtHome, "res/home");

		ImageIcon imgSetting = new ImageIcon("res/setting.png");
		jbtSetting = DrawableUtils.createImageButton("", imgSetting, null);
		jbtSetting.setBounds(530, 20, imgSetting.getIconWidth(),
				imgSetting.getIconHeight());
		DrawableUtils.setMouseListener(jbtSetting, "res/setting");

		resultArea = new JTextArea("");
		resultArea.setBounds(40, 100, 560, 400);
		resultArea.setOpaque(false);
		resultArea.setEditable(false);
		resultArea.setLineWrap(true);
		resultArea.setForeground(Color.BLACK);
		Font font = new Font("宋体", Font.BOLD, 30);
		resultArea.setFont(font);
		resultArea.setText(TEXT_CONTENT);

		setOpaque(false);
		setLayout(null);
		add(jbtLeft);
		add(jbtRight);
		add(resultArea);
		add(jbtHome);
		add(jbtSetting);

		SpeechUser.getUser().login(null, null, "appid=" + Version.getAppid(),
				loginListener);
		// 获取识别对象
		if (SpeechRecognizer.getRecognizer() == null)
			SpeechRecognizer.createRecognizer("appid=" + Version.getAppid());

		jbtLeft.addActionListener(this);
		jbtRight.addActionListener(this);
		jbtHome.addActionListener(this);
	}

	/**
	 * 按钮监听器实现
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbtLeft) {
			try {
				//上传之前必须先登陆
				if (SpeechUser.getUser().getLoginState().toString() == "Logined") {
					String contacts = TEXT_CONTENT;
					byte[] datas = contacts.getBytes("utf-8");
					DataUploader uploader = new DataUploader();
					uploader.uploadData(uploadListener, "contacts",
							"subject=uup,data_type=contact", datas);
				}
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == jbtRight) {
			SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
			recognizer.startListening(resultListener, "sms", null, null);
		} else if (e.getSource() == jbtHome) {
			JFrame frame = Application.MAIN_FRAME;
			frame.getContentPane().remove(this);
			JPanel panel = ((MainView) frame).getMainJpanel();
			frame.getContentPane().add(panel);
			frame.getContentPane().validate();
			frame.getContentPane().repaint();
		}

	}

	SpeechListener uploadListener = new SpeechListener() {

		public void onData(byte[] buffer) {

		}

		public void onEnd(SpeechError error) {
			if (error == null)
				resultArea.setText("上传成功");
			else
				resultArea.setText(error.toString());
		}

		@Override
		public void onEvent(int eventType, String params) {

		}

	};

	private RecognizerListener resultListener = new RecognizerListener() {

		@Override
		public void onCancel() {
		}

		@Override
		public void onEnd(SpeechError mLastError) {

		}

		@Override
		public void onBeginOfSpeech() {
		}

		@Override
		public void onEndOfSpeech() {

		}

		/**
		 * 获取识别结果. 获取ArrayList类型的识别结果，并对结果进行累加，显示到Area里
		 */
		@Override
		public void onResults(@SuppressWarnings("rawtypes") ArrayList results, boolean islast) {
			String text = "";
			for (int i = 0; i < results.size(); i++) {
				RecognizerResult result = (RecognizerResult) results.get(i);
				text += result.text;
			}
			resultArea.append(text);

			if (resultArea.getText().length() >= 110)
				resultArea.setText("");
		}

		@Override
		public void onVolumeChanged(int volume) {
			if (volume == 0)
				volume = 1;
			else if (volume >= 6)
				volume = 6;
		}
	};

	SpeechListener loginListener = new SpeechListener() {

		@Override
		public void onData(byte[] buffer) {
		}

		@Override
		public void onEnd(SpeechError error) {
			if (error == null)
				System.out.println("LoginState:"
						+ SpeechUser.getUser().getLoginState());

		}

		@Override
		public void onEvent(int eventType, String params) {

		}

	};
}
