package com.wicky.tdl;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.wicky.tdl.data.DataVector;
import com.wicky.tdl.data.SubVector;

/**
 * Table Model Class for Simple Table: 
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SimpleTableModel extends DefaultTableModel {
    
    private static final long serialVersionUID = -3231068754665325732L;
    
    private DataVector data;
    
    public SimpleTableModel() {
        data = new DataVector();
        data.add("NO1024","薛鹏飞", false);
        data.add("NO1025","吕静泽", false);
        data.add("NO1026","张曜嵩", false);
        data.add("NO1027","王宁", false);
        data.add("NO1028","杨洁", false);
        
        this.setDataVector(data, data.getTitles());
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
        insertRow(getRowCount(), new SubVector("", ""));
        return this.data.size();
    }
    
    public int getColumnIdx(String colName) {
        return data.getColumnIdx(colName);
    }

    public DataVector exportData() {
        return data;
    }

    public void initData(DataVector data) {
        if(data instanceof DataVector){
            this.data = data;
            this.setDataVector(data, data.getTitles());
        }
    }

    public void initOldData(Vector<?> old) {
        this.data = new DataVector();
        for (Object obj:old) {
            @SuppressWarnings("unchecked")
            Vector<Object> dat = (Vector<Object>) obj;
            data.add(dat);
        }
        this.setDataVector(data, data.getTitles());
    }
    
    @Override
    public void removeRow(int row) {
        super.removeRow(row);
    }

}