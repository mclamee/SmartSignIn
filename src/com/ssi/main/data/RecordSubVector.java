package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.ssi.i18n.Messages;
import com.wicky.tdl.ISubDataVector;

public class RecordSubVector extends Vector<Object> implements Serializable, ISubDataVector{
    private static final long serialVersionUID = -8903166408920564591L;
    
    public RecordSubVector(String qrCode, String name, String salutaion, Boolean flag) {
        super(Arrays.asList(new Object[]{null, qrCode, name, salutaion, new SeeMoreDataVector(qrCode), flag}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("RecordView.table.column_title_qrcode")+":\""+this.get(1)
                +"\", "+Messages.getString("RecordView.table.column_title_name")+":\""+this.get(2)
                +"\", "+Messages.getString("RecordView.table.column_title_salutaion")+":\""+this.get(3)
                +"\", "+Messages.getString("RecordView.table.column_title_more")+":\""+this.get(4)
                +"\", "+Messages.getString("RecordView.table.column_title_choose")+":\""+this.get(5)
                +"\"}";
    }
    
    public String getQrCode(){
    	return (String) this.get(1);
    }
    public String getName(){
    	return (String) this.get(2);
    }
    public String getSalutaion(){
    	return (String) this.get(3);
    }
    public SeeMoreDataVector getMoreInfo(){
        return (SeeMoreDataVector) this.get(4);
    }
    public String getChoose(){
    	return (String) this.get(5);
    }

	@Override
	public void recordDateTime(Date date) {
		this.getMoreInfo().add(date);
	}
}