package com.wicky.tdl;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.WriteAbortedException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.ssi.main.DataFactory;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.TableDataChangeListener;
import com.ssi.main.view.IView;
import com.ssi.util.DataUtil;
import com.ssi.util.StringUtil;

/**
 * Table Model Class for Simple Table: 
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTableModel extends DefaultTableModel implements TableDataChangeListener {
    private static final long serialVersionUID = -3231068754665325732L;
    
    private static Logger LOG = Logger.getLogger(SimpleTableModel.class);
    
    private IDataVector<ISubDataVector> data;
    private boolean dataChanged;
    

	private Timer timer;
    protected boolean runing;
    private File dataFile = null;
    
    private IView view;
    
    public SimpleTableModel(IView view) {
    	this.view = view;
    	if(view != null){
    		String dataFileName = getDataFileName(view);
            String profilePath = SSIConfig.get("system.profileHome");
    		dataFile = new File(profilePath, dataFileName);
        	IDataVector<ISubDataVector> dataFromFile = DataUtil.loadDataFromFile(dataFile);
        	if(dataFromFile != null){
        		// load data vector from saved file
                this.initData(dataFromFile);
        	}else{
            	IDataVector<ISubDataVector> defaultData = DataFactory.createDataVector(view);
            	this.initData(defaultData);
        	}
        	
        	setupFileSaveTimer();
    	}
    }

	private void setupFileSaveTimer() {
        this.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                dataChanged = true;
            }
        });
		
		// setup timer to support automatic save
		int appSaveIntervalMillis = (((int) (1000*60*SSIConfig.getDouble("system.saveIntervalMinute"))) < 60000)?60000:
		    ((int) (1000*60*SSIConfig.getDouble("system.saveIntervalMinute")));
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
		        if(dataChanged){
		            runing = true;
//                        LOG.debug("Timmer Start! Save Interval Minites: " + SSIConfig.get("system.saveIntervalMinute"));
		            DataUtil.saveDataToFile(dataFile, exportData());
		            runing = false;
		        }
		        dataChanged = false;
		    }

		}, new Date(System.currentTimeMillis() + appSaveIntervalMillis), appSaveIntervalMillis);
	}
    

	private String getDataFileName(IView view) {
		String dataFileName = SSIConfig.get(view.getClass().getSimpleName()+".dataFileName");
		if(StringUtil.isEmpty(dataFileName)){
			dataFileName = "/"+view.getClass().getSimpleName()+".db";
			SSIConfig.put(view.getClass().getSimpleName()+".dataFileName", dataFileName);
			SSIConfig.save();
		}
		return dataFileName;
	}
    
	public void cancelTimerAndSaveDataToFile() {
		timer.cancel();
        if(!runing){
            LOG.debug("Window Closing!");
            DataUtil.saveDataToFile(dataFile, exportData());
        }
	}
    
    @Override
    public Object getValueAt(int row, int column) {
        return data.getValueAt(row, column);
    }
    
    @Override
	public Class<?> getColumnClass(int col) {
        return data.getColumnClass(col);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 0)return false;
        return true;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
    }
    
    public int addRow() {
        insertRow(getRowCount(), DataFactory.createSubDataVector(view));
        return this.data.size() - 1;
    }
    
    public int getColumnIdx(String colName) {
        return data.getColumnIdx(colName);
    }

    public IDataVector<ISubDataVector> exportData() {
        return data;
    }

    public void initData(IDataVector<ISubDataVector> data) {
        if(data != null && data instanceof IDataVector){
            this.data = data;
            this.setDataVector((Vector<ISubDataVector>) data, data.getTitles());
        }
    }
    
    public boolean getFlag(int row){
    	return this.data.getFlag(row);
    }
    
    @Override
    public void removeRow(int row) {
        super.removeRow(row);
    }

	public IDataVector<ISubDataVector> getData() {
		return data;
	}

	public void setData(IDataVector<ISubDataVector> data) {
		this.data = data;
	}

	public void adjustColumnWidth(SimpleTodoTable simpleTodoTable) {
		DataFactory.adjustColumnWidth(simpleTodoTable, view);
	}

	public void setupEditorAndRenderer(SimpleTodoTable simpleTodoTable, IView view) {
		DataFactory.setupEditorAndRenderer(simpleTodoTable, view);
	}

    public String getViewName() {
        return this.view == null?"null":view.getClass().getSimpleName();
    }

	public boolean isDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	@Override
	public void dataChanged() {
		this.dataChanged = true;
	}

}