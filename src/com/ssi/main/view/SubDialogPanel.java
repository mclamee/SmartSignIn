package com.ssi.main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ssi.main.Application;
import com.ssi.util.StringUtil;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;

public class SubDialogPanel extends JPanel {
	private static final long serialVersionUID = 4461652465461547905L;
	private SimpleTodoTable subDialogTable;
	private JLabel subDialogTile;
	private JScrollPane mainTablePane;

	public SubDialogPanel(JScrollPane panelTable) {
		this.mainTablePane = panelTable;
		subDialogTable = new SimpleTodoTable();
		
		JScrollPane scrollpane = new JScrollPane(subDialogTable);
		scrollpane.setOpaque(false);
		scrollpane.setBackground(Color.BLACK);
		this.setLayout(new BorderLayout());
		this.add(scrollpane, BorderLayout.CENTER);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		
		subDialogTile = new JLabel("");
		subDialogTile.setAlignmentX(Component.CENTER_ALIGNMENT);
		subDialogTile.setAlignmentY(Component.CENTER_ALIGNMENT);
		subDialogTile.setFont(new Font("黑体", Font.PLAIN, 35));
		this.add(subDialogTile, BorderLayout.NORTH);
		JButton btnClose = new JButton("关闭");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		btnClose.setSize(500, 40);
		this.add(btnClose, BorderLayout.SOUTH);

		Dimension frameSize = Application.MAIN_FRAME.getSize();
		int frameWidth = (int) frameSize.getWidth();
		int frameHeight = (int) frameSize.getHeight();
		this.setBounds(1 * frameWidth / 12, 1 * frameWidth / 12,
				8 * frameWidth / 12, 5 * frameHeight / 12);
	}

	public void close() {
		this.setVisible(false);
		if(mainTablePane != null)mainTablePane.setVisible(true);
		subDialogTile.setText("");
	}

	public void open(String title, String message,
			IDataVector<ISubDataVector> data) {
		if(mainTablePane != null)mainTablePane.setVisible(false);
		if(!StringUtil.isEmpty(title)){
			subDialogTile.setText(title);
		}
		((SimpleTableModel) subDialogTable.getModel()).initData(data);
		this.setVisible(true);
	}
}
