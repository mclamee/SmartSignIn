package com.ssi.model;

import java.util.HashMap;
import java.util.Map;

import com.ssi.view.MainView;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.data.DataVector;
import com.wicky.tdl.data.SubVector;
import com.wicky.util.StringUtil;


public class SignInModel {
	
	private Map<String, SubVector> dataMap = new HashMap<>();
	
	public String lookupMessage(String callback){
		if(StringUtil.isEmpty(callback)){
			return null;
		}
		callback = StringUtil.trimAndUpper(callback);
		SubVector subVector = dataMap.get(callback);
		if(subVector == null)return null;
		return generateMessage(subVector);

	}
	
	private String generateMessage(SubVector subVector) {
		String name = subVector.getName();
		String salutaion = subVector.getSalutaion();
		String tailMessage = "欢迎光临拓德公司!";
		return name+salutaion+","+tailMessage;
	}
	public void initDataMap() {
		SimpleTableModel tableModel = MainView.RECORD_VIEW.getTableModel();
		DataVector data = tableModel.getData();
		for (SubVector subVector : data) {
			dataMap.put(StringUtil.trimAndUpper(subVector.getQrCode()), subVector);
		}
	}
	
}
