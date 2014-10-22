package com.ssi.main.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.AuthorizationException;

public class AuthView extends JFrame{
    private static final long serialVersionUID = -4907857102233644371L;
    
    public AuthView() {
        setTitle("Authrization");
        
        ImageIcon background = new ImageIcon("res/img/auth_bg.png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background
                    .getIconHeight());
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        
        int frameWidth = background.getIconWidth();
        int frameHeight = background.getIconHeight();
        
        this.setSize(frameWidth, frameHeight);
        this.setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(panel, BorderLayout.CENTER);
        this.setContentPane(contentPanel);

        int eleHeight = 30;
        int rowSpace = 15;
        int colSpace = 10;
        
        int topSpace = 2 * frameHeight / 12 - rowSpace;
        int leftSpace = 1 * frameWidth / 12 - colSpace;
        
        int titleWidth = 3 * frameWidth / 12 - colSpace;
        int txfWidth = 8 * frameWidth / 12 - colSpace;
        
        int row1 = topSpace + 0;
        int row2 = row1 + rowSpace + eleHeight;
        int row3 = row2 + rowSpace + eleHeight;
        
        int column1 = leftSpace + 0;
        int column2 = column1 + titleWidth + colSpace;
        
        JLabel lbUser = new JLabel("User: ");
        lbUser.setBounds(column1, row1, titleWidth, eleHeight);
        panel.add(lbUser);
        final JTextField txUser = new JTextField();
        txUser.setBounds(column2, row1, txfWidth, eleHeight);
        txUser.setText(SSIConfig.get("auth.user"));
        panel.add(txUser);
        
        JLabel lbAuth = new JLabel("Authorization Key: ");
        lbAuth.setBounds(column1, row2, titleWidth, eleHeight);
        panel.add(lbAuth);
        final JTextField txAuth = new JTextField();
        txAuth.setBounds(column2, row2, txfWidth, eleHeight);
        txAuth.setText(SSIConfig.get("auth.key"));
        panel.add(txAuth);
        
        JButton btnAuth = new JButton("Authorize");
        btnAuth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SSIConfig.put("auth.user", txUser.getText());
                SSIConfig.put("auth.key", txAuth.getText());
                SSIConfig.save();
                try {
					Application.authorization();
				    Application.initMainFrame();
				    AuthView.this.dispose();
				} catch (AuthorizationException e1) {
                    JOptionPane.showMessageDialog(AuthView.this, e1.getMessage());
				}
            }
        });
        btnAuth.setBounds(column2, row3, titleWidth, eleHeight);
        panel.add(btnAuth);
        JButton btnCancel = new JButton("Exit");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnCancel.setBounds(column2 + titleWidth + colSpace, row3, titleWidth, eleHeight);
        panel.add(btnCancel);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); 
        this.setVisible(true);
    }
}