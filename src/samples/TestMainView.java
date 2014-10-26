package samples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ssi.util.ResizeUtil;

public class TestMainView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4333872831401104854L;

	private JPanel mMainJpanel;
	private JPanel mContentPanel;

	private JLabel label;

	/**
	 * 界面初始化.
	 * 
	 */
	public TestMainView() {
		// 设置界面大小，背景图片
		String bgImage = "res/img/index_bg.png";
		ImageIcon background = new ImageIcon(bgImage);

//		background = ResizeUtil.resizeImageToScreenSize(background);

		label = new JLabel(background);
		label.setBounds(0, 0, background.getIconWidth(),
				background.getIconHeight());
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

		int frameWidth = background.getIconWidth();
		int frameHeight = background.getIconHeight();

		setSize(frameWidth, frameHeight);
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mMainJpanel = new TestBeautyEyeLnf();

		mContentPanel = new JPanel(new BorderLayout());
		mContentPanel.setOpaque(false);
		mContentPanel.add(mMainJpanel, BorderLayout.CENTER);

		setLocationRelativeTo(null);
		setContentPane(mContentPanel);
		mContentPanel.setLayout(null);
//		this.setUndecorated(true);
		setVisible(true);
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Rectangle bounds = new Rectangle( screenSize );
//		this.setBounds(bounds);
	}

	public JPanel getMainJpanel() {
		return mMainJpanel;
	}

	public JPanel getContePanel() {
		return mContentPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}