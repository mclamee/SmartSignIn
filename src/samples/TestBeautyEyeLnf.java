package samples;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.SystemColor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.Field;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.Keymap;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;

import com.ssi.main.SSIConfig;
import com.ssi.util.DrawableUtils;
import com.ssi.util.StringUtil;

public class TestBeautyEyeLnf extends JPanel {
	private static final long serialVersionUID = 2993212480674031301L;
	private int frameWidth;
	private int frameHeight;
	private JButton btnHome;
	private JDesktopPane desktopPane;
	private JInternalFrame fileChooseiFrame;
	private JPanel mainPanel;
	
	private static Robot robot = null;
    static {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }
	
    private static void pressKey(int keyvalue) {
        robot.keyPress(keyvalue); // 按下按键
        robot.keyRelease(keyvalue); // 释放按键
    }
    
    public static void input(String str){
        if(str!=null){
        	Field[] fields = KeyEvent.class.getFields();
        	for(Field f: fields){
        		if(f.getName().equals("VK_"+StringUtil.trimAndUpper(str))){
        			int key;
					try {
						key = f.getInt(KeyEvent.class);
						pressKey(key);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
        		}
        	}
        }
    }
    
	class MouseAdp extends MouseAdapter {
		private Component respComp;

		public MouseAdp(Component source) {
			this.respComp = source;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
				responseAction(source);
			
		}

		private void responseAction(Object source) {
			if (source instanceof JLabel) {
				TestBeautyEyeLnf.input(((JLabel) source).getText());
			}
		}
	    
		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JLabel) {
				((JLabel) source).setBackground(Color.YELLOW);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JLabel) {
				((JLabel) source).setBackground(Color.LIGHT_GRAY);
			}
		}

		public Component getRespComp() {
			return respComp;
		}

		public void setRespComp(Component respComp) {
			this.respComp = respComp;
		}
	}
	
	public TestBeautyEyeLnf() {
        frameWidth = 1366;
        frameHeight = 768;

        this.setSize(frameWidth, frameHeight);
        
        ImageIcon imgHome = new ImageIcon("res/img/home.png");
        btnHome = DrawableUtils.createImageButton("", imgHome, null);
        btnHome.setBounds(20, 20, imgHome.getIconWidth(),
                imgHome.getIconHeight());
        DrawableUtils.setMouseListener(btnHome, "res/img/home");
        this.add(btnHome);
        setLayout(new BorderLayout(0, 0));
        
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(SystemColor.window);
        add(desktopPane);
        
        fileChooseiFrame = new JInternalFrame("TITLE");
        fileChooseiFrame.setBounds(200, 145, 650, 270);
        fileChooseiFrame.setClosable(true);
        fileChooseiFrame.setResizable(true);
        fileChooseiFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        fileChooseiFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
            }
        });
        
        fileChooseiFrame.getContentPane().setLayout(new BorderLayout());
        desktopPane.add(fileChooseiFrame);
        fileChooseiFrame.setVisible(true);
        
        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, frameWidth, frameHeight);
        desktopPane.add(mainPanel);
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);
        
        JPanel panelInput = new JPanel(new FlowLayout());
        panelInput.add(new JLabel("Password: "));
//        JPasswordField pwd = new JPasswordField(15);
//		panelInput.add(pwd);
		
		JTextField field = new JTextField(20);
		panelInput.add(field);
        
		final JPanel createKeyBordPanel = createKeyBordPanel(field);
		
		mainPanel.add(createKeyBordPanel);
		createKeyBordPanel.setVisible(false);
		
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				createKeyBordPanel.setVisible(false);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				createKeyBordPanel.setVisible(true);
				createKeyBordPanel.setBounds((int)e.getComponent().getLocation().getX(), 
						(int)e.getComponent().getLocation().getY(), 650, 270);
			}
		});
		
		panelInput.setBounds(0, 100, 400, 50);
		panelInput.setBorder(BorderFactory.createTitledBorder("This is a title"));
		
		mainPanel.add(panelInput);
		

		
//		JPanel createKeyBordPanel = createKeyBordPanel(field);
//		createKeyBordPanel.add(field);
//		fileChooseiFrame.add(createKeyBordPanel, BorderLayout.CENTER);
//		fileChooseiFrame.setFocusable(false);
	}


	private JPanel createKeyBordPanel(Component source) {
		MouseAdapter mouseAdapter = new MouseAdp(source);
		JPanel contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.addMouseListener(mouseAdapter);
        GridLayout gridlayout = new GridLayout(4, 1); 
        gridlayout.setHgap(0);
        contentPane.setLayout(gridlayout);
        
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 15, 0);
		JPanel panel1 = new JPanel(flowLayout);
		panel1.setFocusable(false);
        contentPane.add(panel1);
        JPanel panel2 = new JPanel(flowLayout);
        panel2.setFocusable(false);
        contentPane.add(panel2);
        JPanel panel3 = new JPanel(flowLayout);
        panel3.setFocusable(false);
        contentPane.add(panel3);
        JPanel panel4 = new JPanel(flowLayout);
        panel4.setFocusable(false);
        contentPane.add(panel4);
        
        char[] row1 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}; 
        char[] row2 = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'}; 
        char[] row3 = new char[]{'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'}; 
        char[] row4 = new char[]{'z', 'x', 'c', 'v', 'b', 'n', 'm'}; 
        
        for(char i : row1){
        	JLabel comp = createKeyBordLabel(i, mouseAdapter);
        	panel1.add(comp);
        }
        for(char i : row2){
        	JLabel comp = createKeyBordLabel(i, mouseAdapter);
        	panel2.add(comp);
        }
        for(char i : row3){
        	JLabel comp = createKeyBordLabel(i, mouseAdapter);
        	panel3.add(comp);
        }
        for(char i : row4){
        	JLabel comp = createKeyBordLabel(i, mouseAdapter);
        	panel4.add(comp);
        }
        contentPane.setBorder(BorderFactory.createEtchedBorder());
        source.requestFocusInWindow();
		return contentPane;
	}


	private JLabel createKeyBordLabel(char i, MouseAdapter mouseAdapter) {
		JLabel comp = new JLabel((char)i+"", SwingConstants.CENTER);
		comp.setPreferredSize(new Dimension(45,45));
		comp.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.DARK_GRAY));
		comp.setOpaque(true);
		comp.setBackground(Color.LIGHT_GRAY);
		comp.setForeground(Color.DARK_GRAY);
		comp.setFont(new Font("Tahoma",Font.PLAIN,35));
		comp.addMouseListener(mouseAdapter);
		comp.setFocusable(false);
		comp.setEnabled(false);
		return comp;
	}


	public static void main(String[] args) {
//        try {
//        	BeautyEyeLNFHelper.frameBorderStyle = FrameBorderStyle.osLookAndFeelDecorated;
//            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//            UIManager.put("RootPane.setupButtonVisible", false);
//            BeautyEyeLNFHelper.launchBeautyEyeLNF();
//        } catch (final Exception r) {
//        	r.printStackTrace();
//        }

        final Font font = new Font("啊啊啊", Font.PLAIN, 12);
        UIManager.put("Frame.titleFont", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("TitledBorder.font", font);
        UIManager.put("InternalFrame.font", font);
        UIManager.put("InternalFrame.titleFont", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
		
		new TestMainView();
	}
	

	
}
