package com.wicky.tdl;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.ssi.i18n.Messages;
import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.TableDataChangeListener;
import com.ssi.main.view.IView;
import com.ssi.util.StringUtil;


/**
 * Main class for the simple todo table project
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTodoTable extends JTable implements ListSelectionListener, DocumentListener{
    private static final long serialVersionUID = -3747203421484558542L;
    
    private static Logger LOG = Logger.getLogger(SimpleTodoTable.class);
    
    public SimpleTableModel dataModel;
    public TableRowSorter<TableModel> rowSorter;
    
    public SimpleTodoTable() {
    	this(null);
    }
    
    public SimpleTodoTable(IView view){
		String viewName = view == null?"null":view.getClass().getSimpleName();
        LOG.info("> initializing table for " + viewName + " ...");
        // 1. create data model
		dataModel = new SimpleTableModel(view);
        
        // 4. setup data model
        this.setModel(dataModel);
        
        // 5. Setup Editor and Renderer
        dataModel.setupEditorAndRenderer(this, view);
        
        // 6. adjust column width
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataModel.adjustColumnWidth(this);
        
        // 7. setup UI to support drag and drop rows
        this.setUI(new DragDropRowTableUI());
        
        // 8. add other components and bind events
        this.setSelectionBackground(Color.CYAN);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                
                int r = SimpleTodoTable.this.rowAtPoint(e.getPoint());
                if (r >= 0 && r < SimpleTodoTable.this.getRowCount()) {
                    SimpleTodoTable.this.setRowSelectionInterval(r, r);
                } else {
                    SimpleTodoTable.this.clearSelection();
                }

                int rowindex = SimpleTodoTable.this.getSelectedRow();
                if (rowindex < 0)
                    return;
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                    stopCellEditing();
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem delItm = new JMenuItem(Messages.getString("RecordView.table.menu_delete")); //$NON-NLS-1$
                    delItm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
		                            dataModel.removeRow(SimpleTodoTable.this.getSelectedRow());
		                            refreshTable();
								}
							});
                        }
                    });
                    popup.add(delItm);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        ListSelectionModel listMod = this.getSelectionModel();
        listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMod.addListSelectionListener(this);

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE){
                    final int selectedRow = SimpleTodoTable.this.getSelectedRow();
                    if(selectedRow != -1){
                        stopCellEditing();
                        int rowCount = dataModel.getRowCount();
                        if(rowCount != 0){
                            int result = JOptionPane.showInternalConfirmDialog(Application.MAIN_FRAME.getContentPane(), Messages.getString("RecordView.table.menu_delete_confirm"), Messages.getString("RecordView.table.menu_delete_confirm_title"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
                            if(result == JOptionPane.YES_OPTION){
                            	SwingUtilities.invokeLater(new Runnable() {
									public void run() {
		                                dataModel.removeRow(selectedRow);
		                                if(selectedRow < dataModel.getRowCount()){
		                                    SimpleTodoTable.this.setRowSelectionInterval(selectedRow, selectedRow);
		                                }
		                                stopCellEditing();
		                                refreshTable();
									}
								});
                            }
                        }
                    }
                }
            }
        });
        
        ////////////////
        // setup row sorter
        createRowSorter();
        this.setRowSorter(rowSorter);
        
        this.setRowHeight(40);
        this.getTableHeader().setReorderingAllowed(false);
        
        LOG.info("> initializing table for " + viewName + " ... OK!");
    }

    private void createRowSorter() {
        rowSorter = new TableRowSorter<TableModel>(this.getModel());
        for (int i=0; i<this.getColumnCount(); i++) {
            rowSorter.setSortable(i, false);
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component com = super.prepareRenderer(renderer, row, column);
        if(dataModel.getData().getFlag(row)){
            com.setForeground(Color.GREEN);
        }else{
            com.setForeground(Color.BLACK);
        }
        return com;
    }
    
    public void refreshTable() {
        this.setModel(this.getModel());
        this.revalidate();
    }
    
    public void stopCellEditing() {
        TableCellEditor ce = SimpleTodoTable.this.getCellEditor();
        if(ce != null){
            ce.stopCellEditing();
        }
    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        String text = null;
        try {
            text = e.getDocument().getText(0, e.getDocument().getLength());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }

        if (text == null || text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String text = null;
        try {
            text = e.getDocument().getText(0, e.getDocument().getLength());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        
        if (text == null || text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
	public WindowAdapter getWindowListener(){
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopCellEditing();
		        dataModel.cancelTimerAndSaveDataToFile();
			}
		};
	}
}
