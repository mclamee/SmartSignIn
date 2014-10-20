package com.ssi.main.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

import com.ssi.i18n.Messages;

public class DailyRecordsVector extends Vector<Object> implements Serializable{
    private static final long serialVersionUID = -8903166408920564591L;
    
    public DailyRecordsVector(String qrCode, String name, String salutaion, Boolean flag) {
        super(Arrays.asList(new Object[]{null, qrCode, name, salutaion, flag}));
    }
    
    public DailyRecordsVector(String qrCode, String name, String salutaion) {
        super(Arrays.asList(new Object[]{null, qrCode, name, salutaion, Boolean.FALSE}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("RecordView.table.column_title_qrcode")+":\""+this.get(1)
                +"\", "+Messages.getString("RecordView.table.column_title_name")+":\""+this.get(2)
                +"\", "+Messages.getString("RecordView.table.column_title_salutaion")+":\""+this.get(3)
                +"\", "+Messages.getString("RecordView.table.column_title_choose")+":\""+this.get(4)
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
    public String getChoose(){
    	return (String) this.get(4);
    }
}