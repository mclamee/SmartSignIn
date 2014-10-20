package com.wicky.tdl;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ssi.main.DataFactory;

/**
 * Table Model Class for Simple Table: 
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTableModel extends DefaultTableModel {
    
    private static final long serialVersionUID = -3231068754665325732L;
    
    private IDataVector<ISubDataVector> data;
    
    private String viewName;
    
    public SimpleTableModel(String viewName) {
    	this.viewName = viewName;
    	IDataVector<ISubDataVector> dataVector = DataFactory.getDataVector(viewName);
    	if(dataVector != null){
    	    this.data = (IDataVector<ISubDataVector>) dataVector;
    	    this.setDataVector((Vector<ISubDataVector>) dataVector, dataVector.getTitles());
    	}
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        return data.getValueAt(row, column);
    }
    
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
        insertRow(getRowCount(), DataFactory.createSubDataVector(viewName));
        return this.data.size();
    }
    
    public int getColumnIdx(String colName) {
        return data.getColumnIdx(colName);
    }

    public IDataVector<ISubDataVector> exportData() {
        return data;
    }

    public void initData(IDataVector<ISubDataVector> data) {
        if(data instanceof IDataVector){
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
		DataFactory.adjustColumnWidth(simpleTodoTable, viewName);
	}

	public void setupEditorAndRenderer(SimpleTodoTable simpleTodoTable) {
		DataFactory.setupEditorAndRenderer(simpleTodoTable, viewName);
	}

}