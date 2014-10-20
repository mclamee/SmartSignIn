package com.ssi.main;

import java.util.Vector;

import javax.swing.table.TableColumnModel;

import com.ssi.main.data.RecordDataVector;
import com.ssi.main.data.RecordSubVector;
import com.ssi.main.data.StaffDataVector;
import com.ssi.main.data.TimesheetSubVector;
import com.ssi.main.view.RecordView;
import com.ssi.main.view.StaffView;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTodoTable;
import com.wicky.tdl.widget.MyButtonEditor;
import com.wicky.tdl.widget.MyButtonRender;

public class DataFactory {
	public static IDataVector<ISubDataVector> getDataVector(String viewName) {
		if(viewName.equals(RecordView.class.getSimpleName())){
			RecordDataVector data = new RecordDataVector();
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
			RecordSubVector data = new RecordSubVector("", "", "");
			return data;
		}else if(viewName.equals(StaffView.class.getSimpleName())){
			TimesheetSubVector data = new TimesheetSubVector(null, null, null, null);
			return data;
		}
		return null;
	}
	
	public static void adjustColumnWidth(SimpleTodoTable simpleTodoTable, String viewName) {
		if(viewName.equals(RecordView.class.getSimpleName())){
		    TableColumnModel columnModel = simpleTodoTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(1);
		    columnModel.getColumn(1).setPreferredWidth(320);
		    columnModel.getColumn(2).setPreferredWidth(200);
		    columnModel.getColumn(3).setPreferredWidth(30);
		    columnModel.getColumn(4).setPreferredWidth(10);
		    applyMoreInfoButton(simpleTodoTable, columnModel);
		    columnModel.getColumn(5).setPreferredWidth(30);
		}else if(viewName.equals(StaffView.class.getSimpleName())){
			
		}
	}

    private static void applyMoreInfoButton(SimpleTodoTable simpleTodoTable, TableColumnModel columnModel) {
        columnModel.getColumn(4).setCellEditor(new MyButtonEditor(simpleTodoTable));
        columnModel.getColumn(4).setCellRenderer(new MyButtonRender());
    }

}
