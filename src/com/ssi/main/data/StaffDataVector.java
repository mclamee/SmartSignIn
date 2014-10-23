package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public class StaffDataVector extends Vector<ISubDataVector> implements Serializable, IDataVector<ISubDataVector>{
    private static final long serialVersionUID = -999861995984107110L;
    
    private String title;
    private String message;
    
    private Vector<String> titles = new Vector<String>(Arrays.asList(new String[]{
            Messages.getString("StaffView.table.column_title_id"),
            Messages.getString("StaffView.table.column_title_qrcode"),
            Messages.getString("StaffView.table.column_title_name"),
            Messages.getString("StaffView.table.column_title_title"),
            Messages.getString("StaffView.table.column_title_customize"),
            Messages.getString("StaffView.table.column_title_more"),
            Messages.getString("StaffView.table.column_title_choose")
            }));
    
    private List<Class<?>> columnTypes = Arrays.asList(new Class<?>[]{
    		String.class, 
    		String.class, 
    		String.class, 
    		String.class, 
    		String.class, 
    		IDataVector.class,
    		Boolean.class
    		});
    
    public synchronized boolean add(String qrCode, String name, String title, String customize, Boolean flag) {
        return this.add(new StaffSubVector(qrCode, name, title, customize, flag));
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
        ISubDataVector rowVector = this.elementAt(row);
        return rowVector.elementAt(column);
    }
    
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n"); //$NON-NLS-1$
        
        for (int i = 0;i < this.size();i++) {
        	ISubDataVector sub = this.get(i);
            sb.append("  ").append(i+1).append(":\"").append(sub).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        sb.append("}\n"); //$NON-NLS-1$
        return sb.toString();
    }
    
	@Override
	public boolean getFlag(int row) {
		return (boolean)this.getValueAt(row, columnTypes.size() - 1);
	}

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
	
}