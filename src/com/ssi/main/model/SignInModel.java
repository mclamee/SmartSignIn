package com.ssi.main.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;

import com.ssi.main.Application;
import com.ssi.main.SSIConfig;
import com.ssi.main.data.RecordSubVector;
import com.ssi.main.data.StaffSubVector;
import com.ssi.util.StringUtil;
import com.ssi.util.weather.WeatherHandle;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;


public class SignInModel {
	
	private Map<String, MessageBody> dataMap = new HashMap<>();
	
	public String lookupMessage(String callback){
		if(StringUtil.isEmpty(callback)){
			return null;
		}
		callback = StringUtil.trimAndUpper(callback);
		if(callback.contains("NO")){
			callback = callback.substring(callback.indexOf("NO"));
		}
		
		MessageBody msg = dataMap.get(callback);
		if(msg == null){
			int rowNo = Application.RECORD_VIEW.getTableModel().addRow();
			Application.RECORD_VIEW.getTableModel().setValueAt(callback, rowNo, 1);
			IDataVector<ISubDataVector> data = Application.RECORD_VIEW.getTableModel().getData();
			ISubDataVector subVector = ((Vector<ISubDataVector>)data).get(rowNo);
			
			msg = new MessageBody();
			msg.setMessage(generateMessage(subVector, Application.RECORD_VIEW.getTemplate(), data.getTitles()));
			msg.setVector(subVector);
			
			dataMap.put(callback, msg);
		}
		// record sign-in details to subVector
		msg.getVector().recordDateTime(new Date());
		String message = msg.getMessage();
		
		if(message.contains("##{time}")){
			Date today = new Date();
			SimpleDateFormat smf = new SimpleDateFormat("H点m分 E");
			String dateStr = smf.format(today);
			message = message.replaceAll("##\\{time\\}", dateStr);
		}
		
		if(message.contains("##{weather}")){
			String weather = "";
			try {
			    // 101270101 = 成都
				weather = new WeatherHandle(SSIConfig.get("weather.city")).getWeather();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			message = message.replaceAll("##\\{weather\\}", weather);
		}
		
		return message;
	}
	
//	public static void main(String[] args) {
//		StaffDataVector vc = new StaffDataVector();
//		StaffSubVector vector = new StaffSubVector("NO1123", null, null, null, false);
//		
//		SignInModel signInModel = new SignInModel();
//		signInModel.generateMessage(vector, SSIConfig.get("StaffView.template"), vc.getTitles());
//	}
	
	private String generateMessage(ISubDataVector subVector, String template, Vector<String> titles) {
		
		template = template.replaceAll("\\$\\{time\\}", "##{time}");
		template = template.replaceAll("\\$\\{weather\\}", "##{weather}");
		
		while(template.indexOf("${") != -1){
			int count = 0;
			String testStr = template.substring(template.indexOf("${") + 2, template.indexOf("}"));
			String origStr = template.substring(template.indexOf("${"), template.indexOf("}") + 1); 
			boolean containsCount = testStr.contains("-");
			if(containsCount){
				String[] split = testStr.split("-");
				testStr = split[0];
				count = Integer.valueOf(split[1]);
			}
			
			for(int i = 0; i<titles.size(); i++){
				String title = titles.get(i);
				if(testStr.equalsIgnoreCase(title)){
					String result = (String) subVector.get(i);
					result = StringUtil.trim(result);
					if(count != 0){
						result = StringUtil.subString(result, count, "");
					}
					template = template.replace(origStr, result);
					break;
				}
			}
		}
		template = StringUtil.trim(template);
		// 移除首字母标点符号
		while (template.startsWith(",") || template.startsWith("，")
				|| template.startsWith("。") || template.startsWith(".")) {
			if(template.startsWith(",")){
				template = template.replaceFirst(",", "");
			}else if(template.startsWith("，")){
				template = template.replaceFirst("，", "");
			}else if(template.startsWith(".")){
				template = template.replaceFirst(".", "");
			}else if(template.startsWith("。")){
				template = template.replaceFirst("。", "");
			}
			template = StringUtil.trim(template);
		}
		System.out.println("RESULT: " + template);
		return template;
	}
	
	public void initDataMap() {
		IDataVector<ISubDataVector> custData = Application.RECORD_VIEW.getTableModel().getData();
		String recordTemplate = Application.RECORD_VIEW.getTemplate();
		for (ISubDataVector iSubVector : custData) {
			RecordSubVector custSubVector = (RecordSubVector)iSubVector;
			String custStr = custSubVector.getCustomize();
			MessageBody msg = new MessageBody();
			msg.setMessage(generateMessage(custSubVector, StringUtil.isEmpty(custStr)?recordTemplate:custStr, custData.getTitles()));
			msg.setVector(custSubVector);
			
			dataMap.put(StringUtil.trimAndUpper(custSubVector.getQrCode()), msg);
		}
		
		String staffTemplate = Application.STAFF_VIEW.getTemplate();
		IDataVector<ISubDataVector> staffData = Application.STAFF_VIEW.getTableModel().getData();
		for (ISubDataVector iSubVector : staffData) {
			StaffSubVector staffSubVector = (StaffSubVector)iSubVector;
			String custStr = staffSubVector.getCustomize();
			MessageBody msg = new MessageBody();
			msg.setMessage(generateMessage(staffSubVector, StringUtil.isEmpty(custStr)?staffTemplate:custStr, staffData.getTitles()));
			msg.setVector(staffSubVector);
			
			dataMap.put(StringUtil.trimAndUpper(staffSubVector.getQrCode()), msg);
		}
	}
	
	private class MessageBody{
		private String message;
		private ISubDataVector vector;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public ISubDataVector getVector() {
			return vector;
		}
		public void setVector(ISubDataVector vector) {
			this.vector = vector;
		}
	}
	
}
