package com.wicky.tdl;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ssi.main.DataFactory;
import com.ssi.main.view.IView;

/**
 * Table Model Class for Simple Table: 
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTableModel extends DefaultTableModel {
    
    private static final long serialVersionUID = -3231068754665325732L;
    
    private IDataVector<ISubDataVector> data;
    
    private IView view;
    
    public SimpleTableModel(IView view) {
    	this.view = view;
    	IDataVector<ISubDataVector> dataVector = DataFactory.createDataVector(view);
    	if(dataVector != null){
    	    this.data = dataVector;
    	    this.setDataVector((Vector<ISubDataVector>) dataVector, dataVector.getTitles());
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
		DataFactory.adjustColumnWidth(simpleTodoTable, view);
	}

	public void setupEditorAndRenderer(SimpleTodoTable simpleTodoTable, IView view) {
		DataFactory.setupEditorAndRenderer(simpleTodoTable, view);
	}

    public String getViewName() {
        return this.view == null?"null":view.getClass().getSimpleName();
    }

}