package com.wicky.tdl.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.ssi.i18n.Messages;

public class TimesheetSubVector extends Vector<Object> implements Serializable, ISubDataVector{
	private static final long serialVersionUID = -5706860340042149768L;
	public TimesheetSubVector(Date date, Integer workhrs, Integer counts, Object detail) {
        super(Arrays.asList(new Object[]{null, date, workhrs, counts, detail}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("StaffView.timesheet.table.column_title_date")+":\""+this.get(1)
                +"\", "+Messages.getString("StaffView.timesheet.table.column_title_workhrs")+":\""+this.get(2)
                +"\", "+Messages.getString("StaffView.timesheet.table.column_title_counts")+":\""+this.get(3)
                +"\", "+Messages.getString("StaffView.timesheet.table.column_title_detail")+":\""+this.get(4)
                +"\"}";
    }
    
    public Date getDate(){
    	return (Date) this.get(1);
    }
    public Integer getWorkhrs(){
    	return (Integer) this.get(2);
    }
    public Integer getCounts(){
    	return (Integer) this.get(3);
    }
    public Object getDetail(){
    	return (Object) this.get(4);
    }
}