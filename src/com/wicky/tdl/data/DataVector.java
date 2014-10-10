package com.wicky.tdl.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.Messages;

public class DataVector extends Vector<SubVector> implements Serializable{
    private static final long serialVersionUID = -999861995984107110L;
    
    private Vector<String> titles = new Vector<String>(Arrays.asList(new String[]{
            Messages.getString("RecordView.table.column_title_id"),
            Messages.getString("RecordView.table.column_title_qrcode"),
            Messages.getString("RecordView.table.column_title_name"),
            Messages.getString("RecordView.table.column_title_salutaion"),
            Messages.getString("RecordView.table.column_title_choose")
            }));
    
    private List<Class<?>> columnTypes = Arrays.asList(new Class<?>[]{
    		String.class, 
    		String.class, 
    		String.class, 
    		String.class, 
    		Boolean.class
    		});
    
    public synchronized boolean add(String qrCode, String name, String salutaion, Boolean flag) {
        return this.add(new SubVector(qrCode, name, salutaion, flag));
    }
    
    public synchronized boolean add(String qrCode, String name, String salutaion) {
        return this.add(new SubVector(qrCode, name, salutaion));
    }
    
    public Vector<String> getTitles() {
        return titles;
    }

    public void setTitles(Vector<String> titles) {
        this.titles = titles;
    }

    public Class<?> getColumnClass(int col) {
        return this.columnTypes.get(col);
    }
    
    public int getColumnIdx(String colName) {
        if(colName != null && colName.length() > 0)
        for(int i=0; i<this.titles.size(); i++){
            if(colName.equals(this.titles.get(i))){
                return i;
            }
        }
        return -1;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return row + 1;
        }
        SubVector rowVector = this.elementAt(row);
        return rowVector.elementAt(column);
    }
    
    public synchronized boolean add(Vector<Object> dat) {
        return this.add((String)dat.get(1), (String)dat.get(2), (String)dat.get(3), (Boolean)dat.get(4));
    }
    
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n"); //$NON-NLS-1$
        
        for (int i = 0;i < this.size();i++) {
            SubVector sub = this.get(i);
            sb.append("  ").append(i+1).append(":\"").append(sub).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        sb.append("}\n"); //$NON-NLS-1$
        return sb.toString();
    }
}