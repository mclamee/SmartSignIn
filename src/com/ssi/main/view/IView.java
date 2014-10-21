package com.ssi.main.view;

import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;

public interface IView {

	void openSubDialog(String title, String message, IDataVector<ISubDataVector> idata);

	void closeSubDialog();

	String getTemplate();

}
