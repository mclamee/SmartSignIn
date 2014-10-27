package com.ssi.main.view;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import com.ssi.util.DrawableUtils;
import com.ssi.util.StringUtil;

public class VirtualKeyboardView extends JPanel {
	private static final int VKB_WIDTH = 700;
	private static final int VKB_HEIGHT = 270;

	private static final long serialVersionUID = -8023081831752715549L;
	
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
    
    private static void pressKeyWithShift(int keyvalue) {
    	robot.keyPress(KeyEvent.VK_SHIFT);
    	robot.keyPress(keyvalue); // 按下按键
    	robot.keyRelease(keyvalue); // 释放按键
    	robot.keyRelease(KeyEvent.VK_SHIFT);
    }
    
    private Map<String, Component> respCompMap = new HashMap<>();
    
	class MouseAdp extends MouseAdapter {
		
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("DRAGED!");
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		private void responseAction(Object source) {
			if (source instanceof JLabel) {
				
				String key = ((JLabel) source).getName();
				if(key == null){
					key = ((JLabel) source).getText();
				}
				VirtualKeyboardView.input(key);
			}
		}
	    
		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JLabel) {
				((JLabel) source).setBackground(Color.YELLOW);
			}
			responseAction(source);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JLabel) {
				((JLabel) source).setBackground(Color.LIGHT_GRAY);
			}
		}
	}

    public static void input(String str){
        if(str!=null){
        	boolean withShift = false;
        	String vkName = StringUtil.trimAndUpper(str);
        	if(vkName.startsWith("SHIFT_")){
        		vkName = vkName.replace("SHIFT_", "");
        		withShift = true;
        	}
        	Field[] fields = KeyEvent.class.getFields();
        	for(Field f: fields){
				if(f.getName().equals("VK_"+vkName)){
        			int key;
					try {
						key = f.getInt(KeyEvent.class);
						if(withShift){
							pressKeyWithShift(key);
						}else{
							pressKey(key);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
        		}
        	}
        }
    }
    
	public void createVirtualKeyboard(Container container, String namespace) {
		MouseAdapter mouseAdapter = new MouseAdp();
		JPanel contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.addMouseListener(mouseAdapter);
        GridLayout gridlayout = new GridLayout(5, 1); 
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
        JPanel panel5 = new JPanel(flowLayout);
        panel5.setFocusable(false);
        contentPane.add(panel5);
        
        char[] row1 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}; 
        char[] row2 = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'}; 
        char[] row3 = new char[]{'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'}; 
        char[] row4 = new char[]{'z', 'x', 'c', 'v', 'b', 'n', 'm'}; 
        char[] row5 = new char[]{',', '.', '!', '?', ':', '(', ')', '$', '{', '}'}; 
        String[] row5Ids = new String[]{"COMMA", "PERIOD", "SHIFT_1", "SHIFT_SLASH", "SHIFT_SEMICOLON", "SHIFT_9", "SHIFT_0", "SHIFT_4", "SHIFT_OPEN_BRACKET", "SHIFT_CLOSE_BRACKET"}; 
        
        // 1 
        JLabel escKey = createVirtualKeybordLabel("ESCAPE", "Esc", mouseAdapter);
        panel1.add(escKey);
        for(char i : row1){
        	JLabel comp = createVirtualKeybordLabel(null, i, mouseAdapter);
        	panel1.add(comp);
        }
        
        // 2
        JLabel capLckKey = createVirtualKeybordLabel("CAPS_LOCK", "CapLck", mouseAdapter);
        panel2.add(capLckKey);
        for(char i : row2){
        	JLabel comp = createVirtualKeybordLabel(null, i, mouseAdapter);
        	panel2.add(comp);
        }
        
        // 3
        for(char i : row3){
        	JLabel comp = createVirtualKeybordLabel(null, i, mouseAdapter);
        	panel3.add(comp);
        }
        JLabel backKey = createVirtualKeybordLabel("BACK_SPACE", "←", mouseAdapter);
        panel3.add(backKey);
        
        // 4
        for(char i : row4){
        	JLabel comp = createVirtualKeybordLabel(null, i, mouseAdapter);
        	panel4.add(comp);
        }
        JLabel spaceKey = createVirtualKeybordLabel("space", "空格", mouseAdapter);
        panel4.add(spaceKey);
        JLabel enterKey = createVirtualKeybordLabel("enter", "回车", mouseAdapter);
        panel4.add(enterKey);
        
        // 5
        JLabel inputMethod = createVirtualKeybordLabel("SHIFT_CONTROL", "输入法", mouseAdapter);
        panel5.add(inputMethod);
        for(int i=0;i<row5.length; i++){
        	JLabel comp = createVirtualKeybordLabel(row5Ids[i], row5[i], mouseAdapter);
        	panel5.add(comp);
        }
        
        contentPane.setBorder(BorderFactory.createEtchedBorder());
        contentPane.setBounds(0, 0, VKB_WIDTH, VKB_HEIGHT);
        container.add(contentPane);
        respCompMap.put(namespace, contentPane);
        contentPane.setVisible(false);
	}


	private JLabel createVirtualKeybordLabel(String rowId, char i, MouseAdapter mouseAdapter) {
		JLabel comp = new JLabel((char)i+"", SwingConstants.CENTER);
		comp.setName(rowId);
		comp.setPreferredSize(new Dimension(45,45));
		comp.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.DARK_GRAY));
		comp.setOpaque(true);
		comp.setBackground(Color.LIGHT_GRAY);
		comp.setForeground(Color.DARK_GRAY);
		comp.setFont(new Font("Tahoma",Font.PLAIN,35));
		comp.addMouseListener(mouseAdapter);
		comp.setFocusable(false);
		return comp;
	}
	
	private JLabel createVirtualKeybordLabel(String id, String strName, MouseAdapter mouseAdapter) {
		JLabel comp = new JLabel(strName, SwingConstants.CENTER);
		comp.setName(id);
		comp.setPreferredSize(new Dimension(70,45));
		comp.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.DARK_GRAY));
		comp.setOpaque(true);
		comp.setBackground(Color.LIGHT_GRAY);
		comp.setForeground(Color.DARK_GRAY);
		comp.setFont(new Font("宋体",Font.PLAIN,20));
		comp.addMouseListener(mouseAdapter);
		comp.setFocusable(false);
		return comp;
	}
	
	class FocusAdap extends FocusAdapter{
		private Component virtualKeyBoard;

		public FocusAdap(Component virtualKeyBoard) {
			this.virtualKeyBoard = virtualKeyBoard;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if(e.getComponent() instanceof JTextComponent && !((JTextComponent)e.getComponent()).isEditable()){
				return;
			}
			
			virtualKeyBoard.revalidate();
			
			System.out.println(virtualKeyBoard.getSize());
			
			int minY = (int) (virtualKeyBoard.getParent().getSize().getHeight() - virtualKeyBoard.getSize().getHeight());
			int minX = (int) (virtualKeyBoard.getParent().getSize().getWidth() - virtualKeyBoard.getSize().getWidth());
			
			int x = (int)e.getComponent().getLocationOnScreen().getX() + (int)e.getComponent().getWidth();
			int y = (int)e.getComponent().getLocationOnScreen().getY() + (int)e.getComponent().getHeight();
			virtualKeyBoard.setBounds(x, y, VKB_WIDTH, VKB_HEIGHT);
			
			virtualKeyBoard.revalidate();
			
			if(x >= minX){
				x = (int)e.getComponent().getLocationOnScreen().getX() - (int)virtualKeyBoard.getSize().getWidth();
				if(x < 0){
					x = 0;
				}
			}
			if(y >= minY){
				y = (int)e.getComponent().getLocationOnScreen().getY() - (int)virtualKeyBoard.getSize().getHeight();
				if(y < 0){
					y = 0;
				}
			}
			virtualKeyBoard.setBounds(x, y, VKB_WIDTH, VKB_HEIGHT);
			
			virtualKeyBoard.revalidate();
			System.out.println(virtualKeyBoard.getSize());
			virtualKeyBoard.setVisible(true);
		}
		
		@Override
		public void focusLost(FocusEvent e) {
			virtualKeyBoard.setVisible(false);
		}
	}
	
	public void applyVirtualKeyboard(Container source, String namespace){
		for(Component c: source.getComponents()){
			if(c instanceof JTextField){
				final Component virtualKeyBoard = respCompMap.get(namespace);
				c.addFocusListener(new FocusAdap(virtualKeyBoard));
				c.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
							virtualKeyBoard.setVisible(false);
						}
					}
				});
			}else if(c instanceof JPasswordField){
				final Component virtualKeyBoard = respCompMap.get(namespace);
				c.addFocusListener(new FocusAdap(virtualKeyBoard));
				c.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
							virtualKeyBoard.setVisible(false);
						}
					}
				});
			}else if(c instanceof JTable){
//				final Component virtualKeyBoard = respCompMap.get(namespace);
//				Component editorComponent = ((JTable) c).getEditorComponent();
//				if(editorComponent != null){
//					editorComponent.addFocusListener(new FocusAdap(virtualKeyBoard));
//					editorComponent.addKeyListener(new KeyAdapter() {
//						@Override
//						public void keyPressed(KeyEvent e) {
//							if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//								virtualKeyBoard.setVisible(false);
//							}
//						}
//					});
//				}
//				c.addFocusListener(new FocusAdap(virtualKeyBoard));
//				c.addKeyListener(new KeyAdapter() {
//					@Override
//					public void keyPressed(KeyEvent e) {
//						if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//							virtualKeyBoard.setVisible(false);
//						}
//					}
//				});
			}else if(c instanceof Container){
				applyVirtualKeyboard((Container) c, namespace);
			}
		}
	}
	
}
