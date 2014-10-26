package com.ssi.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ssi.main.Application;
import com.ssi.main.data.RecordSubVector;
import com.ssi.main.data.SeeMoreDataVector;
import com.ssi.main.data.SeeMoreSubVector;
import com.ssi.main.data.StaffSubVector;
import com.ssi.main.data.TimesheetDataVector;
import com.ssi.main.data.TimesheetSubVector;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;


public class ExcelHandle {
	private static final String STAFF_REPORT_PATH = "msc/员工报表.xls";
	private static final String CUST_REPORT_PATH = "msc/客人报表.xls";

	private ExcelHandle() {
	}

	private static ExcelHandle inst;

	public static ExcelHandle getInstance() {
		if (inst == null) {
			inst = new ExcelHandle();
		}
		return inst;
	}
	
	public static void main(String[] args) throws IOException {
		ExcelHandle.getInstance().exportCustReport();
		ExcelHandle.getInstance().exportStaffReport();
	}

	public String exportCustReport() {
		File outFile = new File(STAFF_REPORT_PATH);
		if(outFile.exists()){
			outFile.delete();
		}
		
		IDataVector<ISubDataVector> data = Application.RECORD_VIEW.getTableModel().getData();
		
		FileInputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(new File("res/template/cust_report.xls"));
			HSSFWorkbook hwb = new HSSFWorkbook(in);
			HSSFSheet hst = hwb.getSheetAt(0);
			
			int row = 1;
			for (ISubDataVector sub : data) {
				RecordSubVector s = (RecordSubVector)sub;
				String qrCode = s.getQrCode();
				String name = s.getName();
				String salutaion = s.getSalutaion();
				
				HSSFRow hrw = hst.createRow(row ++);
				hrw.createCell(0).setCellValue(qrCode);
				hrw.createCell(1).setCellValue(name);
				hrw.createCell(2).setCellValue(salutaion);
				
				boolean isFirstRow = true;
				SeeMoreDataVector moreInfo = s.getMoreInfo();
				for (ISubDataVector subMore : moreInfo) {
					SeeMoreSubVector m = (SeeMoreSubVector)subMore;
					Date date = m.getDate();
					SimpleDateFormat smfDate = new SimpleDateFormat("yyyy/M/d");
					String dateStr = smfDate.format(date);
					SimpleDateFormat smfTime = new SimpleDateFormat("HH:mm:ss");
					String timeStr = smfTime.format(date);
					
					if(isFirstRow){
						hrw.createCell(3).setCellValue(dateStr);
						hrw.createCell(4).setCellValue(timeStr);
						isFirstRow = false;
					}else{
						hrw = hst.createRow(row ++);
						hrw.createCell(3).setCellValue(dateStr);
						hrw.createCell(4).setCellValue(timeStr);
					}
				}
				
				hrw = hst.createRow(row ++);
			}
			
			out = new FileOutputStream(CUST_REPORT_PATH);
			hwb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return CUST_REPORT_PATH;
	}

	class StaffReport{
		String name;
		String dateStr;
		String qrCode;
		String title;
		String workhrs;
		Integer counts;
		List<Date> detail;
		
		public String getDateStr() {
			return dateStr;
		}
		public void setDateStr(String dateStr) {
			this.dateStr = dateStr;
		}
		public String getQrCode() {
			return qrCode;
		}
		public void setQrCode(String qrCode) {
			this.qrCode = qrCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getWorkhrs() {
			return workhrs;
		}
		public void setWorkhrs(String workhrs) {
			this.workhrs = workhrs;
		}
		public Integer getCounts() {
			return counts;
		}
		public void setCounts(Integer counts) {
			this.counts = counts;
		}
		public List<Date> getDetail() {
			return detail;
		}
		public void setDetail(List<Date> detail) {
			this.detail = detail;
		}

	}
	
	public String exportStaffReport() {
		
		File outFile = new File(STAFF_REPORT_PATH);
		if(outFile.exists()){
			outFile.delete();
		}
		
		Map<String, List<StaffReport>> dateMap = new HashMap<String, List<StaffReport>>();
		
		IDataVector<ISubDataVector> data = Application.STAFF_VIEW.getTableModel().getData();
		for (ISubDataVector sub : data) {
			StaffSubVector s = (StaffSubVector)sub;
			String qrCode = s.getQrCode();
			String name = s.getName();
			String title = s.getTitle();

			TimesheetDataVector moreInfo = s.getMoreInfo();
			for (ISubDataVector subMore : moreInfo) {
				TimesheetSubVector m = (TimesheetSubVector)subMore;

				String dateStr = m.getDate();
				String workhrs = m.getWorkhrs();
				Integer counts = m.getCounts();
				List<Date> detail = m.getDetail();
				
				StaffReport rpt = new StaffReport();
				rpt.setQrCode(qrCode);
				rpt.setName(name);
				rpt.setTitle(title);
				rpt.setDateStr(dateStr);
				rpt.setWorkhrs(workhrs);
				rpt.setCounts(counts);
				rpt.setDetail(detail);
				
				String key = dateStr;
				List<StaffReport> list = dateMap.get(key);
				if(list == null){
					list = new ArrayList<StaffReport>();
					dateMap.put(key, list);
				}
				list.add(rpt);
			}
		}
			
		FileInputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(new File("res/template/staff_report.xls"));
			HSSFWorkbook hwb = new HSSFWorkbook(in);
			HSSFSheet hst = hwb.getSheetAt(0);
			
			int row = 1;
			for (String dateStr : dateMap.keySet()) {
				List<StaffReport> list = dateMap.get(dateStr);
				boolean isFirstRow = true;
				for (StaffReport rpt : list) {
					HSSFRow hrw = hst.createRow(row ++);
					if(isFirstRow){
						hrw.createCell(0).setCellValue(dateStr);
						isFirstRow = false;
					}
					hrw.createCell(1).setCellValue(rpt.getQrCode());
					hrw.createCell(2).setCellValue(rpt.getName());
					hrw.createCell(3).setCellValue(rpt.getTitle());
					
					// 总工时
					hrw.createCell(4).setCellFormula("LOOKUP(4^8,G"+row+":IV"+row+")-G"+row);
					// 打卡数
					hrw.createCell(5).setCellFormula("COUNTA(G"+row+":IV"+row+")");
					// 记录 6+

					int col = 6;
					for (Date dateDetail : rpt.getDetail()) {
						hrw.createCell(col ++).setCellValue(dateDetail);
					}
				}
				hst.createRow(row ++);
			}
			
			out = new FileOutputStream(STAFF_REPORT_PATH);
			hwb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return STAFF_REPORT_PATH;
	}
}
