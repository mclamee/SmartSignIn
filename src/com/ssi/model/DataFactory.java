package com.ssi.model;

import java.util.Vector;

import javax.swing.table.TableColumnModel;

import com.ssi.view.RecordView;
import com.ssi.view.StaffView;
import com.wicky.tdl.data.IDataVector;
import com.wicky.tdl.data.ISubDataVector;
import com.wicky.tdl.data.SignInDataVector;
import com.wicky.tdl.data.SignInSubVector;
import com.wicky.tdl.data.StaffDataVector;
import com.wicky.tdl.data.TimesheetSubVector;

public class DataFactory {
	public static IDataVector<ISubDataVector> getDataVector(String viewName) {
		if(viewName.equals(RecordView.class.getSimpleName())){
			SignInDataVector data = new SignInDataVector();
	        data.add("NO1026","张曜嵩", "先生", false);
	        data.add("NO1027","王宁", "先生", false);
	        data.add("NO1028","杨洁", "女士", false);
			return data;
		}else if(viewName.equals(StaffView.class.getSimpleName())){
			StaffDataVector data = new StaffDataVector();
	        data.add("NO1024","薛鹏飞", "经理", false);
	        data.add("NO1025","吕静泽", "经理", false);
			return data;
		}
		return null;
	}

	public static Vector<Object> createSubDataVector(String viewName) {
		if(viewName.equals(RecordView.class.getSimpleName())){
			SignInSubVector data = new SignInSubVector("", "", "");
			return data;
		}else if(viewName.equals(StaffView.class.getSimpleName())){
			TimesheetSubVector data = new TimesheetSubVector(null, null, null, null);
			return data;
		}
		return null;
	}
	
	public static void adjustColumnWidth(TableColumnModel colmodel, String viewName) {
		if(viewName.equals(RecordView.class.getSimpleName())){
		    colmodel.getColumn(0).setPreferredWidth(1);
		    colmodel.getColumn(1).setPreferredWidth(320);
		    colmodel.getColumn(2).setPreferredWidth(200);
		    colmodel.getColumn(3).setPreferredWidth(30);
		    colmodel.getColumn(4).setPreferredWidth(30);
		}else if(viewName.equals(StaffView.class.getSimpleName())){
			
		}
	}

}
