import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.*;

class ClockDemo extends JFrame {
	public ClockDemo() {
		setSize(400, 400);
		this.getContentPane().setBackground(Color.blue);
		this.getContentPane().add(new Clock(), "Center");

		setTitle("时钟演示");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String args[]) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		ClockDemo mainFrame = new ClockDemo();
	}
}

class Clock extends JPanel implements Runnable {
	// Thread timer;
	int oldh = -1, oldm = -1, olds = -1, hq = -1, mq = -1, sq = -1;
	final double RAD = Math.PI / 180.0;

	Thread th;

	public Clock() {
		super();
		this.setOpaque(false); // 设置透明
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// repaint();

			}
		};
		// new Timer(1000, taskPerformer).start();
		th = new Thread(this);
		th.start();

	}

	public void run() {
		while (true) {
			// System.out.println("HELLO");
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}

	public void paintComponent(Graphics g) {
		int h, m, s, q, x1, y1, x2, y2;
		int x0 = this.getWidth() / 2, y0 = this.getHeight() / 2;
		int R = (x0 > y0 ? y0 : x0) - 30;
		Date d = new Date();
		h = d.getHours();
		m = d.getMinutes();
		s = d.getSeconds();
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x0 - R, y0 - R, 2 * R, 2 * R); // 画表盘
		g.setColor(Color.white);
		g.drawOval(x0 - R + 3, y0 - R + 3, 2 * R - 7, 2 * R - 7); // 画刻度
		for (int i = 0; i < 12; i++) {
			y1 = (int) (y0 - Math.cos(i * 30 * RAD) * R * (float) 4 / 5);
			x1 = (int) (x0 + Math.sin(i * 30 * RAD) * R * (float) 4 / 5);
			y2 = (int) (y0 - Math.cos(i * 30 * RAD) * (R - 3));
			x2 = (int) (x0 + Math.sin(i * 30 * RAD) * (R - 3));
			g.drawLine(x1, y1, x2, y2);
		}
		q = h * 30 + m / 2; // 计算时针角度
		drawHand(g, x0, y0, 10, R * 4 / 7, q, Color.blue); // 画时针
		q = m * 6 + s / 10; // 计算分针角度
		drawHand(g, x0, y0, 5, R * 2 / 3, q, Color.yellow); // 画分针
		q = s * 6; // 计算秒针角度
		drawHand(g, x0, y0, 2, R * 4 / 5, q, Color.red); // 画秒针
		g.setColor(Color.black);
		g.drawOval(x0 - 2, y0 - 2, 4, 4); // 画轴心
	}

	void drawHand(Graphics g, int x0, int y0, int w, int L, int q, Color c) {
		int x[] = new int[4];
		int y[] = new int[4];
		x[0] = x0 - (int) (Math.sin(RAD * q) * L / 6);
		y[0] = y0 + (int) (Math.cos(RAD * q) * L / 6);
		x[1] = x0 + (int) (Math.sin(RAD * (90 - q)) * w);
		y[1] = y0 + (int) (Math.cos(RAD * (90 - q)) * w);
		x[2] = x0 + (int) (Math.sin(RAD * q) * L);
		y[2] = y0 - (int) (Math.cos(RAD * q) * L);
		x[3] = x0 - (int) (Math.sin(RAD * (90 - q)) * w);
		y[3] = y0 - (int) (Math.cos(RAD * (90 - q)) * w);
		g.setColor(c);
		g.fillPolygon(x, y, 4);
	}
}