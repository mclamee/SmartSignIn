import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
 
public class FullScreenDemo extends JWindow {
 
    public FullScreenDemo() {
        this.getContentPane().setLayout(null);
 
        JButton showButton = new JButton("show dialog");
        this.add(showButton);
        showButton.setBounds(100, 0, 100, 50);
        showButton.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e) {
                final JOptionPane optionPane = new JOptionPane("Message");
                showDialog(optionPane);
            }
        });
 
        JButton closeButton = new JButton("close");
        this.add(closeButton);
        closeButton.setBounds(100, 60, 100, 50);
        closeButton.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
 
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            throw new RuntimeException("系统不支持全屏");
        }
    }
 
    public void showDialog(final JOptionPane dialogPane) {
        if (this.getContentPane().getLayout() != null) {
            System.out.println(this.getContentPane().getLayout());
            throw new RuntimeException("全屏窗口的布局管理器必须设置为null，以便于定位弹出窗体");
        }
        if (dialog != null) {
            hideDialog();
        }
        //在窗口中显示
        showDialogInWindow(dialogPane);
        //加入监听器
        for (Component c : dialogPane.getComponents()) {
            if ("OptionPane.buttonArea".equals(c.getName())) {
                Container buttonArea = (Container) c;
                JButton okButton = (JButton) buttonArea.getComponent(0);
                okButton.addActionListener(new ActionListener() {
 
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hideDialog();
                    }
                });
            }
        }
    }
 
 
    private void showDialogInWindow(JComponent dialogPane) {
        Dimension paneSize = dialogPane.getPreferredSize();
        int paneWidth = paneSize.width;
        int paneHeight = paneSize.height;
         
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
         
        int x = (screenWidth - paneWidth) / 2;
        int y = (screenHeight - paneHeight) / 2;
         
        this.getContentPane().add(dialogPane);
        dialogPane.setBounds(x, y, paneWidth, paneHeight);
        dialogPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dialog = dialogPane;
        this.validateTree();
        this.repaint();
    }
 
    private void hideDialog() {
        this.remove(dialog);
        this.dialog = null;
        this.validateTree();
        this.repaint();
    }
 
    public static void main(String[] args) throws Exception {
        final FullScreenDemo fullScreenDemo = new FullScreenDemo();
        fullScreenDemo.setVisible(true);
    }
    private JComponent dialog;
}