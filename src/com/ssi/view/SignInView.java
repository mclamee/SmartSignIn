package com.ssi.view;

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
import com.iflytek.speech.SpeechConfig.RATE;
import com.wicky.util.DrawableUtils;
import com.wicky.util.Version;

public class SignInView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton jbtHome;

	private JTextArea resultArea;

	private String lastCallBack = "";
	
	/**
	 * 初始化按钮. 初始化按钮图片背景、大小、鼠标点击事件
	 */
	public SignInView() {

	    ImageIcon imgHome = new ImageIcon("res/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(),
				imgHome.getIconHeight());
		DrawableUtils.setMouseListener(jbtHome, "res/home");

		resultArea = new JTextArea("");
		resultArea.setBounds(40, 230, 560, 400);
		resultArea.setOpaque(false);
		resultArea.setEditable(false);
		resultArea.setLineWrap(true);
		resultArea.setForeground(Color.BLACK);
		Font font = new Font("宋体", Font.BOLD, 70);
		resultArea.setFont(font);
		resultArea.setText("王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。王先生，欢迎光临拓德公司。");

		setOpaque(false);
		setLayout(null);
		add(resultArea);
		add(jbtHome);

		if (SynthesizerPlayer.getSynthesizerPlayer() == null)
			SynthesizerPlayer.createSynthesizerPlayer("appid="
					+ Version.getAppid());

		jbtHome.addActionListener(this);
		
		
	      // FULL SCREEN
//      JButton fullsButton = new JButton("全屏显示");  
//      fullsButton.setBounds(0, 413, 93, 23);
//      fullsButton.addActionListener(new java.awt.event.ActionListener() {  
//          public void actionPerformed(java.awt.event.ActionEvent evt) {  
//              GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
//              //通过调用GraphicsEnvironment的getDefaultScreenDevice方法获得当前的屏幕设备了  
//              GraphicsDevice gd = ge.getDefaultScreenDevice();  
//              // 全屏设置  
//              gd.setFullScreenWindow(frame);  
//          }  
//      });  
//      this.add(fullsButton);  
	}

	/**
	 * 按钮监听器实现
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == jbtHome) {
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

    /**
     * <b>Decode call back</b>: 
     * @param callback string
     * @author williamz@synnex.com
     */
    public void setResultString(String callback) {
        SynthesizerPlayer synthesizer = SynthesizerPlayer
                .getSynthesizerPlayer();
        
        System.out.println("Synthesizer state: " + synthesizer.getState());
        
        if(!lastCallBack.equals(callback) || !"PLAYING".equals(synthesizer.getState().toString())) {
            if("PLAYING".equals(synthesizer.getState().toString())) {
                synthesizer.cancel();
            }
            synthesizer.setVolume(100);
            
            synthesizer.setSampleRate(RATE.rate22k);
            // 设置发音人为小宇
            synthesizer.setVoiceName("xiaoyan");
            // 设置朗读速度为50
            synthesizer.setSpeed(60);
            // 合成文本为TEXT_CONTENT的句子，设置监听器为mSynListener
            synthesizer.playText(callback.trim() + "，王先生、欢迎光临", null, mSynListener);
            
            lastCallBack = callback;
        }
    }
}
