package com.wicky.tdl;
import java.awt.Color;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.ssi.i18n.Messages;
import com.ssi.main.SSIConfig;
import com.ssi.model.SimpleTableModel;
import com.wicky.tdl.data.IDataVector;
import com.wicky.tdl.data.ISubDataVector;


/**
 * Main class for the simple todo table project
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTodoTable extends JTable implements ListSelectionListener, DocumentListener{
    private static final long serialVersionUID = -3747203421484558542L;
    
    private static Logger LOG = Logger.getLogger(SimpleTodoTable.class);
    
    public SimpleTableModel dataModel;
    public TableRowSorter<TableModel> rowSorter;
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
	private Timer timer;
    protected boolean dataChanged;
    protected boolean runing;

	private File dataFile;
    
    public SimpleTodoTable(String viewName){
    	LOG.info("VIEWNAME: " + viewName);
        // 1. create data model
		dataModel = new SimpleTableModel(viewName);
        dataModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                dataChanged = true;
            }
        });
        
        // 2. load data vector from saved file
        IDataVector<ISubDataVector> data = loadDataFromFile(viewName);
        
        // 3. assemble data model
        if(data != null){
            dataModel.initData(data);
        }
        
        // 4. setup data model
        this.setModel(dataModel);
        
        // 5. adjust column width
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataModel.adjustColumnWidth(columnModel);
        
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

//        this.addKeyListener(new KeyAdapter() {
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if(e.getKeyCode() == KeyEvent.VK_DELETE){
//                    int selectedRow = SimpleTodoTable.this.getSelectedRow();
//                    if(selectedRow != -1){
//                        stopCellEditing();
//                        int rowCount = dataModel.getRowCount();
//                        if(rowCount != 0){
//                            int result = JOptionPane.showConfirmDialog(SimpleTodoTable.this, Messages.getString("RecordView.table.menu_delete_confirm"), Messages.getString("RecordView.table.menu_delete_confirm_title"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
//                            if(result == JOptionPane.YES_OPTION){
//                                dataModel.removeRow(selectedRow);
//                                if(selectedRow < dataModel.getRowCount()){
//                                    SimpleTodoTable.this.setRowSelectionInterval(selectedRow, selectedRow);
//                                }
//                                stopCellEditing();
//                                refreshTable();
//                            }
//                        }
//                    }
//
//                }
//            }
//        });
        
        ////////////////
        // setup row sorter
        rowSorter = new TableRowSorter<TableModel>(this.getModel());
        rowSorter.setSortable(0, false);
        rowSorter.setSortable(1, false);
        rowSorter.setSortable(2, false);
        rowSorter.setSortable(3, false);
        rowSorter.setSortable(4, false);
        this.setRowSorter(rowSorter);
        
        // setup timer to support automatic save
        int appSaveIntervalMillis = (((int) (1000*60*SSIConfig.getDouble("saveIntervalMinute"))) < 60000)?60000:
        	((int) (1000*60*SSIConfig.getDouble("saveIntervalMinute")));
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                if(dataChanged){
                    runing = true;
                    LOG.debug("Timmer Start! Save Interval Minites: " + SSIConfig.get("saveIntervalMinute"));
                    saveDataToFile();
                    runing = false;
                }
                dataChanged = false;
            }

        }, new Date(System.currentTimeMillis() + appSaveIntervalMillis), appSaveIntervalMillis);
    }

	private IDataVector<ISubDataVector> loadDataFromFile(String viewName){
		dataFile = new File(SSIConfig.get("profileHome"), SSIConfig.get(viewName+".dataFileName"));
        if(dataFile.isDirectory())dataFile.delete();
        if(!dataFile.exists()){
            try {
				dataFile.createNewFile();
			} catch (IOException e1) {
				dataFile.delete();
				try {
					dataFile.createNewFile();
				} catch (IOException e2) {
					LOG.error("try to recreate data file: ", e2);
					return null;
				}
			}
            LOG.debug("No data file exists, creating new file: ["+dataFile.getAbsolutePath()+"]");
        }
        IDataVector<ISubDataVector> data = null;
        try {
            LOG.debug("Reading data file ... ");
			in = new ObjectInputStream(new FileInputStream(dataFile));
            if(in != null){
                Object object = in.readObject();
                if(object instanceof IDataVector){
                    data = (IDataVector<ISubDataVector>) object;
                    LOG.debug(">> Success!");
                }else{
                    LOG.debug(">> Canceled!");
                    LOG.error("Invalid Data Type.");
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.error("Try to close input stream: ", e);
                        // ignore;
                    }
                    try {
                        dataFile.delete();
                        dataFile.createNewFile();
                    } catch (IOException e) {
                        LOG.error("try to recreate data file: ", e);
                        return null;
                    }
                }
            }
            return data;
        } catch (EOFException e) {
            LOG.debug(">> Canceled.");
            LOG.error("File is empty: ", e);
        } catch (ClassNotFoundException e) {
            LOG.debug(">> Canceled.");
            LOG.error("File format issue: ", e);
        } catch (NotSerializableException e){
            LOG.debug(">> Canceled.");
            LOG.error("Cannot load data: ", e);
        } catch (WriteAbortedException e){
            LOG.debug(">> Canceled.");
            LOG.error("Cannot load data: ", e);
        } catch (IOException e) {
        	 LOG.debug(">> Canceled.");
             LOG.error("Cannot load data: ", e);
		}finally{
            if(in != null){
                try {
					in.close();
				} catch (IOException e) {
					// ignore;
				}
            }
        }
        return null;
	}
    
//    @Override
//    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//        Component com = super.prepareRenderer(renderer, row, column);
//        
//        if(getValueAt(row, 3).equals(true)){
//            com.setForeground(Color.GRAY);
//        }else{
//            com.setForeground(Color.BLACK);
//        }
//        return com;
//    }
    
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
    
    public void saveDataToFile() {
        try {
            LOG.debug("Saving data ... ");
            out = new ObjectOutputStream(new FileOutputStream(dataFile));
            out.flush();
            out.writeObject(dataModel.exportData());
            LOG.debug(">> Saved.");
        } catch (FileNotFoundException e1) {
            LOG.debug(">> Canceled.");
            LOG.error("File cannot open: ", e1);
        } catch (IOException e1) {
            LOG.debug(">> Canceled.");
            LOG.error("IOException: ", e1);
        }finally{
            try {
                out.flush();
                out.close();
            } catch (IOException e1) {
                LOG.error("IOException: ", e1);
            }
        }
    }

	public WindowAdapter getWindowListener(){
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
		        TableCellEditor ce = SimpleTodoTable.this.getCellEditor();
		        if(ce != null){
		            ce.stopCellEditing();
		        }
		        timer.cancel();
		        if(!runing){
		            LOG.debug("Window Closing!");
		            SimpleTodoTable.this.saveDataToFile();
		        }
			}
		};
	}
}
