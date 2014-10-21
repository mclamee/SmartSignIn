package com.wicky.tdl;

import java.util.Vector;

public interface IDataVector<T> extends Iterable<T>{

	Vector<String> getTitles();

	Object getValueAt(int row, int column);

	Class<?> getColumnClass(int col);

	int size();

	int getColumnIdx(String colName);

	Object elementAt(int column);

	boolean getFlag(int row);
	
	String getTitle();
	
	String getMessage();

}
