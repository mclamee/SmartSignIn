package com.wicky.tdl;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

import com.ssi.i18n.Messages;


/**
 * Main class for the simple todo table project
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTodoTable extends JTable implements ListSelectionListener, DocumentListener{
    private static final long serialVersionUID = -3747203421484558542L;
    
    public SimpleTableModel dataModel;
    public TableRowSorter<TableModel> rowSorter;
    
    protected boolean dataChanged;
    protected boolean runing;

    public SimpleTodoTable() {
        // 1. create data model
        dataModel = new SimpleTableModel();

        // 4. setup data model
        this.setModel(dataModel);
        
        // 5. adjust column width
        adjustColumnWidth();
        
        // 6. setup UI to support drag and drop rows
        this.setUI(new DragDropRowTableUI());
        
        // 7. add other components and bind events
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
                            dataModel.removeRow(SimpleTodoTable.this.getSelectedRow());
                            refreshTable();
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
                    int selectedRow = SimpleTodoTable.this.getSelectedRow();
                    if(selectedRow != -1){
                        stopCellEditing();
                        int rowCount = dataModel.getRowCount();
                        if(rowCount != 0){
                            int result = JOptionPane.showConfirmDialog(SimpleTodoTable.this, Messages.getString("RecordView.table.menu_delete_confirm"), Messages.getString("RecordView.table.menu_delete_confirm_title"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
                            if(result == JOptionPane.YES_OPTION){
                                dataModel.removeRow(selectedRow);
                                if(selectedRow < dataModel.getRowCount()){
                                    SimpleTodoTable.this.setRowSelectionInterval(selectedRow, selectedRow);
                                }
                                stopCellEditing();
                                refreshTable();
                            }
                        }
                    }

                }
            }
        });
        
        ////////////////
        // setup row sorter
        rowSorter = new TableRowSorter<TableModel>(this.getModel());
        rowSorter.setSortable(0, false);
        rowSorter.setSortable(1, false);
        rowSorter.setSortable(2, false);
        rowSorter.setSortable(3, false);
        this.setRowSorter(rowSorter);
    }

    /**
     * 调整列宽
     */
    private void adjustColumnWidth() {
        // Tweak the appearance of the table by manipulating its column model
        TableColumnModel colmodel = this.getColumnModel();
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Set column widths
        colmodel.getColumn(0).setPreferredWidth(1);
        colmodel.getColumn(1).setPreferredWidth(320);
        colmodel.getColumn(2).setPreferredWidth(200);
        colmodel.getColumn(3).setPreferredWidth(30);
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component com = super.prepareRenderer(renderer, row, column);
        
        if(getValueAt(row, 3).equals(true)){
            com.setForeground(Color.GRAY);
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
}
