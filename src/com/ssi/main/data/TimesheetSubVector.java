package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.I18NUtil;
import com.wicky.tdl.ISubDataVector;

public class TimesheetSubVector extends Vector<Object> implements Serializable, ISubDataVector{
	private static final long serialVersionUID = -5706860340042149768L;
	public TimesheetSubVector(String date, String workhrs, Integer counts, List<Date> detail) {
        super(Arrays.asList(new Object[]{null, date, workhrs, counts, detail}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_date")+":\""+this.get(1)
                +"\", "+I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_workhrs")+":\""+this.get(2)
                +"\", "+I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_counts")+":\""+this.get(3)
                +"\", "+I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_detail")+":\""+this.get(4)
                +"\"}";
    }
    
    public String getDate(){
    	return (String) this.get(1);
    }
    public String getWorkhrs(){
    	return (String) this.get(2);
    }
    public Integer getCounts(){
    	return (Integer) this.get(3);
    }
    public List<Date> getDetail(){
    	return (List<Date>) this.get(4);
    }
	@Override
	public void recordDateTime(Date date) {
//		this.getMoreInfo().add(date);
	}
}