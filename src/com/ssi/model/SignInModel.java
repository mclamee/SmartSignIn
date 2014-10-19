package com.ssi.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ssi.main.Application;
import com.ssi.util.StringUtil;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.data.DataVector;
import com.wicky.tdl.data.SubVector;


public class SignInModel {
	
	private Map<String, SubVector> dataMap = new HashMap<>();
	
	public String lookupMessage(String callback){
		if(StringUtil.isEmpty(callback)){
			return null;
		}
		callback = StringUtil.trimAndUpper(callback);
		SubVector subVector = dataMap.get(callback);
		if(subVector == null){
			// TODO when subVector is null, add it to the list
			return null;
		}
		// record sign-in details to subVector
		Date today = new Date();
		
		return generateMessage(subVector);

	}
	
	private String generateMessage(SubVector subVector) {
		String name = subVector.getName();
		String salutaion = subVector.getSalutaion();
		String tailMessage = "欢迎光临拓德公司!";
		return name+salutaion+","+tailMessage;
	}
	public void initDataMap() {
		SimpleTableModel tableModel = Application.RECORD_VIEW.getTableModel();
		DataVector data = tableModel.getData();
		for (SubVector subVector : data) {
			dataMap.put(StringUtil.trimAndUpper(subVector.getQrCode()), subVector);
		}
	}
	
}
