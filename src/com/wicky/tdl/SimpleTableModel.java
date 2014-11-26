package com.wicky.tdl;
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
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.ssi.main.DataFactory;
import com.ssi.main.SSIConfig;
import com.ssi.main.model.TableDataChangeListener;
import com.ssi.main.view.IView;
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
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
	private Timer timer;
    protected boolean runing;
	private File dataFile;
    
    private IView view;
    
    public SimpleTableModel(IView view) {
    	this.view = view;
    	if(view != null){
        	IDataVector<ISubDataVector> dataFromFile = this.loadDataFromFile(view);
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
		            saveDataToFile();
		            runing = false;
		        }
		        dataChanged = false;
		    }

		}, new Date(System.currentTimeMillis() + appSaveIntervalMillis), appSaveIntervalMillis);
	}
    
	private IDataVector<ISubDataVector> loadDataFromFile(IView view){
		if(view == null) return null;
		String dataFileName = SSIConfig.get(view.getClass().getSimpleName()+".dataFileName");
		if(StringUtil.isEmpty(dataFileName)){
			dataFileName = "/"+view.getClass().getSimpleName()+".db";
			SSIConfig.put(view.getClass().getSimpleName()+".dataFileName", dataFileName);
			SSIConfig.save();
		}
        dataFile = new File(SSIConfig.get("system.profileHome"), dataFileName);
        if(dataFile.isDirectory())dataFile.delete();
        if(!dataFile.exists()){
            try {
				dataFile.createNewFile();
			} catch (IOException e1) {
				dataFile.delete();
				try {
					dataFile.createNewFile();
				} catch (IOException e2) {
					LOG.error("Failed to re-create data file: ", e2);
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
                        LOG.error("Failed to close input stream: ", e);
                        // ignore;
                    }
                    try {
                        dataFile.delete();
                        dataFile.createNewFile();
                    } catch (IOException e) {
                        LOG.error("Failed to re-create data file: ", e);
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
    
    private void saveDataToFile() {
        if(dataFile == null) return;
        try {
            LOG.debug("Saving data for "+this.getViewName()+" ...");
            out = new ObjectOutputStream(new FileOutputStream(dataFile));
            out.flush();
            out.writeObject(this.exportData());
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
	
	public void cancelTimerAndSaveDataToFile() {
		timer.cancel();
        if(!runing){
            LOG.debug("Window Closing!");
            this.saveDataToFile();
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