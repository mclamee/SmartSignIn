package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public class RecordDataVector extends Vector<ISubDataVector> implements Serializable, IDataVector<ISubDataVector>{
    private static final long serialVersionUID = -999861995984107110L;
    
    private String title;
    private String message;
    
    private Vector<String> titles = new Vector<String>(Arrays.asList(new String[]{
            Messages.getString("RecordView.table.column_title_id"),
            Messages.getString("RecordView.table.column_title_qrcode"),
            Messages.getString("RecordView.table.column_title_name"),
            Messages.getString("RecordView.table.column_title_salutaion"),
            Messages.getString("RecordView.table.column_title_customize"),
            Messages.getString("RecordView.table.column_title_more"),
            Messages.getString("RecordView.table.column_title_choose")
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
    
    public synchronized boolean add(String qrCode, String name, String salutaion, String customize, Boolean flag) {
        return this.add(new RecordSubVector(qrCode, name, salutaion, customize, flag));
    }
    
    @Override
	public Vector<String> getTitles() {
        return titles;
    }

    public void setTitles(Vector<String> titles) {
        this.titles = titles;
    }

    @Override
	public Class<?> getColumnClass(int col) {
        return this.columnTypes.get(col);
    }
    
    @Override
	public int getColumnIdx(String colName) {
        if(colName != null && colName.length() > 0)
        for(int i=0; i<this.titles.size(); i++){
            if(colName.equals(this.titles.get(i))){
                return i;
            }
        }
        return -1;
    }

    @Override
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
		return (Boolean)this.getValueAt(row, columnTypes.size() - 1);
	}

    /**
     * @return the title
     */
    @Override
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
    @Override
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