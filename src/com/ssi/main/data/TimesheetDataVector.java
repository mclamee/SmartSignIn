package com.ssi.main.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.ssi.i18n.I18NUtil;
import com.ssi.main.DataFactory;
import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public class TimesheetDataVector extends Vector<ISubDataVector> implements
		Serializable, IDataVector<ISubDataVector> {
	private static final long serialVersionUID = -5470225425540779635L;

	private String title;
	private String message;

	private Vector<String> titles = new Vector<String>(
			Arrays.asList(new String[] {
					I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_id"),
					I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_date"),
					I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_workhrs"),
					I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_counts"),
					I18NUtil.getInstance().getString("StaffView.timesheet.table.column_title_detail") }));

	private List<Class<?>> columnTypes = Arrays.asList(new Class<?>[] {
			String.class, String.class, String.class, Integer.class,
			List.class });

	public TimesheetDataVector(String qrCode) {
		this.title = qrCode;
	}

	public synchronized boolean add(Date date) {
		ISubDataVector sub = findDetailByDate(date);
		if (sub != null) {
			TimesheetSubVector timesheetSubVector = (TimesheetSubVector) sub;
			List<Date> detail = timesheetSubVector.getDetail();
			detail.add(date);
			Collections.sort(detail);

			Date begin = detail.get(0);
			Date end = detail.get(detail.size() - 1);

			long l = end.getTime() - begin.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");

			String workhrs = hour + ":" + (min<10?("0"+min):min) + ":" + (s<10?("0"+s):s);
			int counts = detail.size();
			sub.set(2, workhrs);
			sub.set(3, counts);
			sub.set(4, detail);
			return true;
		} else {
			ArrayList<Date> detail = new ArrayList<Date>();
			detail.add(date);
			String dateStr = DataFactory.smfDate.format(date);
			return this.add(new TimesheetSubVector(dateStr, "", 1, detail));
		}
	}

	@Override
	public Vector<String> getTitles() {
		return titles;
	}

	public void setTitles(Vector<String> titles) {
		this.titles = titles;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return this.columnTypes.get(col);
	}

	@Override
	public int getColumnIdx(String colName) {
		if (colName != null && colName.length() > 0)
			for (int i = 0; i < this.titles.size(); i++) {
				if (colName.equals(this.titles.get(i))) {
					return i;
				}
			}
		return -1;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return row + 1;
		}
		ISubDataVector rowVector = this.elementAt(row);
		return rowVector.elementAt(column);
	}

	@Override
	public synchronized String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n"); //$NON-NLS-1$

		for (int i = 0; i < this.size(); i++) {
			ISubDataVector sub = this.get(i);
			sb.append("  ").append(i + 1).append(":\"").append(sub).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		sb.append("}\n"); //$NON-NLS-1$
		return sb.toString();
	}

	@Override
	public boolean getFlag(int row) {
		return false;
	}

	/**
	 * @return the title
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public ISubDataVector findDetailByDate(Date date) {
		SimpleDateFormat smf = DataFactory.smfDate;
		String dateStr = smf.format(date);
		for (ISubDataVector sub : this) {
			String recordString = ((TimesheetSubVector) sub).getDate();
			if (dateStr.equals(recordString)) {
				return sub;
			}
		}
		return null;
	}
}