package com.ssi.main.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ssi.main.data.RecordDataVector;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;

public class MoreInfoDialog extends JInternalFrame implements ActionListener {
    private static final long serialVersionUID = -4062829546335381176L;

    private SimpleTodoTable todoTable = new SimpleTodoTable(this.getClass().getSimpleName());

    public MoreInfoDialog(Container parent, String title, String message, IDataVector<ISubDataVector> data) {
        super(title, false, true, false, false);
        if (parent != null) {// 只要窗口不为空
            Dimension parentSize = parent.getSize();// 得到一个尺寸 窗口获得的尺寸
            Point p = parent.getLocation();// 窗口的位置这个点给p
            // 设置位置为
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        getContentPane().add(new JLabel(message), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        ((SimpleTableModel) todoTable.getModel()).initData(data);
        centerPanel.add(todoTable);
        JScrollPane scPane = new JScrollPane(todoTable);
        getContentPane().add(scPane);

        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("Close");
        buttonPanel.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        
        pack();
        Dimension size = todoTable.getSize();
        setPreferredSize(new Dimension((int)(size.getWidth() * 0.8), (int)(size.getHeight() * 0.8)));
        setVisible(true);
    }

    public static void main(String[] args) {
        
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setLayout(null);
        RecordDataVector data = new RecordDataVector();
        data.add("NO1026", "张曜嵩", "先生", false);
        data.add("NO1027", "王宁", "先生", false);
        data.add("NO1028", "杨洁", "女士", false);
        MoreInfoDialog ad = new MoreInfoDialog(frame, "title", "message", data);
        frame.add(ad);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();// 隐藏
    }

}
