package com.ssi.main.view;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.util.ResizeUtil;

public class PlayImageView extends JPanel implements ActionListener {
    private static final String Pause = "Ⅱ 暂停";
    private static final String Play = "▶ 播放";

    public static void main(String[] args) {

        JFrame jf = new JFrame("图片浏览器");
        JPanel mContentPanel = new JPanel(new BorderLayout());
        mContentPanel.setOpaque(false);
        mContentPanel.add(new PlayImageView(), BorderLayout.CENTER);
        jf.setContentPane(mContentPanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setUndecorated(true);
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(jf);

        jf.setVisible(true);
    }
    
    private CardLayout cl = new CardLayout();
    private JButton jbbf;
    private JPanel jp1 = new JPanel();
    private JPanel jp2;
    private JComboBox<Integer> seconds;
    private Timer timer;

    private Timer timerHide = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jp2.setVisible(false);
            Cursor cursor = null;
            try {
                cursor = createEmptyCursor();
            } catch (HeadlessException | IndexOutOfBoundsException | IOException e1) {
                e1.printStackTrace();
            }
            PlayImageView.this.setCursor(cursor);
        }
    });

    public PlayImageView() {
        setLayout(new BorderLayout());
        
        jp1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!jp2.isVisible()) {
                    stopHideTimer();
                    jp2.setVisible(true);
                    timerHide.start();
                }
            }
        });
        jp1.setLayout(cl);
        String folder = "d:\\1\\";
        final Pattern pat = Pattern.compile(".*\\.(?:png|jpg|bmp|gif|pcx|tiff)", Pattern.CASE_INSENSITIVE);
        File[] files = new File(folder).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                Matcher matcher = pat.matcher(name);
                if (matcher.matches()) {
                    return true;
                }
                return false;
            }
        });

        for (int i = 0;i < files.length;i++) {
            ImageIcon ic = new ImageIcon(files[i].getAbsolutePath());
            
            if(!Application.debugMode || SSIConfig.getBoolean("debug.fullscreen") == true){
                ic = ResizeUtil.resizeImageToScreenSize(ic);
            }
            JLabel label = new JLabel(ic);
            label.setBounds(0, 0, ic.getIconWidth(), ic.getIconHeight());
            
            jp1.add(label, i + "");
        }
        this.add(jp1, BorderLayout.CENTER);

        jp2 = new JPanel();
        String[] s = {"第一张","上一张","下一张","最后一张"};
        for (int i = 0;i < s.length;i++) {
            JButton jb = new JButton(s[i]);
            jb.addActionListener(this);
            jp2.add(jb);
        }

        JLabel lb = new JLabel("自动播放间隔:");
        jp2.add(lb);
        Integer str[] = {1,2,3,4,5,6,7,8,9,10};
        seconds = new JComboBox<Integer>(str);
        seconds.setSelectedIndex(4);
        seconds.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
                startTimer();
            }
        });
        jp2.add(seconds);
        JLabel lb2 = new JLabel("秒");
        jp2.add(lb2);
        jbbf = new JButton(Play);
        jbbf.addActionListener(this);
        jp2.add(jbbf, BorderLayout.SOUTH);

        this.add(jp2, BorderLayout.SOUTH);

        jp2.setVisible(false);
        jp2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                stopHideTimer();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                timerHide.start();
                System.out.println("Timer Start!");
            }
        });

        for (Component c:jp2.getComponents()) {
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    stopHideTimer();
                }

            });
        }
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if ("上一张".equals(s)) {
            stopTimer();
            cl.previous(jp1);
        }
        else if ("下一张".equals(s)){
            stopTimer();
            cl.next(jp1);
        }
        else if ("最后一张".equals(s)){
            stopTimer();
            cl.last(jp1);
        }
        else if ("第一张".equals(s)){
            stopTimer();
            cl.first(jp1);
        }else if (Play.equals(s)) {
            stopTimer();
            startTimer();
        } else if (Pause.equals(s)) {
            stopTimer();
        } else {
            cl.next(jp1);
        }
    }

    private void startTimer() {
        timer = new Timer((int) (seconds.getSelectedItem()) * 1000, this);
        timer.start();
        jbbf.setText(Pause);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            jbbf.setText(Play);
        }
    }

    private Cursor createEmptyCursor() throws HeadlessException, IndexOutOfBoundsException, IOException {
        BufferedImage cursor = ImageIO.read(new File("D:/cursor.png"));
        Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(cursor.getWidth(this), cursor.getHeight(this));

        BufferedImage bufferedImage = new BufferedImage(bestCursorSize.width, bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0;x < bestCursorSize.width;x++)
            for (int y = 0;y < bestCursorSize.height;y++)
                bufferedImage.setRGB(x, y, 0);
        bufferedImage.getGraphics().drawImage(cursor, 0, 0, this);

        return Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, new Point(0, 0), "cust_Cursor");
    }

    private void stopHideTimer() {
        this.timerHide.stop();
        System.out.println("Timer STOP!");
        this.setCursor(null);
    }
}
