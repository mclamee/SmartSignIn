package com.ssi.main.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.ssi.main.DataFactory;
import com.wicky.tdl.ISubDataVector;

public class SeeMoreSubVector extends Vector<Object> implements Serializable, ISubDataVector{
	private static final long serialVersionUID = -5706863321042149768L;
	public SeeMoreSubVector(Date date) {
        super(Arrays.asList(new Object[]{
                date, 
                DataFactory.smfDate.format(date), 
                DataFactory.smfTime.format(date)
                }));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("RecordView.seemore.table.column_title_date")+":\""+this.get(1)
                +"\", "+Messages.getString("RecordView.seemore.table.column_title_time")+":\""+this.get(2)
                +"\"}";
    }
    
    public Date getDate(){
        return (Date) this.get(0);
    }
    
    public String getDateString(){
    	return (String) this.get(1);
    }
    public String getTimeString(){
    	return (String) this.get(2);
    }
	@Override
	public void recordDateTime(Date date) {
	}
}