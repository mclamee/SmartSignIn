package com.ssi.main.view;

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
import java.awt.event.WindowAdapter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import com.ssi.i18n.Messages;
import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.util.DrawableUtils;
import com.ssi.util.JTableHelper;
import com.ssi.util.StringUtil;
import com.ssi.util.excel.ExcelHandle;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;

public class RecordView extends JPanel implements IView, ActionListener {
	
    private static final long serialVersionUID = 4479482587513212049L;
    
    private static Logger LOG = Logger.getLogger(RecordView.class);
    
    private JButton btnHome;
    private JButton btnAdd;
    private JButton btnClear;
    
    private JTextField template;
    
    // init simple to-do table at first
    private SimpleTodoTable todoTable = new SimpleTodoTable(this);
    
    private JTextField tfSearch;

	private SubDialogPanel subDialogPanel;

	private JScrollPane panelTable;

    public RecordView() {
        Dimension frameSize = Application.MAIN_FRAME.getSize();
        int frameWidth = (int)frameSize.getWidth();
        int frameHeight = (int)frameSize.getHeight();
        
        this.setOpaque(false);
        this.setLayout(null);
        
        ImageIcon imgHome = new ImageIcon("res/img/home.png"); //$NON-NLS-1$
        btnHome = DrawableUtils.createImageButton("", imgHome, null); //$NON-NLS-1$
        btnHome.setBounds(20, 20, imgHome.getIconWidth(),
                imgHome.getIconHeight());
        DrawableUtils.setMouseListener(btnHome, "res/img/home"); //$NON-NLS-1$
        btnHome.addActionListener(this);
        this.add(btnHome);
        
        JButton btnAdd2 = getBtnAdd();
        btnAdd2.setBounds(100, 15, 150, 25);
        this.add(btnAdd2);
        
        JButton btnClear2 = getBtnClear();
        btnClear2.setBounds(100, 40, 150, 25);
        this.add(btnClear2);
        
        JButton btnExport = getBtnExport();
        btnExport.setBounds(260, 40, 150, 25);
        this.add(btnExport);
        
        JLabel templateLable = new JLabel();
        templateLable.setText("设置播放模板：");
        templateLable.setBounds(430, 40, 100, 25);
        this.add(templateLable);
        
        template = new JTextField();
        template.setText(SSIConfig.get("RecordView.template"));
        template.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				SSIConfig.put("RecordView.template", template.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				SSIConfig.put("RecordView.template", template.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				SSIConfig.put("RecordView.template", template.getText());
			}
		});
        template.setBounds(520, 40, 500, 25);
        this.add(template);
        
        panelTable = getPanelTable();
        panelTable.setBounds(0, 70, frameWidth, frameHeight - 200);
        this.add(panelTable);
        
        SubDialogPanel subDialogPanel2 = getSubDialogPanel();
		this.add(subDialogPanel2);
		subDialogPanel2.close();
        
        JLabel labelSearch = getLabelSearch();
        labelSearch.setBounds(0, frameHeight - 120, 170, 30);
        this.add(labelSearch);
        
        JTextField tfSearch2 = getTfSearch();
        tfSearch2.setBounds(170, frameHeight - 120, frameWidth - 170 - 10, 30);
        this.add(tfSearch2);
    }

	private SubDialogPanel getSubDialogPanel() {
		subDialogPanel = new SubDialogPanel(panelTable);
        return subDialogPanel;
	}
    
	public void closeSubDialog(){
		subDialogPanel.close();
	}
	
	public void openSubDialog(String title, String message, IDataVector<ISubDataVector> data) {
		subDialogPanel.open(title, message, data);
	}
	
    private JButton getBtnExport() {
    	Dimension btnSize = new Dimension(150, 25);
    	
    	JButton btnExport = new JButton("生成报表"); //$NON-NLS-1$
    	btnExport.setPreferredSize(btnSize);
    	btnExport.addActionListener(new ActionListener() {
    		
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			todoTable.stopCellEditing();
    			tfSearch.setText(null);
    			todoTable.refreshTable();
    			ExcelHandle.getInstance().exportCustReport();
    		}
    	});
    	return btnExport;
    }
    
    private JButton getBtnAdd() {
        Dimension btnSize = new Dimension(150, 25);
        
        btnAdd = new JButton(Messages.getString("RecordView.btn_add")); //$NON-NLS-1$
        btnAdd.setPreferredSize(btnSize);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = todoTable.dataModel.addRow();
                todoTable.stopCellEditing();
                tfSearch.setText(null);
                todoTable.refreshTable();
                ListSelectionModel model = todoTable.getSelectionModel();
                model.clearSelection();
                model.setSelectionInterval(--row, row);
                JTableHelper.scrollCellToCenter(todoTable, row, 1);
            }
        });
        return btnAdd;
    }
    
    private JButton getBtnClear() {
        Dimension btnSize = new Dimension(150, 25);
        
        btnClear = new JButton(Messages.getString("RecordView.btn_clear")); //$NON-NLS-1$
        btnClear.setPreferredSize(btnSize);
        btnClear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                todoTable.stopCellEditing();
                tfSearch.setText(null);
                int result = JOptionPane.showInternalConfirmDialog(Application.MAIN_FRAME.getContentPane(), Messages.getString("RecordView.btn_clear_confirm"), 
                		Messages.getString("RecordView.btn_clear_confirm_title"), 
                		JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
                if(result == JOptionPane.YES_OPTION){
                    int rowCount = todoTable.dataModel.getRowCount();
                    for (int rowId = 0;rowId < rowCount;rowId++) {
                        Boolean value = (Boolean) todoTable.dataModel.getFlag(rowId);
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
    
    private JScrollPane getPanelTable() {
        JScrollPane scrollpane = new JScrollPane(todoTable);
        scrollpane.setOpaque(false);
        scrollpane.setBackground(null);
        scrollpane.setBorder(null);
        
        return scrollpane;
    }

    private JTextField getTfSearch() {
        tfSearch = new JTextField();
        tfSearch.setForeground(Color.RED);
        tfSearch.setFont(tfSearch.getFont().deriveFont(Font.BOLD));
        tfSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                todoTable.stopCellEditing();
            }
        });
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    tfSearch.setText(null);
                }
            }
        });
        tfSearch.getDocument().addDocumentListener(todoTable);
        return tfSearch;
    }

    private JLabel getLabelSearch() {
        JLabel label = new JLabel(Messages.getString("RecordView.label_search")); //$NON-NLS-1$
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tfSearch.requestFocus();
                tfSearch.selectAll();
            }
        });
        label.setToolTipText(Messages.getString("RecordView.label_search_tip")); //$NON-NLS-1$
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHome) {
            todoTable.stopCellEditing();
            todoTable.refreshTable();
            
            Application.switchView(Application.MAIN_FRAME.getMainJpanel());
        }
    }

	public SimpleTableModel getTableModel() {
		return todoTable.dataModel;
	}
	
	public WindowAdapter getWindowListener(){
		return todoTable.getWindowListener();
	}

	@Override
	public String getTemplate() {
		return StringUtil.trim(template.getText());
	}
}
