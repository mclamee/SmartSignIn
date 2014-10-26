package samples;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class ClockMainFrame extends JFrame implements Runnable {

	private int width = 500;
	private int height = 500;
	private int hour = 1;
	private int minute = 1;
	private int second = 1;
	// 半径
	private double R = (width - 100) / 2.0;
	private double P = Math.PI / 6;
	private boolean tag = true;
	private Map<Integer, Double> xMap = new HashMap<Integer, Double>();
	private Map<Integer, Double> yMap = new HashMap<Integer, Double>();

	public ClockMainFrame() {

		this.setTitle("我的小钟");
		this.setSize(new Dimension(width, height));
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		for (int i = 1; i <= 12; i++) {

			double e = P * i;
			double x = Math.sin(e) * R + R + 50;
			double y = R + 50 - Math.cos(e) * R;

			xMap.put(i, x);
			yMap.put(i, y);
		}

		new Thread(this).start();
	}

	public int calX(double P, int i, int R1) {

		double e = P * i;
		double x = Math.sin(e) * R1 + R + 50;
		return (int) x;
	}

	public int calY(double P, int i, int R1) {

		double e = P * i;
		double y = R + 50 - Math.cos(e) * R1;
		return (int) y;
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// 钟的背景和钟盘只绘制一次
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, width, height);

		g2.setColor(Color.BLUE);
		g2.drawOval(35, 35, width - 70, height - 70);

		double x = 0, y = 0;
		for (int i = 1; i <= 12; i++) {

			double e = P * i;
			x = xMap.get(i);
			y = yMap.get(i);

			g2.drawString(String.valueOf(i), (int) x, (int) y);
		}
		tag = false;

		// 绘制钟心
		g2.fillRect(30 + (width - 70) / 2, 30 + (height - 70) / 2, 10, 10);

		// 绘制数字时钟
		g2.drawString(hour + ":" + minute + ":" + second,
				20 + (width - 75) / 2, 100);
		// 绘制时针
		g2.setColor(Color.RED);
		if (hour > 12) {

			hour -= 12;
		}
		g2.drawLine(35 + (width - 70) / 2, 35 + (width - 70) / 2,
				calX(Math.PI / 6 + Math.PI / 120, hour, 60),
				calY(Math.PI / 6 + Math.PI / 120, hour, 60));
		// 绘制分针
		g2.setColor(Color.YELLOW);
		g2.drawLine(35 + (width - 70) / 2, 35 + (width - 70) / 2,
				calX(Math.PI / 30, minute, 100),
				calY(Math.PI / 30, minute, 100));
		// 绘制秒针
		g2.setColor(Color.GREEN);
		g2.drawLine(35 + (width - 70) / 2, 35 + (width - 70) / 2,
				calX(Math.PI / 30, second, 150),
				calY(Math.PI / 30, second, 150));

		g2.dispose();
	}

	@Override
	public void run() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				hour = new java.util.Date().getHours();
				minute = new java.util.Date().getMinutes();
				second = new java.util.Date().getSeconds();

				repaint();
			}
		}, 0, 1000);
	}

	public static void main(String[] args) {

		ClockMainFrame mainf = new ClockMainFrame();
	}
}