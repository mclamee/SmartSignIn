package samples;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class TestCardLayout implements ActionListener{
	
		JPanel jp1=new JPanel();
        JButton jButton=new JButton("上一句");
        JButton jButton2=new JButton("下一句");
        JButton jButton3=new JButton("第五句");
		CardLayout cl=new CardLayout();
		public TestCardLayout() {
			JFrame jf = new JFrame("测试");
			jp1.setLayout(cl);
			jp1.add(new JLabel("很简单"),"1");
			jp1.add(new JLabel("有界面，代码肯定少不了"),"2");
			jp1.add(new JLabel("不要怕，先看！"),"3");
			jp1.add(new JLabel("好啊！"),"4");
			jp1.add(new JLabel("呼呼！"),"5");
			jp1.add(new JLabel("真好玩！"),"6");
			jp1.add(new JLabel("明白没"),"7");
			jp1.add(new JLabel("要自己学会看API"),"8");
			jp1.add(new JLabel("就这样！"),"9");
			JPanel jPanel=new JPanel();
			jPanel.add(jButton);
			jPanel.add(jButton2);
			jPanel.add(jButton3);
			jButton.addActionListener(this);
			jButton2.addActionListener(this);
			jButton3.addActionListener(this);
			jf.add(jp1,BorderLayout.CENTER);
			jf.add(jPanel,BorderLayout.SOUTH);
			jf.pack();
			jf.setLocation(500, 100);
			jf.setVisible(true);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			}
		
		@Override
		public void actionPerformed(ActionEvent e) {
				String string=e.getActionCommand();
				if("上一句".equals(string))
					cl.next(jp1);
				if("下一句".equals(string))
					cl.next(jp1);
				if("第五句".equals(string))
					cl.show(jp1,"5");
				
	}
			
		public static void main(String[] args) {
			new TestCardLayout();

		}
	}