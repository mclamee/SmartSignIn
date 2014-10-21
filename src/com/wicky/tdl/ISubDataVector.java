package com.wicky.tdl;

import java.util.Date;
import java.util.List;

public interface ISubDataVector extends List<Object>{

	Object elementAt(int column);

	void recordDateTime(Date date);

}
