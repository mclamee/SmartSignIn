package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.wicky.tdl.ISubDataVector;

public class StaffSubVector extends Vector<Object> implements Serializable, ISubDataVector{
    private static final long serialVersionUID = -8903166408920564591L;
    
    public StaffSubVector(String qrCode, String name, String title, String customize, Boolean flag) {
        super(Arrays.asList(new Object[]{null, qrCode, name, title, customize, new TimesheetDataVector(qrCode), flag}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("StaffView.table.column_title_qrcode")+":\""+this.get(1)
                +"\", "+Messages.getString("StaffView.table.column_title_name")+":\""+this.get(2)
                +"\", "+Messages.getString("StaffView.table.column_title_title")+":\""+this.get(3)
                +"\", "+Messages.getString("StaffView.table.column_title_customize")+":\""+this.get(4)
                +"\", "+Messages.getString("StaffView.table.column_title_more")+":\""+this.get(5)
                +"\", "+Messages.getString("StaffView.table.column_title_choose")+":\""+this.get(6)
                +"\"}";
    }
    
    public String getQrCode(){
    	return (String) this.get(1);
    }
    public String getName(){
    	return (String) this.get(2);
    }
    public String getTitle(){
    	return (String) this.get(3);
    }
    public String getCustomize(){
        return (String) this.get(4);
    }
    public TimesheetDataVector getMoreInfo(){
        return (TimesheetDataVector) this.get(5);
    }
    public String getChoose(){
    	return (String) this.get(6);
    }
    
	@Override
	public void recordDateTime(Date date) {
		this.getMoreInfo().add(date);
	}
}