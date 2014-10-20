package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public class TimesheetDataVector extends Vector<ISubDataVector> implements Serializable, IDataVector<ISubDataVector>{
	private static final long serialVersionUID = -5470225425540779635L;

	private Vector<String> titles = new Vector<String>(Arrays.asList(new String[]{
            Messages.getString("StaffView.timesheet.table.column_title_id"),
            Messages.getString("StaffView.timesheet.table.column_title_date"),
            Messages.getString("StaffView.timesheet.table.column_title_workhrs"),
            Messages.getString("StaffView.timesheet.table.column_title_counts"),
            Messages.getString("StaffView.timesheet.table.column_title_detail")
            }));
    
    private List<Class<?>> columnTypes = Arrays.asList(new Class<?>[]{
    		String.class, 
    		Date.class, 
    		Integer.class, 
    		Integer.class, 
    		Object.class
    		});
    
    public synchronized boolean add(Date date, Integer workhrs, Integer counts, Object detail) {
        return this.add(new TimesheetSubVector(date, workhrs, counts, detail));
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
		return false;
	}
}