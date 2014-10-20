package com.ssi.main.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ssi.main.data.RecordDataVector;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;

public class MoreInfoDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = -4062829546335381176L;

    private SimpleTodoTable todoTable = new SimpleTodoTable(this.getClass().getSimpleName());

    public MoreInfoDialog(JFrame parent, String title, String message, IDataVector<ISubDataVector> data) {
        super(parent, title, true);
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
        getContentPane().add(centerPanel);

        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("OK");
        buttonPanel.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        RecordDataVector data = new RecordDataVector();
        data.add("NO1026", "张曜嵩", "先生", false);
        data.add("NO1027", "王宁", "先生", false);
        data.add("NO1028", "杨洁", "女士", false);
        MoreInfoDialog ad = new MoreInfoDialog(new JFrame(), "title", "message", data);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();// 隐藏
    }

}
