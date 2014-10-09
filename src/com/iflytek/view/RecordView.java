package com.iflytek.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.iflytek.util.DrawableUtils;
import com.iflytek.util.JTableHelper;
import com.wicky.tdl.SimpleTodoTable;

public class RecordView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 4479482587513212049L;
    
    private JButton btnHome;
    private JButton btnAdd;
    private JButton btnClear;
	
    private SimpleTodoTable todoTable;
    
    private JTextField jtfFilter;
    
    public RecordView() {
        todoTable = new SimpleTodoTable();
        todoTable.setOpaque(false);
        todoTable.setBackground(null);
        
        ImageIcon imgHome = new ImageIcon("res/home.png");
        btnHome = DrawableUtils.createImageButton("", imgHome, null);
        btnHome.setBounds(20, 20, imgHome.getIconWidth(),
                imgHome.getIconHeight());
        DrawableUtils.setMouseListener(btnHome, "res/home");
        btnHome.addActionListener(this);
        
        this.setOpaque(false);
        this.setLayout(null);
        
        this.add(btnHome);
        
        this.add(getNorthPanel());
        this.add(getCenterPanel());
        this.add(getSouthPanel());
        
    }

    private JPanel getNorthPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setBounds(0, 0, 100, 100);
        northPanel.add(getBtnAdd());
        northPanel.add(getBtnClear());
        return northPanel;
    }
    
    private JButton getBtnAdd() {
        Dimension btnSize = new Dimension(150, 25);
        
        btnAdd = new JButton("Add New");
        btnAdd.setPreferredSize(btnSize);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = todoTable.dataModel.addRow();
                todoTable.stopCellEditing();
                jtfFilter.setText(null);
                todoTable.refreshTable();
                ListSelectionModel model = todoTable.getSelectionModel();
                model.clearSelection();
                model.setSelectionInterval(--row, row);
                JTableHelper.scrollToCenter(todoTable, row, 1);
            }
        });
        btnAdd.setBounds(100, 20, 150, 25);
        return btnAdd;
    }
    
    private JButton getBtnClear() {
        Dimension btnSize = new Dimension(150, 25);
        
        btnClear = new JButton("Delete Finishied");
        btnClear.setPreferredSize(btnSize);
        btnClear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                todoTable.stopCellEditing();
                jtfFilter.setText(null);
                int result = JOptionPane.showConfirmDialog(todoTable, "Are you sure to delete all the finishied entries?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    int rowCount = todoTable.dataModel.getRowCount();
                    for (int rowId = 0;rowId < rowCount;rowId++) {
                        Boolean value = (Boolean) todoTable.dataModel.getValueAt(rowId, 3);
                        if(value){
                            todoTable.dataModel.removeRow(rowId);
                            rowId--;rowCount--;
                        }
                    }
                    todoTable.refreshTable();
                }
            }
        });
        return btnClear;
    }
    
    private JScrollPane getCenterPanel() {
        JFrame frame = MainView.getFrame();
        Dimension size = frame.getSize();
        
        JScrollPane scrollpane = new JScrollPane(todoTable);
        scrollpane.setBounds(0, 100, (int)size.getWidth(), (int)(size.getHeight() - 200));
        scrollpane.setOpaque(false);
        
        scrollpane.setBackground(null);
        scrollpane.setBorder(null);
        
        return scrollpane;
    }
    
    private JPanel getSouthPanel() {
        JFrame frame = MainView.getFrame();
        Dimension size = frame.getSize();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(100, 100 , 500, 500);
        
        JLabel label = new JLabel("  Specify a word to search:   ");
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jtfFilter.requestFocus();
                jtfFilter.selectAll();
            }
        });
        label.setToolTipText("Type anything here. Tip: Use \"true\" or \"false\" to filter entry status. ");
        
        jtfFilter = new JTextField();
        jtfFilter.setForeground(Color.RED);
        jtfFilter.setFont(jtfFilter.getFont().deriveFont(Font.BOLD));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(jtfFilter, BorderLayout.CENTER);
        panel.add(new JLabel(" "), BorderLayout.EAST);

        jtfFilter.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                todoTable.stopCellEditing();
            }
        });
        jtfFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    jtfFilter.setText(null);
                }
            }
        });
        jtfFilter.getDocument().addDocumentListener(todoTable);
        return panel;
    }

	
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHome) {
            JFrame frame = MainView.getFrame();
            frame.getContentPane().remove(this);
            JPanel panel = ((MainView) frame).getMainJpanel();
            frame.getContentPane().add(panel);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        }
    }

}
