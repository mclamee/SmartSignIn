package com.wicky.tdl.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

import com.ssi.i18n.Messages;

public class SubVector extends Vector<Object> implements Serializable{
    private static final long serialVersionUID = -8903166408920564591L;
    
    public SubVector(String qrCode, String name, Boolean flag) {
        super(Arrays.asList(new Object[]{null, qrCode, name, flag}));
    }
    
    public SubVector(String qrCode, String name) {
        super(Arrays.asList(new Object[]{null, qrCode, name, Boolean.FALSE}));
    }
    
    @Override
    public synchronized String toString() {
        return "{"+Messages.getString("RecordView.table.column_title_qrcode")+":\""+this.get(1)
                +"\", "+Messages.getString("RecordView.table.column_title_name")+":\""+this.get(2)
                +"\", "+Messages.getString("RecordView.table.column_title_choose")+":\""+this.get(3)
                +"\"}";
    }
}