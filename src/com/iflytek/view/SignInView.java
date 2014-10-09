package com.iflytek.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.util.DrawableUtils;
import com.iflytek.util.Version;

public class SignInView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// 合成的文本内容
	private final static String TEXT_CONTENT = "安徽科大讯飞信息科技股份有限公司是一家专业从事智能语音及语言技术研究。";

	private JButton jbtLeft;
	private JButton jbtRight;
	private JButton jbtHome;
	private JButton jbtSetting;

	private JTextArea resultArea;

	/**
	 * 初始化按钮. 初始化按钮图片背景、大小、鼠标点击事件
	 */
	public SignInView() {
		ImageIcon imgLeft = new ImageIcon("res/button_left.png");
		jbtLeft = DrawableUtils.createImageButton("合成", imgLeft, "center");
		jbtLeft.setBounds(0, 320, imgLeft.getIconWidth(),
				imgLeft.getIconHeight() * 4 / 5);
		DrawableUtils.setMouseListener(jbtLeft, "res/button_left");

		ImageIcon imgRight = new ImageIcon("res/button_right.png");
		jbtRight = DrawableUtils.createImageButton("取消", imgRight, "center");
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

		if (SynthesizerPlayer.getSynthesizerPlayer() == null)
			SynthesizerPlayer.createSynthesizerPlayer("appid="
					+ Version.getAppid());

		jbtLeft.addActionListener(this);
		jbtRight.addActionListener(this);
		jbtHome.addActionListener(this);
	}

	/**
	 * 按钮监听器实现
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SynthesizerPlayer synthesizer = SynthesizerPlayer
				.getSynthesizerPlayer();
		if (e.getSource() == jbtLeft) {
			// 设置发音人为小宇
			synthesizer.setVoiceName("xiaoyu");
			// 设置朗读速度为50
			synthesizer.setSpeed(50);
			// 合成文本为TEXT_CONTENT的句子，设置监听器为mSynListener
			synthesizer.playText(resultArea.getText().trim(), null,
					mSynListener);
		} else if (e.getSource() == jbtRight) {
			if (synthesizer.isAvaible()) {
				synthesizer.cancel();
			}
		} else if (e.getSource() == jbtHome) {
			JFrame frame = MainView.getFrame();
			frame.getContentPane().remove(this);
			JPanel panel = ((MainView) frame).getMainJpanel();
			frame.getContentPane().add(panel);
			frame.getContentPane().validate();
			frame.getContentPane().repaint();
		}

	}

	private SynthesizerPlayerListener mSynListener = new SynthesizerPlayerListener() {

		@Override
		public void onEnd(SpeechError error) {
		}

		@Override
		public void onBufferPercent(int percent, int beginPos, int endPos,
				String args) {

		}

		@Override
		public void onPlayBegin() {

		}

		@Override
		public void onPlayPaused() {

		}

		@Override
		public void onPlayPercent(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onPlayResumed() {

		}
	};
}
