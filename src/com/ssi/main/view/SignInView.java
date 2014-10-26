package com.ssi.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.SignInModel;
import com.ssi.util.DrawableUtils;
import com.ssi.util.StringUtil;
import com.ssi.util.Version;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public class SignInView extends JPanel implements IView, ActionListener {
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
        totalPaddingLeft = 1 * frameWidth / 12;
        
        setOpaque(false);
        setLayout(null);
        
	    ImageIcon imgHome = new ImageIcon("res/img/home.png");
		jbtHome = DrawableUtils.createImageButton("", imgHome, null);
		DrawableUtils.setMouseListener(jbtHome, "res/img/home");
		jbtHome.setBounds(20, 20, imgHome.getIconWidth(),
				imgHome.getIconHeight());
		add(jbtHome);

		if(Application.debugMode){
			final JTextField input = new JTextField();
	        input.setText("");
	        input.setBounds(320, 15, 500, 25);
	        this.add(input);
			
	        JButton btnAdd = new JButton("测试输入");
	        Dimension btnSize = new Dimension(150, 25);
	        btnAdd.setPreferredSize(btnSize);
	        btnAdd.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	SignInView.this.setResultString(input.getText());
	            }
	        });
	        btnAdd.setBounds(100, 15, 150, 25);
	        this.add(btnAdd);
		}
		
        SimpleAttributeSet bSet = new SimpleAttributeSet();  
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(bSet, Color.blue);
        StyleConstants.setFontFamily(bSet, "宋体");
        StyleConstants.setBold(bSet, true);
        int fontSize = frameHeight / 11;
		StyleConstants.setFontSize(bSet, fontSize);
   
        resultArea = new JTextPane(){
			private static final long serialVersionUID = 2734359566090502835L;

			@Override
			public boolean getScrollableTracksViewportWidth() {  
        	    return false;  
        	}  
        };
        resultArea.setEditable(false);
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
	        Application.switchView(Application.MAIN_VIEW);
		}
	}

	private SynthesizerPlayerListener mSynListener = new SynthesizerPlayerListener() {

		@Override
		public void onEnd(SpeechError error) {
		  	resultArea.setText("");
	    	SignInView.this.revalidate();
	    	SignInView.this.repaint();
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
            // 设置音量
            String synthVolume = SSIConfig.get("synth.volume");
    		if(StringUtil.isEmpty(synthVolume)){
    			synthVolume = "50";
    			SSIConfig.put("synth.volume", synthVolume);
    		}
            synthesizer.setVolume(StringUtil.stringToInt(synthVolume, 50));
            
            // 设置采样率
            String synthRate = SSIConfig.get("synth.sampleRate");
    		if(StringUtil.isEmpty(synthRate)){
    			synthRate = "80";
    			SSIConfig.put("synth.sampleRate", synthRate);
    		}
    		RATE sample = RATE.rate16k;
    		if(synthRate.equals("0")){
    			sample = RATE.rate8k;
    		}
			if(synthRate.equals("40")){
				sample = RATE.rate11k;
			}
			if(synthRate.equals("80")){
				sample = RATE.rate16k;
			}
			if(synthRate.equals("120")){
				sample = RATE.rate22k;
			}    
            synthesizer.setSampleRate(sample);
            // 设置发音人
            String synthViceName = SSIConfig.get("synth.voiceName");
    		if(StringUtil.isEmpty(synthViceName)){
    			synthViceName = "xiaoyan";
    			SSIConfig.put("synth.voiceName", synthViceName);
    		}
            synthesizer.setVoiceName(synthViceName);
            // 设置朗读速度
            String synthSpeed = SSIConfig.get("synth.speed");
    		if(StringUtil.isEmpty(synthSpeed)){
    			synthSpeed = "70";
    			SSIConfig.put("synth.speed", synthSpeed);
    		}
            synthesizer.setSpeed(StringUtil.stringToInt(synthSpeed, 70));
            String message = model.lookupMessage(callback);
            if(!StringUtil.isEmpty(message)){
                // 合成文本为message的句子，设置监听器为mSynListener
            	synthesizer.playText(message, null, mSynListener);
            	resultArea.setText(message);
            	Application.SETUP_VIEW.setMessageString(message);
            }
            
            Application.SETUP_VIEW.setResultString(callback);
            
            lastCallBack = callback;
        }
    }

	public void initDataMap() {
		this.model.initDataMap();
	}
	
	@Override
	public void closeSubDialog(){
	}
	
	@Override
	public void openSubDialog(String title, String message, IDataVector<ISubDataVector> data) {
	}

	@Override
	public String getTemplate() {
		return null;
	}
}
