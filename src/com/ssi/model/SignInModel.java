package com.ssi.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ssi.main.Application;
import com.ssi.util.StringUtil;
import com.wicky.tdl.data.IDataVector;
import com.wicky.tdl.data.ISubDataVector;
import com.wicky.tdl.data.SignInSubVector;


public class SignInModel {
	
	private Map<String, ISubDataVector> dataMap = new HashMap<>();
	
	public String lookupMessage(String callback){
		if(StringUtil.isEmpty(callback)){
			return null;
		}
		callback = StringUtil.trimAndUpper(callback);
		ISubDataVector subVector = dataMap.get(callback);
		if(subVector == null){
			// TODO when subVector is null, add it to the list
			return null;
		}
		// record sign-in details to subVector
		Date today = new Date();
		
		return generateMessage(subVector);

	}
	
	private String generateMessage(ISubDataVector subVector) {
		SignInSubVector signInSubVector = (SignInSubVector)subVector;
		String name = signInSubVector.getName();
		String salutaion = signInSubVector.getSalutaion();
		String tailMessage = "欢迎光临拓德公司!";
		return name+salutaion+","+tailMessage;
	}
	
	public void initDataMap() {
		SimpleTableModel tableModel = Application.RECORD_VIEW.getTableModel();
		IDataVector<ISubDataVector> data = tableModel.getData();
		for (ISubDataVector subVector : data) {
			SignInSubVector signInSubVector = (SignInSubVector)subVector;
			dataMap.put(StringUtil.trimAndUpper(signInSubVector.getQrCode()), subVector);
		}
	}
	
}
