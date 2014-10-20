package com.ssi.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.ssi.main.Application;
import com.ssi.main.model.SignInModel;
import com.ssi.util.DrawableUtils;
import com.ssi.util.StringUtil;
import com.ssi.util.Version;

public class SignInView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 4089298273304918969L;
	
	private JButton jbtHome;
	private JTextPane resultArea;
	private String lastCallBack = "";
	private SignInModel model = new SignInModel();
	
	private int totalPaddingTop;
	private int totalPaddingLeft;
	
	/**
	 * 初始化按钮. 初始化按钮图片背景、大小、鼠标点击事件
	 */
	public SignInView() {
        Dimension frameSize = Application.MAIN_FRAME.getSize();
        int frameWidth = (int)frameSize.getWidth();
        int frameHeight = (int)frameSize.getHeight();
        
        totalPaddingTop = (int) (3.5 * frameHeight / 12);
        totalPaddingLeft = (int) (1 * frameWidth / 12);
        
        setOpaque(false);
        setLayout(null);
        
	    ImageIcon imgHome = new ImageIcon("res/img/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		DrawableUtils.setMouseListener(jbtHome, "res/img/home");
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(),
				imgHome.getIconHeight());
		add(jbtHome);
		
        SimpleAttributeSet bSet = new SimpleAttributeSet();  
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(bSet, Color.blue);
        StyleConstants.setFontFamily(bSet, "宋体");
        StyleConstants.setBold(bSet, true);
        int fontSize = frameHeight / 11;
		StyleConstants.setFontSize(bSet, fontSize);
   
        resultArea = new JTextPane(){
			private static final long serialVersionUID = 2734359566090502835L;

			public boolean getScrollableTracksViewportWidth() {  
        	    return false;  
        	}  
        };
        resultArea.setText("");  
        StyledDocument doc = resultArea.getStyledDocument();  
        doc.setParagraphAttributes(0, 0, bSet, false);  
		
		int compWidth = 10 * frameWidth / 12;
		int compHeight = (int) (4.5 * frameHeight / 12);
		
		resultArea.setOpaque(false);
		addComponent(resultArea, 0, 0, compWidth, compHeight);

		if (SynthesizerPlayer.getSynthesizerPlayer() == null)
			SynthesizerPlayer.createSynthesizerPlayer("appid=" + Version.getAppid());

		jbtHome.addActionListener(this);
	}
	
	private void addComponent(JComponent comp, int x, int y, int width, int height){
		comp.setBounds(this.totalPaddingLeft + x, this.totalPaddingTop + y, width, height);
		this.add(comp);
	}
	
	/**
	 * 按钮监听器实现
	 */
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
    	resultArea.setText("");
    	
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
            synthesizer.setSpeed(70);
            // 合成文本为TEXT_CONTENT的句子，设置监听器为mSynListener
            String message = model.lookupMessage(callback);
            if(!StringUtil.isEmpty(message)){
            	synthesizer.playText(message, null, mSynListener);
            	resultArea.setText(message);
            }
            
            lastCallBack = callback;
        }
    }

	public void initDataMap() {
		this.model.initDataMap();
	}
}
