package com.ssi.main;

import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import com.ssi.main.data.RecordDataVector;
import com.ssi.main.data.RecordSubVector;
import com.ssi.main.data.SeeMoreDataVector;
import com.ssi.main.data.StaffDataVector;
import com.ssi.main.data.StaffSubVector;
import com.ssi.main.data.TimesheetDataVector;
import com.ssi.main.view.IView;
import com.ssi.main.view.RecordView;
import com.ssi.main.view.StaffView;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;
import com.wicky.tdl.widget.MyButtonEditor;
import com.wicky.tdl.widget.MyButtonRender;

public class DataFactory {
	
	public static SimpleDateFormat smfDate = new SimpleDateFormat("yyyy年M月d日");
	public static SimpleDateFormat smfTime = new SimpleDateFormat("H:mm:ss");
	public static SimpleDateFormat smfDateTime = new SimpleDateFormat();
	
	public static IDataVector<ISubDataVector> createDataVector(IView view) {
		if(view instanceof RecordView){
			RecordDataVector data = new RecordDataVector();
	        data.add("NO1026","张曜嵩", "先生", null, Boolean.FALSE);
	        data.add("NO1027","王宁", "先生", null, Boolean.FALSE);
	        data.add("NO1028","杨洁", "女士", null, Boolean.FALSE);
			return data;
		}else if(view instanceof StaffView){
			StaffDataVector data = new StaffDataVector();
	        data.add("NO1024","薛鹏飞", "经理", null, Boolean.FALSE);
	        data.add("NO1025","吕静泽", "经理", null, Boolean.FALSE);
			return data;
		}
		return null;
	}

	public static Vector<Object> createSubDataVector(IView view) {
		if(view instanceof RecordView){
			RecordSubVector data = new RecordSubVector(null, null, null, null, Boolean.FALSE);
			return data;
		}else if(view instanceof StaffView){
			StaffSubVector data = new StaffSubVector(null, null, null, null, Boolean.FALSE);
			return data;
		}
		return null;
	}
	
	public static void adjustColumnWidth(SimpleTodoTable simpleTodoTable, IView view) {
		TableColumnModel columnModel = simpleTodoTable.getColumnModel();
		if(view instanceof RecordView){
            columnModel.getColumn(0).setPreferredWidth(1);
//		    columnModel.getColumn(1).setPreferredWidth(320);
//		    columnModel.getColumn(2).setPreferredWidth(200);
//		    columnModel.getColumn(3).setPreferredWidth(30);
//		    columnModel.getColumn(4).setPreferredWidth(10);
//		    columnModel.getColumn(5).setPreferredWidth(30);
		}else if(view instanceof StaffView){
		    columnModel.getColumn(0).setPreferredWidth(1);
		}
	}

	public static void setupEditorAndRenderer(SimpleTodoTable simpleTodoTable, IView view) {
		JTextField field = new JTextField();
		simpleTodoTable.setCellEditor(new DefaultCellEditor(field));
		
		TableColumnModel columnModel = simpleTodoTable.getColumnModel();
		
		if(view instanceof RecordView){
			columnModel.getColumn(5).setCellEditor(new MyButtonEditor(simpleTodoTable, view));
	        columnModel.getColumn(5).setCellRenderer(new MyButtonRender());
	        simpleTodoTable.getModel().addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					RecordDataVector recordDataVector = (RecordDataVector)((SimpleTableModel)e.getSource()).getData();
					int firstRow = e.getFirstRow();
					if(firstRow == recordDataVector.size()){
						return;
					}
					RecordSubVector sub = (RecordSubVector)recordDataVector.get(firstRow);
					SeeMoreDataVector moreInfo = sub.getMoreInfo();
					moreInfo.setTitle(sub.getQrCode());
				}
			});
		}else if(view instanceof StaffView){
			columnModel.getColumn(5).setCellEditor(new MyButtonEditor(simpleTodoTable, view));
			columnModel.getColumn(5).setCellRenderer(new MyButtonRender());
			
	        simpleTodoTable.getModel().addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					StaffDataVector staffDataVector = (StaffDataVector)((SimpleTableModel)e.getSource()).getData();
					int firstRow = e.getFirstRow();
					if(firstRow == staffDataVector.size()){
						return;
					}
					StaffSubVector sub = (StaffSubVector)staffDataVector.get(firstRow);
					TimesheetDataVector moreInfo = sub.getMoreInfo();
					moreInfo.setTitle(sub.getQrCode());
				}
			});
		}
	}
}
